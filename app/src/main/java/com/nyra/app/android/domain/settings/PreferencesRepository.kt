package com.nyra.app.android.domain.settings

import com.nyra.app.android.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val preferences: Flow<UserPreferences>
}