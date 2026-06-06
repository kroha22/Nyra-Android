package com.nyra.app.android.feature.home

import com.nyra.app.android.core.model.PresenceState
import com.nyra.app.android.feature.home.model.AtmosphereState
import com.nyra.app.android.feature.home.model.HomeCardUiModel

data class HomeUiState(
    val isLoading: Boolean = true,
    val greeting: String = "",
    val subtitle: String? = null,
    val atmosphere: AtmosphereState = AtmosphereState.EVENING,
    val presenceState: PresenceState? = null,
    val cards: List<HomeCardUiModel> = emptyList(),
    val hasCheckedInToday: Boolean = false
)
