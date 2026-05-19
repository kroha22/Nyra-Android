package com.nyra.app.android.domain.gentleaction.usecase

import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.GentleAction
import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.model.UserPreferences
import com.nyra.app.android.core.rules.GentleActionResolver
import javax.inject.Inject

class ResolveGentleActionsUseCase @Inject constructor(
    private val gentleActionResolver: GentleActionResolver
) {
    operator fun invoke(
        lastCheckIn: CheckInEntry?,
        timeOfDay: TimeOfDay,
        preferences: UserPreferences
    ): List<GentleAction> {
        return gentleActionResolver.resolve(lastCheckIn, timeOfDay, preferences)
    }
}