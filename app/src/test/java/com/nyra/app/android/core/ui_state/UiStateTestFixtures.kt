package com.nyra.app.android.core.ui_state

import com.nyra.app.android.core.astrology.model.PlanetPlacement
import com.nyra.app.android.core.profile.model.NyraActiveArchetype
import com.nyra.app.android.core.profile.model.NyraActiveCombo
import com.nyra.app.android.core.profile.model.NyraAreaScore
import com.nyra.app.android.core.profile.model.NyraDimensionProfile
import com.nyra.app.android.core.profile.model.NyraTraitScore
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.profile.model.NyraVisualState

internal fun profile(
    visualStates: List<NyraVisualState> = listOf(NyraVisualState.WARM_HORIZON),
    preferredHomeModules: List<String> = emptyList(),
    areas: List<NyraAreaScore> = emptyList(),
    reflectionTags: List<String> = emptyList()
) = NyraUserProfile(
    placements = emptyList<PlanetPlacement>(),
    topTraits = emptyList<NyraTraitScore>(),
    shadowTraits = emptyList<NyraTraitScore>(),
    growthTraits = emptyList<NyraTraitScore>(),
    dimensions = NyraDimensionProfile(emptyMap()),
    areas = areas,
    activeCombos = emptyList<NyraActiveCombo>(),
    activeArchetypes = emptyList<NyraActiveArchetype>(),
    visualStates = visualStates,
    aestheticTags = emptyList(),
    reflectionTags = reflectionTags,
    preferredHomeModules = preferredHomeModules,
    summaries = emptyList(),
    shadows = emptyList(),
    growths = emptyList(),
    warnings = emptyList()
)
