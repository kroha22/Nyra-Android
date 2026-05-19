package com.nyra.app.android.data.notification.repository

import com.nyra.app.android.core.database.dao.NotificationDao
import com.nyra.app.android.core.model.NotificationSchedule
import com.nyra.app.android.data.notification.mapper.toDomain
import com.nyra.app.android.data.notification.mapper.toEntity
import com.nyra.app.android.domain.notification.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dao: NotificationDao
) : NotificationRepository {

    override fun observeSchedules(): Flow<List<NotificationSchedule>> {
        return dao.getSchedules().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncSchedules(schedules: List<NotificationSchedule>) {
        dao.insertSchedules(schedules.map { it.toEntity() })
    }

    override suspend fun updateScheduleEnabled(id: String, enabled: Boolean) {
        dao.updateScheduleEnabled(id, enabled)
    }
}