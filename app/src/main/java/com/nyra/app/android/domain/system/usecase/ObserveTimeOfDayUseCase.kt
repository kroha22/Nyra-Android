package com.nyra.app.android.domain.system.usecase

import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.rules.TimeOfDayResolver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveTimeOfDayUseCase @Inject constructor(
    private val timeOfDayResolver: TimeOfDayResolver
) {
    operator fun invoke(): Flow<TimeOfDay> = flow {
        while (true) {
            emit(timeOfDayResolver.resolve())
            // Check every minute to react to time changes
            delay(60_000)
        }
    }
}