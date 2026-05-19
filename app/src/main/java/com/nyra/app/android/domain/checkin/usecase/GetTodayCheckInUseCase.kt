package com.nyra.app.android.domain.checkin.usecase

import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetTodayCheckInUseCase @Inject constructor(
    private val repository: CheckInRepository
) {
    operator fun invoke(): Flow<List<CheckInEntry>> {
        return repository.observeCheckInsByDay(LocalDate.now())
    }
}