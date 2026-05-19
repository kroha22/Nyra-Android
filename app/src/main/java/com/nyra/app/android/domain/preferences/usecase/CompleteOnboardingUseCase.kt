package com.nyra.app.android.domain.preferences.usecase

import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import javax.inject.Inject

class CompleteOnboardingUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke() {
        repository.updateOnboardingCompleted(true)
    }
}