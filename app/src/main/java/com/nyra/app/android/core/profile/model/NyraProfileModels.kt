package com.nyra.app.android.core.profile.model

import com.nyra.app.android.core.astrology.model.PlanetPlacement

data class NyraTraitScore(
    val traitId: String,
    val score: Double
)

data class NyraAreaScore(
    val areaId: String,
    val score: Double
)

data class NyraDimensionProfile(
    val dimensions: Map<String, Double>
)

data class NyraActiveCombo(
    val comboId: String,
    val score: Double
)

data class NyraActiveArchetype(
    val archetypeId: String,
    val score: Double
)

enum class NyraVisualState {
    WARM_HORIZON,
    NIGHT_REFLECTION,
    DEEP_WATER,
    GOLD_WARMTH,
    SOFT_DAWN,
    CRYSTAL_CLARITY,
    HORIZON_MOTION,
    VELVET_INTIMACY
}

data class NyraUserProfile(
    val placements: List<PlanetPlacement>,
    val topTraits: List<NyraTraitScore>,
    val shadowTraits: List<NyraTraitScore>,
    val growthTraits: List<NyraTraitScore>,
    val dimensions: NyraDimensionProfile,
    val areas: List<NyraAreaScore>,
    val activeCombos: List<NyraActiveCombo>,
    val activeArchetypes: List<NyraActiveArchetype>,
    val visualStates: List<NyraVisualState>,
    val aestheticTags: List<String>,
    val reflectionTags: List<String>,
    val preferredHomeModules: List<String>,
    val summaries: List<String>,
    val shadows: List<String>,
    val growths: List<String>,
    val warnings: List<String>
) {
    companion object {
        /**
         * Sane fallback when the user hasn't completed onboarding (no birth data, no MBTI).
         *
         * With empty `visualStates`, [com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolverImpl]
         * falls back to `WARM_HORIZON`. With empty `preferredHomeModules`, the screen composition
         * resolver falls back to the visual state's default module list.
         *
         * The adaptive home screen will render — calmly — until a real profile arrives.
         */
        fun empty(): NyraUserProfile = NyraUserProfile(
            placements = emptyList(),
            topTraits = emptyList(),
            shadowTraits = emptyList(),
            growthTraits = emptyList(),
            dimensions = NyraDimensionProfile(emptyMap()),
            areas = emptyList(),
            activeCombos = emptyList(),
            activeArchetypes = emptyList(),
            visualStates = emptyList(),
            aestheticTags = emptyList(),
            reflectionTags = emptyList(),
            preferredHomeModules = emptyList(),
            summaries = emptyList(),
            shadows = emptyList(),
            growths = emptyList(),
            warnings = emptyList()
        )
    }
}
