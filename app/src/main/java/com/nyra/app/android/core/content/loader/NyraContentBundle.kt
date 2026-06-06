package com.nyra.app.android.core.content.loader

import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.ReflectionPrompt
import com.nyra.app.android.core.content.model.SingleTraitInterpretation
import com.nyra.app.android.core.content.model.TraitDefinition

data class NyraContentBundle(
    val traits: List<TraitDefinition>,
    val lifeAreas: List<LifeAreaDefinition>,
    val areaComboRules: List<AreaComboRule>,
    val mbtiResults: List<MbtiResultDefinition>,
    val astrologyPlacements: List<AstrologyPlacementDefinition>,
    val singleTraitInterpretations: List<SingleTraitInterpretation>,
    val reflectionPrompts: List<ReflectionPrompt>,
    val archetypes: List<ArchetypeDefinition>
)
