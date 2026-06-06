package com.nyra.app.android.core.profile.resolver

import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.profile.model.NyraDimensionProfile
import com.nyra.app.android.core.profile.model.NyraVisualState
import javax.inject.Inject

class VisualStateResolver @Inject constructor() {

    fun resolve(
        placements: List<AstrologyPlacementDefinition>,
        activeArchetypes: List<ArchetypeDefinition>,
        aestheticTags: List<String>,
        dimensions: NyraDimensionProfile
    ): List<NyraVisualState> {
        val scores = mutableMapOf<NyraVisualState, Double>()

        (placements.flatMap { it.visualStateBias } + activeArchetypes.flatMap { it.visualStateBias })
            .forEach { bias -> scores.addBias(bias, DIRECT_BIAS_WEIGHT) }

        aestheticTags.forEach { tag -> scores.addBias(tag, TAG_WEIGHT) }

        dimensions.dimensions.forEach { (dimension, value) ->
            dimensionStateMap[dimension]?.forEach { state ->
                scores[state] = (scores[state] ?: 0.0) + value.coerceIn(0.0, 1.0) * DIMENSION_WEIGHT
            }
        }

        val resolved = scores
            .entries
            .sortedWith(compareByDescending<Map.Entry<NyraVisualState, Double>> { it.value }.thenBy { it.key.name })
            .map { it.key }
            .take(MAX_STATES)

        return resolved.ifEmpty { listOf(NyraVisualState.WARM_HORIZON) }
    }

    private fun MutableMap<NyraVisualState, Double>.addBias(value: String, weight: Double) {
        val normalized = value.lowercase()
        val states = directStateMap[normalized] ?: tagStateMap[normalized] ?: return
        states.forEach { state -> this[state] = (this[state] ?: 0.0) + weight }
    }

    private companion object {
        const val MAX_STATES = 2
        const val DIRECT_BIAS_WEIGHT = 1.0
        const val TAG_WEIGHT = 0.45
        const val DIMENSION_WEIGHT = 0.55

        val directStateMap = mapOf(
            "warm_horizon" to listOf(NyraVisualState.WARM_HORIZON),
            "night_reflection" to listOf(NyraVisualState.NIGHT_REFLECTION),
            "deep_water" to listOf(NyraVisualState.DEEP_WATER),
            "gold_warmth" to listOf(NyraVisualState.GOLD_WARMTH),
            "soft_dawn" to listOf(NyraVisualState.SOFT_DAWN),
            "crystal_clarity" to listOf(NyraVisualState.CRYSTAL_CLARITY),
            "horizon_motion" to listOf(NyraVisualState.HORIZON_MOTION),
            "velvet_intimacy" to listOf(NyraVisualState.VELVET_INTIMACY),
            "flow_state" to listOf(NyraVisualState.HORIZON_MOTION, NyraVisualState.SOFT_DAWN),
            "deep_focus" to listOf(NyraVisualState.NIGHT_REFLECTION, NyraVisualState.CRYSTAL_CLARITY)
        )

        val tagStateMap = mapOf(
            "moonlight" to listOf(NyraVisualState.NIGHT_REFLECTION),
            "night" to listOf(NyraVisualState.NIGHT_REFLECTION),
            "fog" to listOf(NyraVisualState.NIGHT_REFLECTION),
            "water" to listOf(NyraVisualState.DEEP_WATER),
            "deep_water" to listOf(NyraVisualState.DEEP_WATER),
            "velvet" to listOf(NyraVisualState.VELVET_INTIMACY),
            "rose" to listOf(NyraVisualState.VELVET_INTIMACY, NyraVisualState.GOLD_WARMTH),
            "gold" to listOf(NyraVisualState.GOLD_WARMTH),
            "flame" to listOf(NyraVisualState.GOLD_WARMTH),
            "ember" to listOf(NyraVisualState.GOLD_WARMTH),
            "dawn" to listOf(NyraVisualState.SOFT_DAWN),
            "paper" to listOf(NyraVisualState.CRYSTAL_CLARITY),
            "glass" to listOf(NyraVisualState.CRYSTAL_CLARITY),
            "silver" to listOf(NyraVisualState.CRYSTAL_CLARITY),
            "horizon" to listOf(NyraVisualState.WARM_HORIZON, NyraVisualState.HORIZON_MOTION),
            "wind" to listOf(NyraVisualState.HORIZON_MOTION),
            "spark" to listOf(NyraVisualState.HORIZON_MOTION)
        )

        val dimensionStateMap = mapOf(
            "depth" to listOf(NyraVisualState.DEEP_WATER, NyraVisualState.NIGHT_REFLECTION),
            "symbolism" to listOf(NyraVisualState.NIGHT_REFLECTION),
            "sensitivity" to listOf(NyraVisualState.VELVET_INTIMACY, NyraVisualState.SOFT_DAWN),
            "warmth" to listOf(NyraVisualState.GOLD_WARMTH, NyraVisualState.WARM_HORIZON),
            "harmony" to listOf(NyraVisualState.SOFT_DAWN),
            "groundedness" to listOf(NyraVisualState.WARM_HORIZON),
            "structure" to listOf(NyraVisualState.CRYSTAL_CLARITY),
            "novelty" to listOf(NyraVisualState.HORIZON_MOTION),
            "autonomy" to listOf(NyraVisualState.HORIZON_MOTION),
            "social_energy" to listOf(NyraVisualState.GOLD_WARMTH),
            "transformative_energy" to listOf(NyraVisualState.DEEP_WATER, NyraVisualState.HORIZON_MOTION)
        )
    }
}
