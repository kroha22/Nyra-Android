package com.nyra.app.android.core.profile.synthesis

import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.profile.model.NyraAreaScore
import com.nyra.app.android.core.profile.model.NyraDimensionProfile
import com.nyra.app.android.core.profile.model.NyraTraitScore
import javax.inject.Inject

class AreaScoreCalculator @Inject constructor(
    private val repository: NyraContentRepository
) {

    fun calculate(
        placements: List<AstrologyPlacementDefinition>,
        topTraits: List<NyraTraitScore>,
        dimensions: NyraDimensionProfile,
        mbtiResult: MbtiResultDefinition? = null
    ): List<NyraAreaScore> {
        val placementAreaWeights = mutableMapOf<String, Double>()
        placements.forEach { placement ->
            val sourceWeight = placement.sourceWeight * placement.confidence
            placement.areaWeights.forEach { (areaId, value) ->
                placementAreaWeights[areaId] =
                    (placementAreaWeights[areaId] ?: 0.0) + value.coerceUnit() * sourceWeight
            }
        }
        mbtiResult?.let { result ->
            val sourceWeight = result.sourceWeight * result.confidence
            result.areaWeights.forEach { (areaId, value) ->
                placementAreaWeights[areaId] =
                    (placementAreaWeights[areaId] ?: 0.0) + value.coerceUnit() * sourceWeight
            }
        }

        val normalizedTraitScores = topTraits
            .associate { it.traitId to it.score }
            .normalizedScores()
        val areaIds = placementAreaWeights.keys + DEFAULT_AREA_IDS

        return areaIds
            .mapNotNull { areaId ->
                val area = repository.getArea(areaId) ?: return@mapNotNull null
                val placementScore = placementAreaWeights[areaId] ?: 0.0
                val primaryTraitScore = area.primaryTraits
                    .mapNotNull { normalizedTraitScores[it] }
                    .takeIf { it.isNotEmpty() }
                    ?.average()
                    ?: 0.0
                val dimensionScore = area.dimensionBias
                    .map { (dimension, bias) ->
                        (dimensions.dimensions[dimension] ?: 0.0) * bias.coerceUnit()
                    }
                    .takeIf { it.isNotEmpty() }
                    ?.average()
                    ?: 0.0
                val priorityScore = area.priority.coerceIn(0, 100) / 100.0
                val score = (
                    placementScore * PLACEMENT_WEIGHT +
                        primaryTraitScore * TRAIT_WEIGHT +
                        dimensionScore * DIMENSION_WEIGHT +
                        priorityScore * PRIORITY_WEIGHT
                    ).coerceUnit()
                NyraAreaScore(areaId = area.id, score = score)
            }
            .sortedWith(compareByDescending<NyraAreaScore> { it.score }.thenBy { it.areaId })
    }

    private companion object {
        val DEFAULT_AREA_IDS = listOf(
            "identity",
            "inner_world",
            "relationships",
            "communication",
            "motivation",
            "creativity",
            "growth",
            "stability",
            "healing",
            "purpose",
            "belonging",
            "exploration"
        )
        const val PLACEMENT_WEIGHT = 0.42
        const val TRAIT_WEIGHT = 0.26
        const val DIMENSION_WEIGHT = 0.22
        const val PRIORITY_WEIGHT = 0.10
    }
}
