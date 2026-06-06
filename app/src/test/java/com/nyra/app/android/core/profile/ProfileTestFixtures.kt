package com.nyra.app.android.core.profile

import com.nyra.app.android.core.content.availability.NyraFeatureState
import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.AstrologyPlacementDefinition
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.content.model.LocalizedText
import com.nyra.app.android.core.content.model.MbtiResultDefinition
import com.nyra.app.android.core.content.model.ReflectionPrompt
import com.nyra.app.android.core.content.model.SingleTraitInterpretation
import com.nyra.app.android.core.content.model.TraitDefinition
import com.nyra.app.android.core.content.repository.NyraContentRepository

internal fun text(value: String = "Text") = LocalizedText(en = value, ru = value)

internal fun placement(
    id: String = "sun_cancer",
    coreTraits: List<String> = listOf("emotional_depth"),
    shadowTraits: List<String> = emptyList(),
    growthTraits: List<String> = emptyList(),
    traitWeights: Map<String, Double> = emptyMap(),
    areaWeights: Map<String, Double> = emptyMap(),
    dimensionProfile: Map<String, Double> = emptyMap(),
    aestheticTags: List<String> = emptyList(),
    reflectionTags: List<String> = emptyList(),
    visualStateBias: List<String> = emptyList(),
    preferredHomeModules: List<String> = emptyList()
) = AstrologyPlacementDefinition(
    id = id,
    planet = id.substringBefore("_"),
    sign = id.substringAfter("_"),
    title = text(),
    shortSummary = text(),
    coreTraits = coreTraits,
    shadowTraits = shadowTraits,
    growthTraits = growthTraits,
    traitWeights = traitWeights,
    areaWeights = areaWeights,
    dimensionProfile = dimensionProfile,
    summary = text("Summary $id"),
    aestheticTags = aestheticTags,
    reflectionTags = reflectionTags,
    visualStateBias = visualStateBias,
    preferredHomeModules = preferredHomeModules
)

internal class FakeNyraContentRepository(
    private val traits: Map<String, TraitDefinition> = emptyMap(),
    private val areas: Map<String, LifeAreaDefinition> = emptyMap(),
    private val astrology: Map<String, AstrologyPlacementDefinition> = emptyMap(),
    private val combos: Map<String, List<AreaComboRule>> = emptyMap(),
    private val archetypes: List<ArchetypeDefinition> = emptyList()
) : NyraContentRepository {
    override suspend fun loadContent() = error("Not used in tests")
    override fun getContentState() = NyraFeatureState(
        contentReady = true,
        mbtiEnabled = true,
        astrologyEnabled = true,
        reflectionEnabled = true,
        archetypesEnabled = true,
        validationErrors = emptyList()
    )
    override fun getTrait(id: String): TraitDefinition? = traits[id]
    override fun getArea(id: String): LifeAreaDefinition? = areas[id]
    override fun getMbtiResult(type: String): MbtiResultDefinition? = null
    override fun getAstrologyPlacement(planet: String, sign: String): AstrologyPlacementDefinition? =
        astrology["${planet.lowercase()}_${sign.lowercase()}"]
    override fun getCombosForArea(areaId: String): List<AreaComboRule> = combos[areaId].orEmpty()
    override fun getSingleTraitInterpretations(
        areaId: String,
        traitIds: Collection<String>
    ): List<SingleTraitInterpretation> = emptyList()
    override fun getPromptsByTags(tags: Collection<String>): List<ReflectionPrompt> = emptyList()
    override fun getArchetype(id: String): ArchetypeDefinition? = archetypes.firstOrNull { it.id == id }
    override fun getArchetypes(): List<ArchetypeDefinition> = archetypes
}
