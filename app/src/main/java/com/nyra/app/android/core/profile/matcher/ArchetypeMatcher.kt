package com.nyra.app.android.core.profile.matcher

import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.profile.model.NyraActiveArchetype
import com.nyra.app.android.core.profile.model.NyraAreaScore
import com.nyra.app.android.core.profile.model.NyraDimensionProfile
import com.nyra.app.android.core.profile.model.NyraTraitScore
import javax.inject.Inject
import kotlin.math.abs

class ArchetypeMatcher @Inject constructor(
    private val repository: NyraContentRepository
) {

    fun match(
        topTraits: List<NyraTraitScore>,
        dimensions: NyraDimensionProfile,
        areas: List<NyraAreaScore>
    ): List<NyraActiveArchetype> {
        val traitIds = topTraits.map { it.traitId }.toSet()
        val areaScores = areas.associate { it.areaId to it.score }

        return repository.getArchetypes()
            .mapNotNull { archetype ->
                val matchedTraits = archetype.requiredTraits.count { it in traitIds }
                if (matchedTraits < archetype.minimumMatch) return@mapNotNull null

                val traitScore = matchedTraits.toDouble() /
                    archetype.requiredTraits.size.coerceAtLeast(1)
                val dimensionScore = similarity(archetype.dimensionBias, dimensions.dimensions)
                val areaScore = similarity(archetype.areaBias, areaScores)
                val rarityBoost = 1.0 + ((archetype.rarity ?: 0.0).coerceIn(0.0, 1.0) * RARITY_BOOST)

                NyraActiveArchetype(
                    archetypeId = archetype.id,
                    score = (
                        (traitScore * TRAIT_WEIGHT +
                            dimensionScore * DIMENSION_WEIGHT +
                            areaScore * AREA_WEIGHT) * rarityBoost
                        ).coerceIn(0.0, 1.0)
                )
            }
            .sortedWith(compareByDescending<NyraActiveArchetype> { it.score }.thenBy { it.archetypeId })
            .take(MAX_ARCHETYPES)
    }

    private fun similarity(
        bias: Map<String, Double>,
        actual: Map<String, Double>
    ): Double {
        if (bias.isEmpty()) return 0.0
        return bias
            .map { (key, expected) ->
                val value = actual[key] ?: 0.0
                1.0 - abs(expected.coerceIn(0.0, 1.0) - value.coerceIn(0.0, 1.0))
            }
            .average()
            .coerceIn(0.0, 1.0)
    }

    private companion object {
        const val MAX_ARCHETYPES = 3
        const val TRAIT_WEIGHT = 0.50
        const val DIMENSION_WEIGHT = 0.30
        const val AREA_WEIGHT = 0.20
        const val RARITY_BOOST = 0.08
    }
}
