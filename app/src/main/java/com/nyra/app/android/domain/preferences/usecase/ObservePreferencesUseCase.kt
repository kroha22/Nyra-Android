package com.nyra.app.android.domain.preferences.usecase

import com.nyra.app.android.core.model.UserPreferences
import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePreferencesUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    operator fun invoke(): Flow<UserPreferences> = repository.observeUserPreferences()
}