package com.nyra.app.android.data.preferences.repository

import com.nyra.app.android.core.model.ReflectionRhythm
import com.nyra.app.android.core.model.ToneStyle
import com.nyra.app.android.core.model.UserPreferences
import com.nyra.app.android.data.preferences.datastore.UserPreferencesDataSource
import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : PreferenceRepository {

    override fun observeUserPreferences(): Flow<UserPreferences> {
        return dataSource.userPreferences
    }

    override suspend fun updateToneStyle(toneStyle: ToneStyle) {
        dataSource.updateToneStyle(toneStyle)
    }

    override suspend fun updateReflectionRhythm(rhythm: ReflectionRhythm) {
        dataSource.updateDefaultRhythm(rhythm)
    }

    override suspend fun updateAtmosphereIntensity(intensity: Int) {
        dataSource.updateAtmosphereIntensity(intensity)
    }

    override suspend fun updateMotionEnabled(enabled: Boolean) {
        dataSource.updateMotionEnabled(enabled)
    }

    override suspend fun updateHapticsEnabled(enabled: Boolean) {
        dataSource.updateHapticsEnabled(enabled)
    }

    override suspend fun updateNotificationsEnabled(enabled: Boolean) {
        dataSource.updateNotificationsEnabled(enabled)
    }

    override suspend fun updateOnboardingCompleted(completed: Boolean) {
        dataSource.updateOnboardingCompleted(completed)
    }
}