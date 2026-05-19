package com.nyra.app.android.domain.home.usecase

import com.nyra.app.android.core.model.CardTemplate
import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.model.UserPreferences
import com.nyra.app.android.core.rules.HomeCardResolver
import javax.inject.Inject

class ResolveHomeCardsUseCase @Inject constructor(
    private val homeCardResolver: HomeCardResolver
) {
    operator fun invoke(
        lastCheckIn: CheckInEntry?,
        timeOfDay: TimeOfDay,
        preferences: UserPreferences
    ): List<CardTemplate> {
        return homeCardResolver.resolve(lastCheckIn, timeOfDay, preferences)
    }
}