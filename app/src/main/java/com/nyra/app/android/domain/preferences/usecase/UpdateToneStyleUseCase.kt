package com.nyra.app.android.domain.preferences.usecase

import com.nyra.app.android.core.model.ToneStyle
import com.nyra.app.android.domain.preferences.repository.PreferenceRepository
import javax.inject.Inject

class UpdateToneStyleUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke(toneStyle: ToneStyle) {
        repository.updateToneStyle(toneStyle)
    }
}