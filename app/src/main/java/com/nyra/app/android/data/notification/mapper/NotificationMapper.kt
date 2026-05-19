package com.nyra.app.android.data.notification.mapper

import com.nyra.app.android.core.database.entity.NotificationScheduleEntity
import com.nyra.app.android.core.model.NotificationSchedule

fun NotificationScheduleEntity.toDomain(): NotificationSchedule = NotificationSchedule(
    id = id,
    type = type,
    enabled = enabled,
    hour = hour,
    minute = minute,
    daysOfWeek = daysOfWeek,
    messageTemplateId = messageTemplateId,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun NotificationSchedule.toEntity(): NotificationScheduleEntity = NotificationScheduleEntity(
    id = id,
    type = type,
    enabled = enabled,
    hour = hour,
    minute = minute,
    daysOfWeek = daysOfWeek,
    messageTemplateId = messageTemplateId,
    createdAt = createdAt,
    updatedAt = updatedAt
)