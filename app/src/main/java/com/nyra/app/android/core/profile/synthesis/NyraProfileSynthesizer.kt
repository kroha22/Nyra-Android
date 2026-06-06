package com.nyra.app.android.core.profile.synthesis

import com.nyra.app.android.core.astrology.model.AstrologyCalculationResult
import com.nyra.app.android.core.profile.model.NyraUserProfile

interface NyraProfileSynthesizer {
    fun synthesizeFromAstrology(
        calculationResult: AstrologyCalculationResult
    ): NyraUserProfile

    fun synthesize(
        calculationResult: AstrologyCalculationResult,
        mbtiType: String? = null
    ): NyraUserProfile
}
