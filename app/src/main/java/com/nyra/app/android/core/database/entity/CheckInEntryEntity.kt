package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.CheckInSource
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.model.TimeOfDay
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "check_in_entries")
data class CheckInEntryEntity(
    @PrimaryKey val id: String,
    val moodCode: MoodCode,
    val energyLevel: Int,
    val intensityLevel: Int,
    val note: String?,
    val tags: List<String>,
    val createdAt: LocalDateTime,
    val localDay: LocalDate,
    val timeOfDay: TimeOfDay,
    val source: CheckInSource
)