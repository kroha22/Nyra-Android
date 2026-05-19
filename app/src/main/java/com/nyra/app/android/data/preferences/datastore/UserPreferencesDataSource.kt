package com.nyra.app.android.data.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.nyra.app.android.core.model.ReflectionRhythm
import com.nyra.app.android.core.model.ToneStyle
import com.nyra.app.android.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val TONE_STYLE = stringPreferencesKey("tone_style")
        val DEFAULT_RHYTHM = stringPreferencesKey("default_rhythm")
        val ATMOSPHERE_INTENSITY = intPreferencesKey("atmosphere_intensity")
        val MOTION_ENABLED = booleanPreferencesKey("motion_enabled")
        val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            toneStyle = ToneStyle.valueOf(
                preferences[PreferencesKeys.TONE_STYLE] ?: ToneStyle.Soft.name
            ),
            defaultRhythm = ReflectionRhythm.valueOf(
                preferences[PreferencesKeys.DEFAULT_RHYTHM] ?: ReflectionRhythm.Evening.name
            ),
            atmosphereIntensity = preferences[PreferencesKeys.ATMOSPHERE_INTENSITY] ?: 70,
            motionEnabled = preferences[PreferencesKeys.MOTION_ENABLED] ?: true,
            hapticsEnabled = preferences[PreferencesKeys.HAPTICS_ENABLED] ?: true,
            notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: false,
            onboardingCompleted = preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        )
    }

    suspend fun updateToneStyle(toneStyle: ToneStyle) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TONE_STYLE] = toneStyle.name
        }
    }

    suspend fun updateDefaultRhythm(rhythm: ReflectionRhythm) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_RHYTHM] = rhythm.name
        }
    }

    suspend fun updateAtmosphereIntensity(intensity: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ATMOSPHERE_INTENSITY] = intensity
        }
    }

    suspend fun updateMotionEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MOTION_ENABLED] = enabled
        }
    }

    suspend fun updateHapticsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTICS_ENABLED] = enabled
        }
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun updateOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun saveAllPreferences(prefs: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TONE_STYLE] = prefs.toneStyle.name
            preferences[PreferencesKeys.DEFAULT_RHYTHM] = prefs.defaultRhythm.name
            preferences[PreferencesKeys.ATMOSPHERE_INTENSITY] = prefs.atmosphereIntensity
            preferences[PreferencesKeys.MOTION_ENABLED] = prefs.motionEnabled
            preferences[PreferencesKeys.HAPTICS_ENABLED] = prefs.hapticsEnabled
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = prefs.notificationsEnabled
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = prefs.onboardingCompleted
        }
    }
}