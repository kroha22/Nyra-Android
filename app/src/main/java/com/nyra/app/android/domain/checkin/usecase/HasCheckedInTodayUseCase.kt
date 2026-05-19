package com.nyra.app.android.domain.checkin.usecase

import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class HasCheckedInTodayUseCase @Inject constructor(
    private val repository: CheckInRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.observeCheckInsByDay(LocalDate.now()).map { it.isNotEmpty() }
    }
}