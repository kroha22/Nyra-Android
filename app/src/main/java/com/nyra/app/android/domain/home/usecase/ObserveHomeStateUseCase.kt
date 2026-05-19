package com.nyra.app.android.domain.home.usecase

import com.nyra.app.android.core.model.*
import com.nyra.app.android.core.rules.RuleEngine
import com.nyra.app.android.core.rules.TimeOfDayResolver
import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import com.nyra.app.android.domain.home.model.HomeState
import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveHomeStateUseCase @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val preferenceRepository: PreferenceRepository,
    private val timeOfDayResolver: TimeOfDayResolver,
    private val ruleEngine: RuleEngine,
    private val getGreetingUseCase: GetGreetingUseCase
) {
    operator fun invoke(): Flow<HomeState> {
        return combine(
            checkInRepository.observeLatestCheckIn(),
            preferenceRepository.observeUserPreferences()
        ) { lastCheckIn, preferences ->
            val homeRuleState = ruleEngine.resolveHomeState(lastCheckIn, preferences)
            
            HomeState(
                greeting = getGreetingUseCase(homeRuleState.timeOfDay, lastCheckIn?.moodCode),
                timeOfDay = homeRuleState.timeOfDay,
                presenceState = homeRuleState.presenceState,
                homeCards = homeRuleState.homeCards,
                gentleActions = homeRuleState.gentleActions
            )
        }
    }
}