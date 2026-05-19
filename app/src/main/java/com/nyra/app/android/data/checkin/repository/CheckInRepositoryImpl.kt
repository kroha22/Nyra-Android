package com.nyra.app.android.data.checkin.repository

import com.nyra.app.android.core.database.dao.CheckInDao
import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.data.checkin.mapper.toDomain
import com.nyra.app.android.data.checkin.mapper.toEntity
import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val dao: CheckInDao
) : CheckInRepository {

    override fun observeCheckIns(): Flow<List<CheckInEntry>> {
        return dao.getAllCheckIns().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeLatestCheckIn(): Flow<CheckInEntry?> {
        return dao.getLatestCheckIn().map { it?.toDomain() }
    }

    override fun observeCheckInsByDay(date: LocalDate): Flow<List<CheckInEntry>> {
        return dao.getCheckInsByDay(date).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveCheckIn(entry: CheckInEntry) {
        dao.insertCheckIn(entry.toEntity())
    }
}