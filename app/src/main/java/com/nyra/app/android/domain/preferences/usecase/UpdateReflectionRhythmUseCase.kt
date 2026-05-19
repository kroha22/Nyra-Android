package com.nyra.app.android.domain.preferences.usecase

import com.nyra.app.android.core.model.ReflectionRhythm
import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import javax.inject.Inject

class UpdateReflectionRhythmUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke(rhythm: ReflectionRhythm) {
        repository.updateReflectionRhythm(rhythm)
    }
}