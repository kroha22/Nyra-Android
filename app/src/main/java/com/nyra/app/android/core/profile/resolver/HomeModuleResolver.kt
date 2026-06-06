package com.nyra.app.android.core.profile.resolver

import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.profile.model.NyraAreaScore
import javax.inject.Inject

class HomeModuleResolver @Inject constructor() {

    fun resolve(
        placements: List<AstrologyPlacementDefinition>,
        areas: List<NyraAreaScore>,
        activeArchetypes: List<ArchetypeDefinition>
    ): List<String> {
        val scores = mutableMapOf<String, Double>()

        placements.flatMap { it.preferredHomeModules }
            .forEach { module -> scores.addScore(module, PLACEMENT_MODULE_WEIGHT) }
        activeArchetypes.flatMap { it.preferredHomeModules }
            .forEach { module -> scores.addScore(module, ARCHETYPE_MODULE_WEIGHT) }
        areas.take(TOP_AREAS_FOR_MODULES).forEachIndexed { index, area ->
            scores.addScore(area.areaId, AREA_MODULE_WEIGHT / (index + 1))
            areaModuleAliases[area.areaId]?.forEach { alias ->
                scores.addScore(alias, ALIAS_WEIGHT / (index + 1))
            }
        }

        return scores
            .entries
            .sortedWith(compareByDescending<Map.Entry<String, Double>> { it.value }.thenBy { it.key })
            .map { it.key }
            .take(MAX_MODULES)
            .ifEmpty { DEFAULT_MODULES }
    }

    private fun MutableMap<String, Double>.addScore(module: String, score: Double) {
        if (module.isBlank() || score <= 0.0) return
        this[module] = (this[module] ?: 0.0) + score
    }

    private companion object {
        const val TOP_AREAS_FOR_MODULES = 5
        const val MAX_MODULES = 6
        const val PLACEMENT_MODULE_WEIGHT = 0.7
        const val ARCHETYPE_MODULE_WEIGHT = 1.0
        const val AREA_MODULE_WEIGHT = 0.85
        const val ALIAS_WEIGHT = 0.45

        val DEFAULT_MODULES = listOf("identity", "reflection", "inner_world")
        val areaModuleAliases = mapOf(
            "identity" to listOf("profile", "identity_journal"),
            "inner_world" to listOf("reflection", "inner_world_journal"),
            "relationships" to listOf("relationship_reflection"),
            "creativity" to listOf("creative_prompt"),
            "healing" to listOf("emotional_recovery"),
            "purpose" to listOf("purpose_reflection"),
            "exploration" to listOf("exploration_prompt")
        )
    }
}
