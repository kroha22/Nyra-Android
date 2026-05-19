package com.nyra.app.android.feature.home

import com.nyra.app.android.core.model.CardTemplate
import com.nyra.app.android.core.model.PresenceState

data class HomeUiState(
    val isLoading: Boolean = true,
    val greeting: String = "",
    val subtitle: String? = null,
    val presenceState: PresenceState? = null,
    val cards: List<CardTemplate> = emptyList(),
    val hasCheckedInToday: Boolean = false
)