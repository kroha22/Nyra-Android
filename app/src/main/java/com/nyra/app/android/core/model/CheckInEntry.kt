package com.nyra.app.android.core.model

import java.time.LocalDate
import java.time.LocalDateTime

data class CheckInEntry(
    val id: String,
    val moodCode: MoodCode,
    val energyLevel: Int,
    val intensityLevel: Int,
    val note: String? = null,
    val tags: List<String> = emptyList(),
    val createdAt: LocalDateTime,
    val localDay: LocalDate,
    val timeOfDay: TimeOfDay,
    val source: CheckInSource
)

enum class TimeOfDay {
    Morning,
    Afternoon,
    Evening,
    Night
}

enum class CheckInSource {
    Manual,
    Prompt,
    PauseScreen
}