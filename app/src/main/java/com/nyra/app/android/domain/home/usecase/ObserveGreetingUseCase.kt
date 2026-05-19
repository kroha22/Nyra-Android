package com.nyra.app.android.domain.home.usecase

import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import com.nyra.app.android.domain.system.usecase.ObserveTimeOfDayUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveGreetingUseCase @Inject constructor(
    private val observeTimeOfDayUseCase: ObserveTimeOfDayUseCase,
    private val checkInRepository: CheckInRepository,
    private val getGreetingUseCase: GetGreetingUseCase
) {
    operator fun invoke(): Flow<String> {
        return combine(
            observeTimeOfDayUseCase(),
            checkInRepository.observeLatestCheckIn()
        ) { timeOfDay, lastCheckIn ->
            getGreetingUseCase(timeOfDay, lastCheckIn?.moodCode)
        }
    }
}