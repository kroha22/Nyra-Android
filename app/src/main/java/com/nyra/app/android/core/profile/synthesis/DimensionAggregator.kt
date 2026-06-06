package com.nyra.app.android.core.profile.synthesis

import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.TraitDefinition
import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.profile.model.NyraDimensionProfile
import com.nyra.app.android.core.profile.model.NyraTraitScore
import javax.inject.Inject

class DimensionAggregator @Inject constructor(
    private val repository: NyraContentRepository
) {

    fun aggregate(
        placements: List<AstrologyPlacementDefinition>,
        topTraits: List<NyraTraitScore>,
        mbtiResult: MbtiResultDefinition? = null
    ): NyraDimensionProfile {
        val totals = mutableMapOf<String, Double>()
        val weights = mutableMapOf<String, Double>()

        placements.forEach { placement ->
            addWeightedDimensions(
                totals = totals,
                weights = weights,
                dimensions = placement.dimensionProfile,
                sourceWeight = placement.sourceWeight * placement.confidence
            )
        }

        mbtiResult?.let { result ->
            addWeightedDimensions(
                totals = totals,
                weights = weights,
                dimensions = result.dimensionProfile,
                sourceWeight = result.sourceWeight * result.confidence
            )
        }

        val strongestTraitScore = topTraits.maxOfOrNull { it.score }?.takeIf { it > 0.0 } ?: 1.0
        topTraits.take(MAX_TRAITS_FOR_DIMENSIONS).forEach { traitScore ->
            val trait: TraitDefinition = repository.getTrait(traitScore.traitId) ?: return@forEach
            val sourceWeight = TRAIT_DIMENSION_WEIGHT * (traitScore.score / strongestTraitScore)
            addWeightedDimensions(totals, weights, trait.dimensions, sourceWeight)
        }

        return NyraDimensionProfile(
            dimensions = totals.mapValues { (dimension, total) ->
                (total / (weights[dimension] ?: 1.0)).coerceUnit()
            }
        )
    }

    private fun addWeightedDimensions(
        totals: MutableMap<String, Double>,
        weights: MutableMap<String, Double>,
        dimensions: Map<String, Double>,
        sourceWeight: Double
    ) {
        if (sourceWeight <= 0.0) return
        dimensions.forEach { (dimension, value) ->
            totals[dimension] = (totals[dimension] ?: 0.0) + value.coerceUnit() * sourceWeight
            weights[dimension] = (weights[dimension] ?: 0.0) + sourceWeight
        }
    }

    private companion object {
        const val MAX_TRAITS_FOR_DIMENSIONS = 16
        const val TRAIT_DIMENSION_WEIGHT = 0.45
    }
}
