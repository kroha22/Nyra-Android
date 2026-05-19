package com.nyra.app.android.domain.preferences.repository

import com.nyra.app.android.core.model.ReflectionRhythm
import com.nyra.app.android.core.model.ToneStyle
import com.nyra.app.android.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun observeUserPreferences(): Flow<UserPreferences>
    suspend fun updateToneStyle(toneStyle: ToneStyle)
    suspend fun updateReflectionRhythm(rhythm: ReflectionRhythm)
    suspend fun updateAtmosphereIntensity(intensity: Int)
    suspend fun updateMotionEnabled(enabled: Boolean)
    suspend fun updateHapticsEnabled(enabled: Boolean)
    suspend fun updateNotificationsEnabled(enabled: Boolean)
    suspend fun updateOnboardingCompleted(completed: Boolean)
}