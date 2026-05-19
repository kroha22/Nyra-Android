package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.ReflectionRhythm
import com.nyra.app.android.core.model.ToneStyle

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 0, // Single row
    val toneStyle: ToneStyle,
    val defaultRhythm: ReflectionRhythm,
    val atmosphereIntensity: Int,
    val motionEnabled: Boolean,
    val hapticsEnabled: Boolean,
    val notificationsEnabled: Boolean,
    val onboardingCompleted: Boolean
)