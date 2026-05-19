package com.nyra.app.android.data.preferences.mapper

import com.nyra.app.android.core.database.entity.UserPreferencesEntity
import com.nyra.app.android.core.model.UserPreferences

fun UserPreferencesEntity.toDomain(): UserPreferences = UserPreferences(
    toneStyle = toneStyle,
    defaultRhythm = defaultRhythm,
    atmosphereIntensity = atmosphereIntensity,
    motionEnabled = motionEnabled,
    hapticsEnabled = hapticsEnabled,
    notificationsEnabled = notificationsEnabled,
    onboardingCompleted = onboardingCompleted
)

fun UserPreferences.toEntity(): UserPreferencesEntity = UserPreferencesEntity(
    toneStyle = toneStyle,
    defaultRhythm = defaultRhythm,
    atmosphereIntensity = atmosphereIntensity,
    motionEnabled = motionEnabled,
    hapticsEnabled = hapticsEnabled,
    notificationsEnabled = notificationsEnabled,
    onboardingCompleted = onboardingCompleted
)