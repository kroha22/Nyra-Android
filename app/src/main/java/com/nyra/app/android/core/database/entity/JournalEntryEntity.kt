package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.MoodCode
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "journal_entries")
data class JournalEntryEntity(
    @PrimaryKey val id: String,
    val title: String?,
    val body: String,
    val linkedCheckInId: String?,
    val moodCode: MoodCode?,
    val tags: List<String>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val localDay: LocalDate,
    val isArchived: Boolean,
    val isDeleted: Boolean
)