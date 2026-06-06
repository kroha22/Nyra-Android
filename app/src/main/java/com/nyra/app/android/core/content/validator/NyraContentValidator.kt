package com.nyra.app.android.core.content.validator

import com.nyra.app.android.core.content.loader.NyraContentBundle
import com.nyra.app.android.core.content.model.AreaWeights
import com.nyra.app.android.core.content.model.DimensionProfile
import com.nyra.app.android.core.content.model.LocalizedText
import com.nyra.app.android.core.content.model.TraitWeights
import javax.inject.Inject

class NyraContentValidator @Inject constructor() {

    fun validate(bundle: NyraContentBundle): List<ContentValidationError> {
        val errors = mutableListOf<ContentValidationError>()
        val traitIds = bundle.traits.map { it.id }.toSet()
        val areaIds = bundle.lifeAreas.map { it.id }.toSet()

        errors += duplicateIdErrors("traits.json", bundle.traits.map { it.id })
        errors += duplicateIdErrors("life_areas.json", bundle.lifeAreas.map { it.id })
        errors += duplicateIdErrors("area_combo_rules.json", bundle.areaComboRules.map { it.id })
        errors += duplicateIdErrors("mbti_results.json", bundle.mbtiResults.map { it.id })
        errors += duplicateIdErrors(
            "astrology_planet_sign.json",
            bundle.astrologyPlacements.map { it.id }
        )
        errors += duplicateIdErrors(
            "single_trait_interpretations.json",
            bundle.singleTraitInterpretations.map { it.id }
        )
        errors += duplicateIdErrors(
            "reflection_prompts.json",
            bundle.reflectionPrompts.map { it.id }
        )
        errors += duplicateIdErrors("archetypes.json", bundle.archetypes.map { it.id })

        bundle.traits.forEach { trait ->
            errors += validateWeights("traits.json", trait.id, "dimensions", trait.dimensions)
            errors += validateTraitReferences(
                source = "traits.json",
                ownerId = trait.id,
                field = "related_traits",
                references = trait.relatedTraits,
                traitIds = traitIds
            )
            errors += validateTraitReferences(
                source = "traits.json",
                ownerId = trait.id,
                field = "opposite_traits",
                references = trait.oppositeTraits,
                traitIds = traitIds
            )
        }

        bundle.lifeAreas.forEach { area ->
            errors += validatePriority("life_areas.json", area.id, area.priority)
            errors += validateWeights("life_areas.json", area.id, "dimension_bias", area.dimensionBias)
            errors += validateTraitReferences(
                source = "life_areas.json",
                ownerId = area.id,
                field = "primary_traits",
                references = area.primaryTraits,
                traitIds = traitIds
            )
        }

        bundle.areaComboRules.forEach { combo ->
            errors += validateAreaReference("area_combo_rules.json", combo.id, combo.area, areaIds)
            errors += validateTraitReferences(
                source = "area_combo_rules.json",
                ownerId = combo.id,
                field = "required_traits",
                references = combo.requiredTraits,
                traitIds = traitIds
            )
            errors += validateWeight("area_combo_rules.json", combo.id, "weight", combo.weight)
            errors += validatePriority(
                source = "area_combo_rules.json",
                ownerId = combo.id,
                value = combo.fallbackPriority,
                field = "fallback_priority"
            )
            errors += validateMinimumMatch(
                source = "area_combo_rules.json",
                ownerId = combo.id,
                minimumMatch = combo.minimumMatch,
                referenceCount = combo.requiredTraits.size
            )
            errors += validateLocalizedText("area_combo_rules.json", combo.id, "title", combo.title)
            errors += validateLocalizedText(
                "area_combo_rules.json",
                combo.id,
                "short_summary",
                combo.shortSummary
            )
            errors += validateLocalizedText("area_combo_rules.json", combo.id, "summary", combo.summary)
            combo.shadow?.let { errors += validateLocalizedText("area_combo_rules.json", combo.id, "shadow", it) }
            combo.growth?.let { errors += validateLocalizedText("area_combo_rules.json", combo.id, "growth", it) }
        }

        bundle.mbtiResults.forEach { result ->
            errors += validateLocalizedText("mbti_results.json", result.id, "title", result.title)
            errors += validateLocalizedText(
                "mbti_results.json",
                result.id,
                "short_summary",
                result.shortSummary
            )
            errors += validateLocalizedText("mbti_results.json", result.id, "summary", result.summary)
            result.shadow?.let { errors += validateLocalizedText("mbti_results.json", result.id, "shadow", it) }
            result.growth?.let { errors += validateLocalizedText("mbti_results.json", result.id, "growth", it) }
            errors += validateTraitReferences(
                source = "mbti_results.json",
                ownerId = result.id,
                field = "core_traits",
                references = result.coreTraits,
                traitIds = traitIds
            )
            errors += validateTraitReferences(
                source = "mbti_results.json",
                ownerId = result.id,
                field = "shadow_traits",
                references = result.shadowTraits,
                traitIds = traitIds
            )
            errors += validateTraitReferences(
                source = "mbti_results.json",
                ownerId = result.id,
                field = "growth_traits",
                references = result.growthTraits,
                traitIds = traitIds
            )
            errors += validateAreaWeights("mbti_results.json", result.id, result.areaWeights, areaIds)
            errors += validateTraitWeights("mbti_results.json", result.id, result.traitWeights, traitIds)
            errors += validateWeights(
                "mbti_results.json",
                result.id,
                "dimension_profile",
                result.dimensionProfile
            )
        }

        bundle.astrologyPlacements.forEach { placement ->
            errors += validateLocalizedText(
                "astrology_planet_sign.json",
                placement.id,
                "title",
                placement.title
            )
            errors += validateLocalizedText(
                "astrology_planet_sign.json",
                placement.id,
                "short_summary",
                placement.shortSummary
            )
            errors += validateLocalizedText(
                "astrology_planet_sign.json",
                placement.id,
                "summary",
                placement.summary
            )
            placement.shadow?.let {
                errors += validateLocalizedText("astrology_planet_sign.json", placement.id, "shadow", it)
            }
            placement.growth?.let {
                errors += validateLocalizedText("astrology_planet_sign.json", placement.id, "growth", it)
            }
            errors += validateTraitReferences(
                source = "astrology_planet_sign.json",
                ownerId = placement.id,
                field = "core_traits",
                references = placement.coreTraits,
                traitIds = traitIds
            )
            errors += validateTraitReferences(
                source = "astrology_planet_sign.json",
                ownerId = placement.id,
                field = "shadow_traits",
                references = placement.shadowTraits,
                traitIds = traitIds
            )
            errors += validateTraitReferences(
                source = "astrology_planet_sign.json",
                ownerId = placement.id,
                field = "growth_traits",
                references = placement.growthTraits,
                traitIds = traitIds
            )
            errors += validateAreaWeights(
                "astrology_planet_sign.json",
                placement.id,
                placement.areaWeights,
                areaIds
            )
            errors += validateTraitWeights(
                "astrology_planet_sign.json",
                placement.id,
                placement.traitWeights,
                traitIds
            )
            errors += validateWeights(
                "astrology_planet_sign.json",
                placement.id,
                "dimension_profile",
                placement.dimensionProfile
            )
        }

        bundle.singleTraitInterpretations.forEach { interpretation ->
            errors += validateTraitReference(
                "single_trait_interpretations.json",
                interpretation.id,
                "trait",
                interpretation.trait,
                traitIds
            )
            errors += validateAreaReference(
                "single_trait_interpretations.json",
                interpretation.id,
                interpretation.area,
                areaIds
            )
            errors += validatePriority(
                "single_trait_interpretations.json",
                interpretation.id,
                interpretation.priority
            )
            errors += validateLocalizedText(
                "single_trait_interpretations.json",
                interpretation.id,
                "title",
                interpretation.title
            )
            errors += validateLocalizedText(
                "single_trait_interpretations.json",
                interpretation.id,
                "short_summary",
                interpretation.shortSummary
            )
            errors += validateLocalizedText(
                "single_trait_interpretations.json",
                interpretation.id,
                "summary",
                interpretation.summary
            )
        }

        bundle.reflectionPrompts.forEach { prompt ->
            prompt.lifeAreas.forEach { areaId ->
                errors += validateAreaReference("reflection_prompts.json", prompt.id, areaId, areaIds)
            }
            errors += validateLocalizedText("reflection_prompts.json", prompt.id, "prompt", prompt.prompt)
            prompt.followupPrompt?.let {
                errors += validateLocalizedText(
                    "reflection_prompts.json",
                    prompt.id,
                    "followup_prompt",
                    it
                )
            }
        }

        bundle.archetypes.forEach { archetype ->
            errors += validatePriority("archetypes.json", archetype.id, archetype.priority)
            errors += validateTraitReferences(
                source = "archetypes.json",
                ownerId = archetype.id,
                field = "required_traits",
                references = archetype.requiredTraits,
                traitIds = traitIds
            )
            errors += validateMinimumMatch(
                source = "archetypes.json",
                ownerId = archetype.id,
                minimumMatch = archetype.minimumMatch,
                referenceCount = archetype.requiredTraits.size
            )
            errors += validateWeights("archetypes.json", archetype.id, "dimension_bias", archetype.dimensionBias)
            errors += validateAreaWeights("archetypes.json", archetype.id, archetype.areaBias, areaIds)
            archetype.rarity?.let { errors += validateWeight("archetypes.json", archetype.id, "rarity", it) }
            errors += validateLocalizedText("archetypes.json", archetype.id, "title", archetype.title)
            errors += validateLocalizedText(
                "archetypes.json",
                archetype.id,
                "short_summary",
                archetype.shortSummary
            )
            errors += validateLocalizedText("archetypes.json", archetype.id, "summary", archetype.summary)
            archetype.shadow?.let { errors += validateLocalizedText("archetypes.json", archetype.id, "shadow", it) }
            archetype.growth?.let { errors += validateLocalizedText("archetypes.json", archetype.id, "growth", it) }
        }

        return errors
    }

    private fun duplicateIdErrors(source: String, ids: List<String>): List<ContentValidationError> =
        ids.groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .map { (id, count) ->
                ContentValidationError(
                    code = ContentValidationErrorCode.DuplicateId,
                    source = source,
                    id = id,
                    message = "Duplicate id '$id' appears $count times"
                )
            }

    private fun validateLocalizedText(
        source: String,
        ownerId: String,
        field: String,
        text: LocalizedText
    ): List<ContentValidationError> =
        if (text.isComplete) {
            emptyList()
        } else {
            listOf(
                ContentValidationError(
                    code = ContentValidationErrorCode.MissingLocalization,
                    source = source,
                    id = ownerId,
                    message = "$field must contain non-empty en and ru values"
                )
            )
        }

    private fun validateAreaWeights(
        source: String,
        ownerId: String,
        weights: AreaWeights,
        areaIds: Set<String>
    ): List<ContentValidationError> =
        validateWeights(source, ownerId, "area_weights", weights) +
            weights.keys.flatMap { validateAreaReference(source, ownerId, it, areaIds) }

    private fun validateTraitWeights(
        source: String,
        ownerId: String,
        weights: TraitWeights,
        traitIds: Set<String>
    ): List<ContentValidationError> =
        validateWeights(source, ownerId, "trait_weights", weights) +
            weights.keys.flatMap {
                validateTraitReference(source, ownerId, "trait_weights", it, traitIds)
            }

    private fun validateWeights(
        source: String,
        ownerId: String,
        field: String,
        weights: DimensionProfile
    ): List<ContentValidationError> =
        weights.flatMap { (key, value) -> validateWeight(source, ownerId, "$field.$key", value) }

    private fun validateWeight(
        source: String,
        ownerId: String,
        field: String,
        value: Double
    ): List<ContentValidationError> =
        if (value in 0.0..1.0) {
            emptyList()
        } else {
            listOf(
                ContentValidationError(
                    code = ContentValidationErrorCode.InvalidWeight,
                    source = source,
                    id = ownerId,
                    message = "$field must be in 0.0..1.0, found $value"
                )
            )
        }

    private fun validatePriority(
        source: String,
        ownerId: String,
        value: Int,
        field: String = "priority"
    ): List<ContentValidationError> =
        if (value in 0..100) {
            emptyList()
        } else {
            listOf(
                ContentValidationError(
                    code = ContentValidationErrorCode.InvalidPriority,
                    source = source,
                    id = ownerId,
                    message = "$field must be in 0..100, found $value"
                )
            )
        }

    private fun validateTraitReferences(
        source: String,
        ownerId: String,
        field: String,
        references: List<String>,
        traitIds: Set<String>
    ): List<ContentValidationError> =
        references.flatMap { validateTraitReference(source, ownerId, field, it, traitIds) }

    private fun validateTraitReference(
        source: String,
        ownerId: String,
        field: String,
        reference: String,
        traitIds: Set<String>
    ): List<ContentValidationError> =
        if (reference in traitIds) {
            emptyList()
        } else {
            listOf(
                ContentValidationError(
                    code = ContentValidationErrorCode.InvalidReference,
                    source = source,
                    id = ownerId,
                    message = "$field references missing trait '$reference'"
                )
            )
        }

    private fun validateAreaReference(
        source: String,
        ownerId: String,
        reference: String,
        areaIds: Set<String>
    ): List<ContentValidationError> =
        if (reference in areaIds) {
            emptyList()
        } else {
            listOf(
                ContentValidationError(
                    code = ContentValidationErrorCode.InvalidReference,
                    source = source,
                    id = ownerId,
                    message = "References missing area '$reference'"
                )
            )
        }

    private fun validateMinimumMatch(
        source: String,
        ownerId: String,
        minimumMatch: Int,
        referenceCount: Int
    ): List<ContentValidationError> =
        if (minimumMatch in 1..referenceCount) {
            emptyList()
        } else {
            listOf(
                ContentValidationError(
                    code = ContentValidationErrorCode.InvalidRule,
                    source = source,
                    id = ownerId,
                    message = "minimum_match must be between 1 and required trait count $referenceCount"
                )
            )
        }
}
