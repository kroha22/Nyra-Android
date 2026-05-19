package com.nyra.app.android.core.rules

import com.nyra.app.android.core.model.*
import javax.inject.Inject
import javax.inject.Singleton

data class NyraHomeState(
    val timeOfDay: TimeOfDay,
    val presenceState: PresenceState,
    val homeCards: List<CardTemplate>,
    val gentleActions: List<GentleAction>
)

@Singleton
class RuleEngine @Inject constructor(
    private val timeOfDayResolver: TimeOfDayResolver,
    private val presenceStateResolver: PresenceStateResolver,
    private val homeCardResolver: HomeCardResolver,
    private val gentleActionResolver: GentleActionResolver
) {
    fun resolveHomeState(
        lastCheckIn: CheckInEntry?,
        preferences: UserPreferences
    ): NyraHomeState {
        val timeOfDay = timeOfDayResolver.resolve()
        
        return NyraHomeState(
            timeOfDay = timeOfDay,
            presenceState = presenceStateResolver.resolve(lastCheckIn),
            homeCards = homeCardResolver.resolve(lastCheckIn, timeOfDay, preferences),
            gentleActions = gentleActionResolver.resolve(lastCheckIn, timeOfDay, preferences)
        )
    }
}