package com.nyra.app.android.domain.home.model

import com.nyra.app.android.core.model.CardTemplate
import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.PresenceState
import com.nyra.app.android.core.model.TimeOfDay

data class HomeState(
    val greeting: String,
    val subtitle: String?,
    val presenceState: PresenceState,
    val cards: List<CardTemplate>,
    val lastCheckIn: CheckInEntry?,
    val hasCheckedInToday: Boolean,
    val timeOfDay: TimeOfDay
)