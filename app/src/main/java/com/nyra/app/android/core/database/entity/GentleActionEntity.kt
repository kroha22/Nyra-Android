package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.GentleActionCategory
import com.nyra.app.android.core.model.GentleActionIntensity
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.model.TimeOfDay

@Entity(tableName = "gentle_actions")
data class GentleActionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String?,
    val durationSeconds: Int?,
    val category: GentleActionCategory,
    val suitableMoodCodes: List<MoodCode>,
    val unsuitableMoodCodes: List<MoodCode>,
    val timeOfDay: List<TimeOfDay>,
    val intensity: GentleActionIntensity,
    val isActive: Boolean
)