package com.nyra.app.android.core.ui_state.model

import com.nyra.app.android.core.profile.model.NyraVisualState

data class NyraPalette(
    val backgroundPrimary: Long,
    val backgroundSecondary: Long,
    val surface: Long,
    val surfaceSecondary: Long,
    val primaryGlow: Long,
    val secondaryGlow: Long,
    val textPrimary: Long,
    val textSecondary: Long,
    val accent: Long
)

data class NyraBackgroundConfig(
    val backgroundAsset: String,
    val gradientAsset: String,
    val particleAsset: String,
    val blurStrength: Float,
    val glowIntensity: Float
)

data class NyraMotionProfile(
    val driftSpeed: Float,
    val orbBreathingSpeed: Float,
    val parallaxStrength: Float,
    val transitionDurationMs: Int,
    val particleDensity: Float
)

data class NyraCardStyle(
    val cornerRadius: Float,
    val blurAmount: Float,
    val borderOpacity: Float,
    val glowOpacity: Float,
    val verticalSpacing: Float
)

data class NyraOrbConfig(
    val orbStyle: String,
    val orbGlowColor: Long,
    val orbPulseSpeed: Float,
    val orbSizeMultiplier: Float
)

enum class NyraTextTone {
    CALM,
    REFLECTIVE,
    SUPPORTIVE,
    EXPRESSIVE,
    ANALYTICAL,
    EXPLORATORY,
    INTIMATE
}

data class NyraUiStateConfig(
    val primaryState: NyraVisualState,
    val secondaryState: NyraVisualState?,
    val palette: NyraPalette,
    val background: NyraBackgroundConfig,
    val motion: NyraMotionProfile,
    val cardStyle: NyraCardStyle,
    val orb: NyraOrbConfig,
    val textTone: NyraTextTone,
    val moduleOrder: List<String>
)

data class NyraUiStateDefinition(
    val state: NyraVisualState,
    val palette: NyraPalette,
    val background: NyraBackgroundConfig,
    val motion: NyraMotionProfile,
    val cardStyle: NyraCardStyle,
    val orb: NyraOrbConfig,
    val textTone: NyraTextTone,
    val defaultModules: List<String>
)
