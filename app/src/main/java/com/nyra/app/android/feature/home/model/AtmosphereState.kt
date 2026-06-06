package com.nyra.app.android.feature.home.model

import androidx.compose.ui.graphics.Color
import com.nyra.app.android.core.designsystem.theme.*

enum class AtmosphereState(
    val gradientColors: List<Color>,
    val blurRadius: Float = 40f,
    val glowIntensity: Float = 0.6f,
    val motionSpeed: Int = 5000,
    val textContrast: Float = 1f
) {
    MORNING(
        gradientColors = listOf(NyraMorningTop, NyraMorningMiddle, NyraMorningBottom),
        blurRadius = 30f, glowIntensity = 0.5f, motionSpeed = 4000, textContrast = 1f
    ),
    AFTERNOON(
        gradientColors = listOf(NyraAfternoonTop, NyraAfternoonMiddle, NyraAfternoonBottom),
        blurRadius = 40f, glowIntensity = 0.6f, motionSpeed = 5000, textContrast = 1f
    ),
    EVENING(
        gradientColors = listOf(NyraEveningTop, NyraEveningMiddle, NyraEveningBottom),
        blurRadius = 80f, glowIntensity = 0.8f, motionSpeed = 7000, textContrast = 0.9f
    ),
    NIGHT(
        gradientColors = listOf(NyraQuietTop, NyraQuietMiddle, NyraQuietBottom),
        blurRadius = 20f, glowIntensity = 0.3f, motionSpeed = 9000, textContrast = 0.8f
    ),
    CALM(
        gradientColors = listOf(NyraCalmTop, NyraCalmMiddle, NyraCalmBottom),
        blurRadius = 40f, glowIntensity = 0.6f, motionSpeed = 5000, textContrast = 1f
    ),
    TIRED(
        gradientColors = listOf(NyraTiredTop, NyraTiredMiddle, NyraTiredBottom),
        blurRadius = 100f, glowIntensity = 0.7f, motionSpeed = 8000, textContrast = 0.7f
    ),
    RESTLESS(
        gradientColors = listOf(NyraRestlessTop, NyraRestlessMiddle, NyraRestlessBottom),
        blurRadius = 50f, glowIntensity = 0.9f, motionSpeed = 3000, textContrast = 1f
    ),
    GROUNDED(
        gradientColors = listOf(NyraGroundedTop, NyraGroundedMiddle, NyraGroundedBottom),
        blurRadius = 40f, glowIntensity = 0.4f, motionSpeed = 6000, textContrast = 1f
    )
}
