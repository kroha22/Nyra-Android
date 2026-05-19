package com.nyra.app.android.domain.notification.repository

import com.nyra.app.android.core.model.NotificationSchedule
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun observeSchedules(): Flow<List<NotificationSchedule>>
    suspend fun syncSchedules(schedules: List<NotificationSchedule>)
    suspend fun updateScheduleEnabled(id: String, enabled: Boolean)
}