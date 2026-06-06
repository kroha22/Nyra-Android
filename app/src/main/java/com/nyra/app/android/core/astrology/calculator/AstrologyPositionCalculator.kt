package com.nyra.app.android.core.astrology.calculator

import com.nyra.app.android.core.astrology.model.AstrologyCalculationResult
import com.nyra.app.android.core.astrology.model.BirthData

interface AstrologyPositionCalculator {
    fun calculateFivePlanetPlacements(
        birthData: BirthData
    ): AstrologyCalculationResult
}
