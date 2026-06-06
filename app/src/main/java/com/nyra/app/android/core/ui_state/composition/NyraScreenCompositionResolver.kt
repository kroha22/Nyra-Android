package com.nyra.app.android.core.ui_state.composition

import com.nyra.app.android.core.profile.model.NyraUserProfile
import javax.inject.Inject

class NyraScreenCompositionResolver @Inject constructor() {

    fun resolveModuleOrder(
        profile: NyraUserProfile,
        primaryDefaultModules: List<String>,
        secondaryDefaultModules: List<String> = emptyList()
    ): List<String> {
        val scores = mutableMapOf<String, Double>()

        profile.preferredHomeModules.forEachIndexed { index, module ->
            scores.add(module, PROFILE_MODULE_WEIGHT / (index + 1))
        }
        profile.areas.take(TOP_AREAS).forEachIndexed { index, area ->
            scores.add(area.areaId, AREA_WEIGHT * area.score / (index + 1))
            areaAliases[area.areaId]?.forEach { alias ->
                scores.add(alias, ALIAS_WEIGHT * area.score / (index + 1))
            }
        }
        primaryDefaultModules.forEachIndexed { index, module ->
            scores.add(module, PRIMARY_DEFAULT_WEIGHT / (index + 1))
        }
        secondaryDefaultModules.forEachIndexed { index, module ->
            scores.add(module, SECONDARY_DEFAULT_WEIGHT / (index + 1))
        }

        return scores.entries
            .sortedWith(compareByDescending<Map.Entry<String, Double>> { it.value }.thenBy { it.key })
            .map { it.key }
            .take(MAX_MODULES)
            .ifEmpty { FALLBACK_MODULES }
    }

    private fun MutableMap<String, Double>.add(module: String, score: Double) {
        if (module.isBlank() || score <= 0.0) return
        this[module] = (this[module] ?: 0.0) + score
    }

    private companion object {
        const val TOP_AREAS = 6
        const val MAX_MODULES = 8
        const val PROFILE_MODULE_WEIGHT = 1.3
        const val AREA_WEIGHT = 0.9
        const val ALIAS_WEIGHT = 0.35
        const val PRIMARY_DEFAULT_WEIGHT = 0.7
        const val SECONDARY_DEFAULT_WEIGHT = 0.35
        val FALLBACK_MODULES = listOf("identity", "reflection", "inner_world")
        val areaAliases = mapOf(
            "inner_world" to listOf("reflection", "inner_world_journal"),
            "relationships" to listOf("relationship_reflection"),
            "creativity" to listOf("creative_prompt"),
            "healing" to listOf("emotional_recovery"),
            "purpose" to listOf("purpose_reflection"),
            "identity" to listOf("profile", "identity_journal"),
            "exploration" to listOf("exploration_prompt")
        )
    }
}
