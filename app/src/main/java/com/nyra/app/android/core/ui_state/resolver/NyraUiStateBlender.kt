package com.nyra.app.android.core.ui_state.resolver

import com.nyra.app.android.core.ui_state.model.NyraBackgroundConfig
import com.nyra.app.android.core.ui_state.model.NyraCardStyle
import com.nyra.app.android.core.ui_state.model.NyraMotionProfile
import com.nyra.app.android.core.ui_state.model.NyraOrbConfig
import com.nyra.app.android.core.ui_state.model.NyraPalette
import com.nyra.app.android.core.ui_state.model.NyraUiStateDefinition
import javax.inject.Inject
import kotlin.math.roundToInt

class NyraUiStateBlender @Inject constructor() {

    fun blend(
        primary: NyraUiStateDefinition,
        secondary: NyraUiStateDefinition?,
        primaryRatio: Float = DEFAULT_PRIMARY_RATIO
    ): BlendedUiStateParts {
        if (secondary == null) {
            return BlendedUiStateParts(
                palette = primary.palette,
                background = primary.background,
                motion = primary.motion,
                cardStyle = primary.cardStyle,
                orb = primary.orb
            )
        }
        val ratio = primaryRatio.coerceIn(0f, 1f)
        val inverse = 1f - ratio
        return BlendedUiStateParts(
            palette = primary.palette.blend(secondary.palette, ratio),
            background = NyraBackgroundConfig(
                backgroundAsset = primary.background.backgroundAsset,
                gradientAsset = primary.background.gradientAsset,
                particleAsset = secondary.background.particleAsset,
                blurStrength = blendFloat(primary.background.blurStrength, secondary.background.blurStrength, ratio),
                glowIntensity = blendFloat(primary.background.glowIntensity, secondary.background.glowIntensity, ratio)
            ),
            motion = NyraMotionProfile(
                driftSpeed = blendFloat(primary.motion.driftSpeed, secondary.motion.driftSpeed, ratio),
                orbBreathingSpeed = blendFloat(
                    primary.motion.orbBreathingSpeed,
                    secondary.motion.orbBreathingSpeed,
                    ratio
                ),
                parallaxStrength = blendFloat(
                    primary.motion.parallaxStrength,
                    secondary.motion.parallaxStrength,
                    ratio
                ),
                transitionDurationMs = (
                    primary.motion.transitionDurationMs * ratio +
                        secondary.motion.transitionDurationMs * inverse
                    ).roundToInt(),
                particleDensity = blendFloat(primary.motion.particleDensity, secondary.motion.particleDensity, ratio)
            ),
            cardStyle = NyraCardStyle(
                cornerRadius = blendFloat(primary.cardStyle.cornerRadius, secondary.cardStyle.cornerRadius, ratio),
                blurAmount = blendFloat(primary.cardStyle.blurAmount, secondary.cardStyle.blurAmount, ratio),
                borderOpacity = blendFloat(primary.cardStyle.borderOpacity, secondary.cardStyle.borderOpacity, ratio),
                glowOpacity = blendFloat(primary.cardStyle.glowOpacity, secondary.cardStyle.glowOpacity, ratio),
                verticalSpacing = blendFloat(primary.cardStyle.verticalSpacing, secondary.cardStyle.verticalSpacing, ratio)
            ),
            orb = NyraOrbConfig(
                orbStyle = primary.orb.orbStyle,
                orbGlowColor = blendColor(primary.orb.orbGlowColor, secondary.orb.orbGlowColor, ratio),
                orbPulseSpeed = blendFloat(primary.orb.orbPulseSpeed, secondary.orb.orbPulseSpeed, ratio),
                orbSizeMultiplier = blendFloat(primary.orb.orbSizeMultiplier, secondary.orb.orbSizeMultiplier, ratio)
            )
        )
    }

    private fun NyraPalette.blend(other: NyraPalette, ratio: Float): NyraPalette =
        NyraPalette(
            backgroundPrimary = blendColor(backgroundPrimary, other.backgroundPrimary, ratio),
            backgroundSecondary = blendColor(backgroundSecondary, other.backgroundSecondary, ratio),
            surface = blendColor(surface, other.surface, ratio),
            surfaceSecondary = blendColor(surfaceSecondary, other.surfaceSecondary, ratio),
            primaryGlow = blendColor(primaryGlow, other.primaryGlow, ratio),
            secondaryGlow = blendColor(secondaryGlow, other.secondaryGlow, ratio),
            textPrimary = blendColor(textPrimary, other.textPrimary, ratio),
            textSecondary = blendColor(textSecondary, other.textSecondary, ratio),
            accent = blendColor(accent, other.accent, ratio)
        )

    private fun blendFloat(primary: Float, secondary: Float, ratio: Float): Float =
        primary * ratio + secondary * (1f - ratio)

    private fun blendColor(primary: Long, secondary: Long, ratio: Float): Long {
        val inverse = 1f - ratio
        val a = (primary.channel(24) * ratio + secondary.channel(24) * inverse).roundToInt()
        val r = (primary.channel(16) * ratio + secondary.channel(16) * inverse).roundToInt()
        val g = (primary.channel(8) * ratio + secondary.channel(8) * inverse).roundToInt()
        val b = (primary.channel(0) * ratio + secondary.channel(0) * inverse).roundToInt()
        return ((a.toLong() and 0xFF) shl 24) or
            ((r.toLong() and 0xFF) shl 16) or
            ((g.toLong() and 0xFF) shl 8) or
            (b.toLong() and 0xFF)
    }

    private fun Long.channel(shift: Int): Float =
        ((this ushr shift) and 0xFF).toFloat()

    companion object {
        const val DEFAULT_PRIMARY_RATIO = 0.65f
    }
}

data class BlendedUiStateParts(
    val palette: NyraPalette,
    val background: NyraBackgroundConfig,
    val motion: NyraMotionProfile,
    val cardStyle: NyraCardStyle,
    val orb: NyraOrbConfig
)
