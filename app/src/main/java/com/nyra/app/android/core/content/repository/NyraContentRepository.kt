package com.nyra.app.android.core.content.repository

import com.nyra.app.android.core.content.availability.NyraContentAvailability
import com.nyra.app.android.core.content.availability.NyraFeatureState
import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.ReflectionPrompt
import com.nyra.app.android.core.content.model.SingleTraitInterpretation
import com.nyra.app.android.core.content.model.TraitDefinition

interface NyraContentRepository {
    suspend fun loadContent(): NyraContentAvailability
    fun getContentState(): NyraFeatureState
    fun getTrait(id: String): TraitDefinition?
    fun getArea(id: String): LifeAreaDefinition?
    fun getMbtiResult(type: String): MbtiResultDefinition?
    fun getAstrologyPlacement(planet: String, sign: String): AstrologyPlacementDefinition?
    fun getCombosForArea(areaId: String): List<AreaComboRule>
    fun getSingleTraitInterpretations(
        areaId: String,
        traitIds: Collection<String>
    ): List<SingleTraitInterpretation>
    fun getPromptsByTags(tags: Collection<String>): List<ReflectionPrompt>
    fun getArchetype(id: String): ArchetypeDefinition?
    fun getArchetypes(): List<ArchetypeDefinition>
}
