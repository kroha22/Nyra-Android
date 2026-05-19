package com.nyra.app.android.core.model

import java.time.LocalDate
import java.time.LocalDateTime

data class JournalEntry(
    val id: String,
    val title: String? = null,
    val body: String,
    val linkedCheckInId: String? = null,
    val moodCode: MoodCode? = null,
    val tags: List<String> = emptyList(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val localDay: LocalDate,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false
)