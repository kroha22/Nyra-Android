package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.NotificationType
import java.time.LocalDateTime

@Entity(tableName = "notification_schedules")
data class NotificationScheduleEntity(
    @PrimaryKey val id: String,
    val type: NotificationType,
    val enabled: Boolean,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: List<Int>,
    val messageTemplateId: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)