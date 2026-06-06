package com.nyra.app.android.domain.home

import com.nyra.app.android.domain.home.model.HomeState
import com.nyra.app.android.domain.presence.ResolvePresenceStateUseCase
import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import com.nyra.app.android.domain.gentleaction.usecase.ResolveGentleActionsUseCase
import com.nyra.app.android.domain.settings.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

class ObserveHomeStateUseCase @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val preferencesRepository: PreferencesRepository,
    private val timeOfDayResolver: TimeOfDayResolver,
    private val resolvePresenceState: ResolvePresenceStateUseCase,
    private val resolveHomeCards: ResolveHomeCardsUseCase,
    private val resolveGentleActions: ResolveGentleActionsUseCase,
    private val getGreeting: GetGreetingUseCase
) {
    operator fun invoke(): Flow<HomeState> {
        val today = LocalDate.now()

        return combine(
            checkInRepository.observeLatestCheckIn(),
            checkInRepository.observeCheckInsByDay(today),
            preferencesRepository.preferences
        ) { latestCheckIn, todayCheckIns, preferences ->

            val timeOfDay = timeOfDayResolver.resolve()
            val hasCheckedInToday = todayCheckIns.isNotEmpty()

            val presenceState = resolvePresenceState(
                lastCheckIn = latestCheckIn,
                timeOfDay = timeOfDay
            )

            val cards = resolveHomeCards(
                lastCheckIn = latestCheckIn,
                hasCheckedInToday = hasCheckedInToday,
                timeOfDay = timeOfDay
            )

            val gentleActions = resolveGentleActions(
                lastCheckIn = latestCheckIn,
                timeOfDay = timeOfDay,
                preferences = preferences
            )

            val greeting = getGreeting(
                lastCheckIn = latestCheckIn,
                timeOfDay = timeOfDay
            )

            HomeState(
                greeting = greeting.first,
                subtitle = greeting.second,
                presenceState = presenceState,
                cards = cards,
                gentleActions = gentleActions,
                lastCheckIn = latestCheckIn,
                hasCheckedInToday = hasCheckedInToday,
                timeOfDay = timeOfDay
            )
        }
    }
}
