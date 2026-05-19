package com.nyra.app.android.core.model

import java.time.LocalDateTime

data class NotificationSchedule(
    val id: String,
    val type: NotificationType,
    val enabled: Boolean,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: List<Int>,
    val messageTemplateId: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class NotificationType {
    CheckInReminder,
    EveningReflection,
    GentlePause
}