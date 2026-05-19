package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.model.PresenceCode

@Entity(tableName = "mood_states")
data class MoodStateEntity(
    @PrimaryKey val id: String,
    val code: MoodCode,
    val title: String,
    val description: String?,
    val valence: Int,
    val energy: Int,
    val intensity: Int,
    val presenceCode: PresenceCode,
    val isActive: Boolean
)