package com.nyra.app.android.domain.preferences.usecase

import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import javax.inject.Inject

class UpdateNotificationSettingsUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        repository.updateNotificationsEnabled(enabled)
    }
}