package com.nyra.app.android.core.content

import java.io.File
import org.junit.Test
import org.junit.Assert.assertTrue

/**
 * Step 1: every required canonical JSON file exists in `assets/canonical/` and is non-empty.
 */
class NyraContentAssetsExistTest {

    private val canonicalDir = File(NyraContentAssetsDir.assetsRoot, "canonical")

    @Test
    fun canonicalDirectoryExists() {
        assertTrue(
            "canonical/ directory missing at ${canonicalDir.absolutePath}",
            canonicalDir.exists() && canonicalDir.isDirectory
        )
    }

    @Test
    fun everyRequiredFileExists() {
        val missing = ContentTestFixtures.requiredFiles
            .filterNot { File(canonicalDir, it).exists() }
        assertTrue(
            "Missing required asset files: $missing (dir: ${canonicalDir.absolutePath})",
            missing.isEmpty()
        )
    }

    @Test
    fun everyRequiredFileIsNonEmpty() {
        val empty = ContentTestFixtures.requiredFiles
            .map { File(canonicalDir, it) }
            .filter { it.exists() && it.length() == 0L }
            .map { it.name }
        assertTrue("Empty asset files: $empty", empty.isEmpty())
    }
}
