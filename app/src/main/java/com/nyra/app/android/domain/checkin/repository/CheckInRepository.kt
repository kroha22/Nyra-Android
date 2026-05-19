package com.nyra.app.android.domain.checkin.repository

import com.nyra.app.android.core.model.CheckInEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CheckInRepository {
    fun observeCheckIns(): Flow<List<CheckInEntry>>
    fun observeLatestCheckIn(): Flow<CheckInEntry?>
    fun observeCheckInsByDay(date: LocalDate): Flow<List<CheckInEntry>>
    suspend fun saveCheckIn(entry: CheckInEntry)
}