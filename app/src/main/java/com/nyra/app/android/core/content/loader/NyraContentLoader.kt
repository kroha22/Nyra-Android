package com.nyra.app.android.core.content.loader

import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.ReflectionPrompt
import com.nyra.app.android.core.content.model.SingleTraitInterpretation
import com.nyra.app.android.core.content.model.TraitDefinition
import com.nyra.app.android.core.content.validator.ContentValidationError
import com.nyra.app.android.core.content.validator.ContentValidationErrorCode
import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class NyraContentLoader @Inject constructor(
    private val assetReader: NyraContentAssetReader,
    private val json: Json
) {

    fun load(): NyraContentLoadResult {
        val errors = mutableListOf<ContentValidationError>()

        val traits = decodeList<TraitDefinition>(NyraContentFiles.TRAITS, errors)
        val areas = decodeList<LifeAreaDefinition>(NyraContentFiles.LIFE_AREAS, errors)
        val combos = decodeList<AreaComboRule>(NyraContentFiles.AREA_COMBO_RULES, errors)
        val mbti = decodeList<MbtiResultDefinition>(NyraContentFiles.MBTI_RESULTS, errors)
        val astrology = decodeList<AstrologyPlacementDefinition>(
            NyraContentFiles.ASTROLOGY_PLANET_SIGN,
            errors
        )
        val singleTraits = decodeList<SingleTraitInterpretation>(
            NyraContentFiles.SINGLE_TRAIT_INTERPRETATIONS,
            errors
        )
        val prompts = decodeList<ReflectionPrompt>(NyraContentFiles.REFLECTION_PROMPTS, errors)
        val archetypes = decodeList<ArchetypeDefinition>(NyraContentFiles.ARCHETYPES, errors)

        return if (errors.isNotEmpty()) {
            NyraContentLoadResult.Failure(errors)
        } else {
            NyraContentLoadResult.Success(
                NyraContentBundle(
                    traits = traits,
                    lifeAreas = areas,
                    areaComboRules = combos,
                    mbtiResults = mbti,
                    astrologyPlacements = astrology,
                    singleTraitInterpretations = singleTraits,
                    reflectionPrompts = prompts,
                    archetypes = archetypes
                )
            )
        }
    }

    private inline fun <reified T> decodeList(
        path: String,
        errors: MutableList<ContentValidationError>
    ): List<T> {
        val raw = when (val readResult = assetReader.readText(path)) {
            is NyraAssetReadResult.Success -> readResult.content
            is NyraAssetReadResult.Failure -> {
                errors += ContentValidationError(
                    code = ContentValidationErrorCode.MissingFile,
                    source = path,
                    message = readResult.reason
                )
                return emptyList()
            }
        }

        return try {
            json.decodeFromString<List<T>>(raw)
        } catch (exception: SerializationException) {
            errors += ContentValidationError(
                code = ContentValidationErrorCode.ParseError,
                source = path,
                message = exception.message ?: "Unable to parse JSON"
            )
            emptyList()
        } catch (exception: IllegalArgumentException) {
            errors += ContentValidationError(
                code = ContentValidationErrorCode.ParseError,
                source = path,
                message = exception.message ?: "Invalid JSON content"
            )
            emptyList()
        }
    }
}
