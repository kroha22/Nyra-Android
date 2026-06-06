package com.nyra.app.android.core.content.cache

import com.nyra.app.android.core.content.loader.NyraContentBundle
import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.ReflectionPrompt
import com.nyra.app.android.core.content.model.SingleTraitInterpretation
import com.nyra.app.android.core.content.model.TraitDefinition

data class NyraContentCache(
    val traitsById: Map<String, TraitDefinition>,
    val areasById: Map<String, LifeAreaDefinition>,
    val combosByArea: Map<String, List<AreaComboRule>>,
    val mbtiByType: Map<String, MbtiResultDefinition>,
    val astrologyByPlacementId: Map<String, AstrologyPlacementDefinition>,
    val astrologyByPlanetSign: Map<String, AstrologyPlacementDefinition>,
    val singleTraitByArea: Map<String, List<SingleTraitInterpretation>>,
    val promptsByTag: Map<String, List<ReflectionPrompt>>,
    val archetypesById: Map<String, ArchetypeDefinition>
) {
    companion object {
        fun from(bundle: NyraContentBundle): NyraContentCache =
            NyraContentCache(
                traitsById = bundle.traits.associateBy { it.id },
                areasById = bundle.lifeAreas.associateBy { it.id },
                combosByArea = bundle.areaComboRules
                    .groupBy { it.area }
                    .mapValues { (_, rules) -> rules.sortedByDescending { it.weight } },
                mbtiByType = bundle.mbtiResults.associateBy { it.code.uppercase() },
                astrologyByPlacementId = bundle.astrologyPlacements.associateBy { it.id },
                astrologyByPlanetSign = bundle.astrologyPlacements.associateBy {
                    placementKey(it.planet, it.sign)
                },
                singleTraitByArea = bundle.singleTraitInterpretations
                    .groupBy { it.area }
                    .mapValues { (_, interpretations) ->
                        interpretations.sortedByDescending { it.priority }
                    },
                promptsByTag = bundle.reflectionPrompts
                    .flatMap { prompt ->
                        (prompt.tags + prompt.reflectionTags).distinct().map { tag -> tag to prompt }
                    }
                    .groupBy(keySelector = { it.first }, valueTransform = { it.second })
                    .mapValues { (_, prompts) -> prompts.distinctBy { it.id } },
                archetypesById = bundle.archetypes.associateBy { it.id }
            )

        fun placementKey(planet: String, sign: String): String =
            "${planet.lowercase()}_${sign.lowercase()}"
    }
}
