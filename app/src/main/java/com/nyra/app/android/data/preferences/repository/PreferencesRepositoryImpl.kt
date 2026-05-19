package com.nyra.app.android.data.preferences.repository

import com.nyra.app.android.core.model.UserPreferences
import com.nyra.app.android.data.preferences.datastore.UserPreferencesDataSource
import com.nyra.app.android.domain.settings.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : PreferencesRepository {

    override val preferences: Flow<UserPreferences>
        get() = dataSource.userPreferences
}