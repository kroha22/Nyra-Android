package com.nyra.app.android.core.content

import com.nyra.app.android.core.content.loader.NyraAssetReadResult
import com.nyra.app.android.core.content.loader.NyraContentAssetReader
import com.nyra.app.android.core.content.loader.NyraContentBundle
import com.nyra.app.android.core.content.loader.NyraContentLoadResult
import com.nyra.app.android.core.content.loader.NyraContentLoader
import com.nyra.app.android.core.content.validator.ContentValidationError
import com.nyra.app.android.core.content.validator.NyraContentValidator
import java.io.File
import kotlinx.serialization.json.Json

/**
 * Shared fixtures for the content test suite.
 *
 * Loads canonical content from `app/src/main/assets/canonical/` via a JVM
 * filesystem reader (Android `AssetManager` is not available in unit tests) and
 * caches the parsed bundle for the JVM lifetime so each test file only pays the
 * parse cost once.
 */
internal object ContentTestFixtures {

    /** Project-aware Json — mirrors NyraContentModule. */
    val json: Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /** Cached parsed bundle. Computed lazily on first use. */
    val bundle: NyraContentBundle by lazy { loadOrFail() }

    /** Validator instance — pure, no dependencies. */
    val validator: NyraContentValidator by lazy { NyraContentValidator() }

    /** Loader bound to a real filesystem asset reader. */
    val loader: NyraContentLoader by lazy {
        NyraContentLoader(assetReader = FilesystemAssetReader, json = json)
    }

    /** All canonical file paths under [NyraContentAssetsDir]. */
    val requiredFiles: List<String> = listOf(
        "traits.json",
        "life_areas.json",
        "area_combo_rules.json",
        "mbti_results.json",
        "astrology_planet_sign.json",
        "single_trait_interpretations.json",
        "reflection_prompts.json",
        "archetypes.json"
    )

    private fun loadOrFail(): NyraContentBundle = when (val result = loader.load()) {
        is NyraContentLoadResult.Success -> result.bundle
        is NyraContentLoadResult.Failure -> {
            val summary = result.errors.joinToString("\n") {
                "  - [${it.code}] ${it.source} ${it.id ?: ""}: ${it.message}"
            }
            error("Content failed to load:\n$summary")
        }
    }

    /** Convenience accessor for re-running the validator from tests. */
    fun validate(bundle: NyraContentBundle = ContentTestFixtures.bundle): List<ContentValidationError> =
        validator.validate(bundle)
}

/**
 * Filesystem-based asset reader for unit tests. Resolves the canonical assets
 * directory by walking a few likely working-directory candidates.
 */
internal object FilesystemAssetReader : NyraContentAssetReader {

    override fun readText(path: String): NyraAssetReadResult {
        val file = File(NyraContentAssetsDir.assetsRoot, path)
        if (!file.exists()) {
            return NyraAssetReadResult.Failure(
                path = path,
                reason = "File not found at ${file.absolutePath}"
            )
        }
        return runCatching { NyraAssetReadResult.Success(file.readText(Charsets.UTF_8)) }
            .getOrElse { error ->
                NyraAssetReadResult.Failure(path = path, reason = error.message ?: "Read error")
            }
    }
}

/**
 * Locates the `app/src/main/assets/` directory. Unit tests can be invoked from
 * either the module dir (`app/`) or the repository root, depending on how the
 * Gradle daemon was launched; we try both.
 */
internal object NyraContentAssetsDir {

    val assetsRoot: File by lazy { resolve() }

    private fun resolve(): File {
        val candidates = listOf(
            "src/main/assets",          // working dir = app/
            "app/src/main/assets",      // working dir = repo root
            "../app/src/main/assets"    // working dir = a sibling module
        )
        return candidates
            .map(::File)
            .firstOrNull { it.exists() && it.isDirectory }
            ?: error(
                "Cannot locate Nyra assets. Tried: ${candidates.joinToString { File(it).absolutePath }}"
            )
    }
}
