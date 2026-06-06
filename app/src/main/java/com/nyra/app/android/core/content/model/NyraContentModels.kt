package com.nyra.app.android.core.content.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraitDefinition(
    val id: String,
    val category: String,
    val subcategory: String? = null,
    val dimensions: DimensionProfile = emptyMap(),
    @SerialName("related_traits")
    val relatedTraits: List<String> = emptyList(),
    @SerialName("opposite_traits")
    val oppositeTraits: List<String> = emptyList(),
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    @SerialName("source_systems")
    val sourceSystems: List<String> = emptyList(),
    val premium: PremiumFlag = false
)

@Serializable
data class LifeAreaDefinition(
    val id: String,
    val category: String,
    val tier: String? = null,
    val priority: Int = 0,
    val icon: String? = null,
    @SerialName("primary_traits")
    val primaryTraits: List<String> = emptyList(),
    @SerialName("dimension_bias")
    val dimensionBias: DimensionProfile = emptyMap(),
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    val premium: PremiumFlag = false
)

@Serializable
data class AreaComboRule(
    val id: String,
    val area: String,
    val tier: String? = null,
    @SerialName("required_traits")
    val requiredTraits: List<String> = emptyList(),
    @SerialName("minimum_match")
    val minimumMatch: Int = 1,
    val weight: Double = 0.0,
    @SerialName("fallback_priority")
    val fallbackPriority: Int = 0,
    @SerialName("dominant_dimensions")
    val dominantDimensions: List<String> = emptyList(),
    @SerialName("emotional_tone")
    val emotionalTone: String? = null,
    val title: LocalizedText,
    @SerialName("short_summary")
    val shortSummary: LocalizedText,
    val summary: LocalizedText,
    val shadow: LocalizedText? = null,
    val growth: LocalizedText? = null,
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    val premium: PremiumFlag = false
)

@Serializable
data class MbtiResultDefinition(
    val id: String,
    val code: String,
    @SerialName("technique_id")
    val techniqueId: String? = null,
    @SerialName("source_weight")
    val sourceWeight: Double = 1.0,
    val confidence: Double = 1.0,
    val title: LocalizedText,
    @SerialName("short_summary")
    val shortSummary: LocalizedText,
    @SerialName("core_traits")
    val coreTraits: List<String> = emptyList(),
    @SerialName("shadow_traits")
    val shadowTraits: List<String> = emptyList(),
    @SerialName("growth_traits")
    val growthTraits: List<String> = emptyList(),
    @SerialName("area_weights")
    val areaWeights: AreaWeights = emptyMap(),
    @SerialName("trait_weights")
    val traitWeights: TraitWeights = emptyMap(),
    @SerialName("dimension_profile")
    val dimensionProfile: DimensionProfile = emptyMap(),
    val summary: LocalizedText,
    val shadow: LocalizedText? = null,
    val growth: LocalizedText? = null,
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    val premium: PremiumFlag = false
)

@Serializable
data class AstrologyPlacementDefinition(
    val id: String,
    val planet: String,
    val sign: String,
    @SerialName("technique_id")
    val techniqueId: String? = null,
    @SerialName("source_weight")
    val sourceWeight: Double = 1.0,
    val confidence: Double = 1.0,
    val title: LocalizedText,
    @SerialName("short_summary")
    val shortSummary: LocalizedText,
    @SerialName("core_traits")
    val coreTraits: List<String> = emptyList(),
    @SerialName("shadow_traits")
    val shadowTraits: List<String> = emptyList(),
    @SerialName("growth_traits")
    val growthTraits: List<String> = emptyList(),
    @SerialName("area_weights")
    val areaWeights: AreaWeights = emptyMap(),
    @SerialName("trait_weights")
    val traitWeights: TraitWeights = emptyMap(),
    @SerialName("dimension_profile")
    val dimensionProfile: DimensionProfile = emptyMap(),
    val summary: LocalizedText,
    val shadow: LocalizedText? = null,
    val growth: LocalizedText? = null,
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    val premium: PremiumFlag = false
)

@Serializable
data class SingleTraitInterpretation(
    val id: String,
    val trait: String,
    val area: String,
    val priority: Int,
    val title: LocalizedText,
    @SerialName("short_summary")
    val shortSummary: LocalizedText,
    val summary: LocalizedText,
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    val premium: PremiumFlag = false
)

@Serializable
data class ReflectionPrompt(
    val id: String,
    val tags: List<String> = emptyList(),
    @SerialName("life_areas")
    val lifeAreas: List<String> = emptyList(),
    val depth: String,
    @SerialName("emotional_tone")
    val emotionalTone: String,
    val prompt: LocalizedText,
    @SerialName("followup_prompt")
    val followupPrompt: LocalizedText? = null,
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    @SerialName("time_bias")
    val timeBias: List<String> = emptyList(),
    val premium: PremiumFlag = false
)

@Serializable
data class ArchetypeDefinition(
    val id: String,
    val priority: Int,
    val title: LocalizedText,
    @SerialName("short_summary")
    val shortSummary: LocalizedText,
    @SerialName("required_traits")
    val requiredTraits: List<String> = emptyList(),
    @SerialName("minimum_match")
    val minimumMatch: Int = 1,
    @SerialName("dimension_bias")
    val dimensionBias: DimensionProfile = emptyMap(),
    @SerialName("area_bias")
    val areaBias: AreaWeights = emptyMap(),
    val summary: LocalizedText,
    val shadow: LocalizedText? = null,
    val growth: LocalizedText? = null,
    @SerialName("aesthetic_tags")
    val aestheticTags: List<String> = emptyList(),
    @SerialName("reflection_tags")
    val reflectionTags: List<String> = emptyList(),
    @SerialName("visual_state_bias")
    val visualStateBias: List<String> = emptyList(),
    @SerialName("preferred_home_modules")
    val preferredHomeModules: List<String> = emptyList(),
    val rarity: Double? = null,
    val premium: PremiumFlag = false
)
