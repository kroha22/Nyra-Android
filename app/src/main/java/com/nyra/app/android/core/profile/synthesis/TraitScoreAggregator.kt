package com.nyra.app.android.core.profile.synthesis

import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.profile.model.NyraTraitScore
import javax.inject.Inject

class TraitScoreAggregator @Inject constructor() {

    fun aggregateTopTraits(
        placements: List<AstrologyPlacementDefinition>,
        mbtiResult: MbtiResultDefinition? = null
    ): List<NyraTraitScore> =
        aggregateTraitSet(
            placements = placements,
            mbtiResult = mbtiResult,
            placementTraits = { it.coreTraits },
            mbtiTraits = { it.coreTraits },
            listWeight = CORE_TRAIT_WEIGHT,
            weightedTraitMultiplier = WEIGHTED_TRAIT_MULTIPLIER
        )

    fun aggregateShadowTraits(
        placements: List<AstrologyPlacementDefinition>,
        mbtiResult: MbtiResultDefinition? = null
    ): List<NyraTraitScore> =
        aggregateTraitSet(
            placements = placements,
            mbtiResult = mbtiResult,
            placementTraits = { it.shadowTraits },
            mbtiTraits = { it.shadowTraits },
            listWeight = SHADOW_GROWTH_WEIGHT,
            weightedTraitMultiplier = 0.0
        )

    fun aggregateGrowthTraits(
        placements: List<AstrologyPlacementDefinition>,
        mbtiResult: MbtiResultDefinition? = null
    ): List<NyraTraitScore> =
        aggregateTraitSet(
            placements = placements,
            mbtiResult = mbtiResult,
            placementTraits = { it.growthTraits },
            mbtiTraits = { it.growthTraits },
            listWeight = SHADOW_GROWTH_WEIGHT,
            weightedTraitMultiplier = 0.0
        )

    private fun aggregateTraitSet(
        placements: List<AstrologyPlacementDefinition>,
        mbtiResult: MbtiResultDefinition?,
        placementTraits: (AstrologyPlacementDefinition) -> List<String>,
        mbtiTraits: (MbtiResultDefinition) -> List<String>,
        listWeight: Double,
        weightedTraitMultiplier: Double
    ): List<NyraTraitScore> {
        val scores = mutableMapOf<String, Double>()

        placements.forEach { placement ->
            val sourceWeight = placement.sourceWeight * placement.confidence
            placementTraits(placement).forEach { traitId ->
                scores.addScore(traitId, listWeight * sourceWeight)
            }
            if (weightedTraitMultiplier > 0.0) {
                placement.traitWeights.forEach { (traitId, weight) ->
                    scores.addScore(traitId, weight * weightedTraitMultiplier * sourceWeight)
                }
            }
        }

        mbtiResult?.let { result ->
            val sourceWeight = result.sourceWeight * result.confidence
            mbtiTraits(result).forEach { traitId ->
                scores.addScore(traitId, listWeight * sourceWeight)
            }
            if (weightedTraitMultiplier > 0.0) {
                result.traitWeights.forEach { (traitId, weight) ->
                    scores.addScore(traitId, weight * weightedTraitMultiplier * sourceWeight)
                }
            }
        }

        return scores
            .map { (traitId, score) -> NyraTraitScore(traitId, score) }
            .sortedWith(compareByDescending<NyraTraitScore> { it.score }.thenBy { it.traitId })
    }

    private fun MutableMap<String, Double>.addScore(traitId: String, score: Double) {
        if (traitId.isBlank() || score <= 0.0) return
        this[traitId] = (this[traitId] ?: 0.0) + score
    }

    private companion object {
        const val CORE_TRAIT_WEIGHT = 1.0
        const val SHADOW_GROWTH_WEIGHT = 0.7
        const val WEIGHTED_TRAIT_MULTIPLIER = 1.0
    }
}
