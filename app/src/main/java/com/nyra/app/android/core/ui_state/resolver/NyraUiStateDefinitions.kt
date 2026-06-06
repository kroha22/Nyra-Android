package com.nyra.app.android.core.ui_state.resolver

import com.nyra.app.android.core.profile.model.NyraVisualState
import com.nyra.app.android.core.ui_state.asset.NyraUiStateAssetResolver
import com.nyra.app.android.core.ui_state.model.NyraBackgroundConfig
import com.nyra.app.android.core.ui_state.model.NyraCardStyle
import com.nyra.app.android.core.ui_state.model.NyraMotionProfile
import com.nyra.app.android.core.ui_state.model.NyraOrbConfig
import com.nyra.app.android.core.ui_state.model.NyraPalette
import com.nyra.app.android.core.ui_state.model.NyraTextTone
import com.nyra.app.android.core.ui_state.model.NyraUiStateDefinition
import javax.inject.Inject

class NyraUiStateDefinitions @Inject constructor(
    private val assetResolver: NyraUiStateAssetResolver
) {

    fun get(state: NyraVisualState): NyraUiStateDefinition =
        definitions[state] ?: definitions.getValue(NyraVisualState.WARM_HORIZON)

    private fun definition(
        state: NyraVisualState,
        palette: NyraPalette,
        blurStrength: Float,
        glowIntensity: Float,
        motion: NyraMotionProfile,
        cardStyle: NyraCardStyle,
        orbPulseSpeed: Float,
        orbSizeMultiplier: Float,
        textTone: NyraTextTone,
        defaultModules: List<String>
    ): NyraUiStateDefinition {
        val assets = assetResolver.resolve(state)
        return NyraUiStateDefinition(
            state = state,
            palette = palette,
            background = NyraBackgroundConfig(
                backgroundAsset = assets.backgroundAsset,
                gradientAsset = assets.gradientAsset,
                particleAsset = assets.particleAsset,
                blurStrength = blurStrength,
                glowIntensity = glowIntensity
            ),
            motion = motion,
            cardStyle = cardStyle,
            orb = NyraOrbConfig(
                orbStyle = assets.orbAsset,
                orbGlowColor = palette.primaryGlow,
                orbPulseSpeed = orbPulseSpeed,
                orbSizeMultiplier = orbSizeMultiplier
            ),
            textTone = textTone,
            defaultModules = defaultModules
        )
    }

    private val definitions: Map<NyraVisualState, NyraUiStateDefinition> by lazy {
        mapOf(
            NyraVisualState.WARM_HORIZON to definition(
                state = NyraVisualState.WARM_HORIZON,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF0E1726,
                    backgroundSecondary = 0xFF1F2A3A,
                    surface = 0x2EF3ECE5,
                    surfaceSecondary = 0x1AD6A28D,
                    primaryGlow = 0xFFD6A28D,
                    secondaryGlow = 0xFFF3ECE5,
                    textPrimary = 0xFFF3ECE5,
                    textSecondary = 0xCCD8D4D0,
                    accent = 0xFFD6A28D
                ),
                blurStrength = 18f,
                glowIntensity = 0.54f,
                motion = NyraMotionProfile(0.18f, 0.34f, 0.08f, 900, 0.28f),
                cardStyle = NyraCardStyle(18f, 14f, 0.20f, 0.22f, 16f),
                orbPulseSpeed = 0.34f,
                orbSizeMultiplier = 1.0f,
                textTone = NyraTextTone.CALM,
                defaultModules = listOf("identity", "reflection", "inner_world", "purpose")
            ),
            NyraVisualState.NIGHT_REFLECTION to definition(
                state = NyraVisualState.NIGHT_REFLECTION,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF0A1020,
                    backgroundSecondary = 0xFF11192E,
                    surface = 0x2AD2C2BA,
                    surfaceSecondary = 0x1A7380A3,
                    primaryGlow = 0xFF7380A3,
                    secondaryGlow = 0xFFD2C2BA,
                    textPrimary = 0xFFE8E2DA,
                    textSecondary = 0xBFD2C2BA,
                    accent = 0xFF9FA8C8
                ),
                blurStrength = 24f,
                glowIntensity = 0.36f,
                motion = NyraMotionProfile(0.08f, 0.18f, 0.04f, 1200, 0.14f),
                cardStyle = NyraCardStyle(16f, 18f, 0.16f, 0.14f, 18f),
                orbPulseSpeed = 0.18f,
                orbSizeMultiplier = 0.94f,
                textTone = NyraTextTone.REFLECTIVE,
                defaultModules = listOf("inner_world", "reflection", "symbolic_patterns", "journal")
            ),
            NyraVisualState.DEEP_WATER to definition(
                state = NyraVisualState.DEEP_WATER,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF071520,
                    backgroundSecondary = 0xFF0D2230,
                    surface = 0x2ABFD0D8,
                    surfaceSecondary = 0x185F8194,
                    primaryGlow = 0xFF5F8194,
                    secondaryGlow = 0xFFBFD0D8,
                    textPrimary = 0xFFE7F0F4,
                    textSecondary = 0xBFBFD0D8,
                    accent = 0xFF88AFC1
                ),
                blurStrength = 26f,
                glowIntensity = 0.46f,
                motion = NyraMotionProfile(0.14f, 0.24f, 0.07f, 1050, 0.24f),
                cardStyle = NyraCardStyle(20f, 20f, 0.14f, 0.18f, 18f),
                orbPulseSpeed = 0.24f,
                orbSizeMultiplier = 1.08f,
                textTone = NyraTextTone.SUPPORTIVE,
                defaultModules = listOf("healing", "inner_world", "reflection", "emotional_recovery")
            ),
            NyraVisualState.GOLD_WARMTH to definition(
                state = NyraVisualState.GOLD_WARMTH,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF17131B,
                    backgroundSecondary = 0xFF2A1D24,
                    surface = 0x30FFE4BC,
                    surfaceSecondary = 0x1AFFB357,
                    primaryGlow = 0xFFFFB357,
                    secondaryGlow = 0xFFFFE4BC,
                    textPrimary = 0xFFFFF3E1,
                    textSecondary = 0xCCFFE4BC,
                    accent = 0xFFFFB357
                ),
                blurStrength = 20f,
                glowIntensity = 0.68f,
                motion = NyraMotionProfile(0.20f, 0.42f, 0.10f, 820, 0.34f),
                cardStyle = NyraCardStyle(18f, 16f, 0.20f, 0.30f, 15f),
                orbPulseSpeed = 0.42f,
                orbSizeMultiplier = 1.04f,
                textTone = NyraTextTone.EXPRESSIVE,
                defaultModules = listOf("relationships", "identity", "creative_prompt", "reflection")
            ),
            NyraVisualState.SOFT_DAWN to definition(
                state = NyraVisualState.SOFT_DAWN,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF182031,
                    backgroundSecondary = 0xFF2A3443,
                    surface = 0x32F7D9C8,
                    surfaceSecondary = 0x1AE9BFAE,
                    primaryGlow = 0xFFE9BFAE,
                    secondaryGlow = 0xFFF7E5D9,
                    textPrimary = 0xFFF7EEE8,
                    textSecondary = 0xCCD8CDD0,
                    accent = 0xFFE9BFAE
                ),
                blurStrength = 17f,
                glowIntensity = 0.48f,
                motion = NyraMotionProfile(0.16f, 0.28f, 0.06f, 900, 0.22f),
                cardStyle = NyraCardStyle(18f, 14f, 0.18f, 0.20f, 16f),
                orbPulseSpeed = 0.28f,
                orbSizeMultiplier = 0.98f,
                textTone = NyraTextTone.SUPPORTIVE,
                defaultModules = listOf("reflection", "check_in", "healing", "identity")
            ),
            NyraVisualState.CRYSTAL_CLARITY to definition(
                state = NyraVisualState.CRYSTAL_CLARITY,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF0D1620,
                    backgroundSecondary = 0xFF172333,
                    surface = 0x2FE5EDF2,
                    surfaceSecondary = 0x168DB5C8,
                    primaryGlow = 0xFF8DB5C8,
                    secondaryGlow = 0xFFE5EDF2,
                    textPrimary = 0xFFF1F7FA,
                    textSecondary = 0xC7D9E5EA,
                    accent = 0xFF9DCFE2
                ),
                blurStrength = 12f,
                glowIntensity = 0.38f,
                motion = NyraMotionProfile(0.10f, 0.20f, 0.04f, 760, 0.10f),
                cardStyle = NyraCardStyle(14f, 10f, 0.24f, 0.12f, 14f),
                orbPulseSpeed = 0.20f,
                orbSizeMultiplier = 0.92f,
                textTone = NyraTextTone.ANALYTICAL,
                defaultModules = listOf("identity", "insights", "journal", "patterns")
            ),
            NyraVisualState.HORIZON_MOTION to definition(
                state = NyraVisualState.HORIZON_MOTION,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF101926,
                    backgroundSecondary = 0xFF1C3040,
                    surface = 0x2FE8D8B8,
                    surfaceSecondary = 0x1A7FA6A9,
                    primaryGlow = 0xFF7FA6A9,
                    secondaryGlow = 0xFFE8D8B8,
                    textPrimary = 0xFFF0ECE2,
                    textSecondary = 0xC8D7D3C8,
                    accent = 0xFFE0B56E
                ),
                blurStrength = 15f,
                glowIntensity = 0.52f,
                motion = NyraMotionProfile(0.30f, 0.36f, 0.15f, 720, 0.32f),
                cardStyle = NyraCardStyle(16f, 12f, 0.18f, 0.20f, 15f),
                orbPulseSpeed = 0.36f,
                orbSizeMultiplier = 1.02f,
                textTone = NyraTextTone.EXPLORATORY,
                defaultModules = listOf("exploration", "purpose", "creative_prompt", "identity")
            ),
            NyraVisualState.VELVET_INTIMACY to definition(
                state = NyraVisualState.VELVET_INTIMACY,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF160E18,
                    backgroundSecondary = 0xFF271827,
                    surface = 0x30F1C7CF,
                    surfaceSecondary = 0x1AAE667C,
                    primaryGlow = 0xFFAE667C,
                    secondaryGlow = 0xFFF1C7CF,
                    textPrimary = 0xFFF8E7EA,
                    textSecondary = 0xCFE6C8CF,
                    accent = 0xFFE0A0AD
                ),
                blurStrength = 24f,
                glowIntensity = 0.58f,
                motion = NyraMotionProfile(0.11f, 0.25f, 0.05f, 1050, 0.18f),
                cardStyle = NyraCardStyle(20f, 20f, 0.15f, 0.24f, 18f),
                orbPulseSpeed = 0.25f,
                orbSizeMultiplier = 1.05f,
                textTone = NyraTextTone.INTIMATE,
                defaultModules = listOf("relationships", "reflection", "inner_world", "journal")
            )
        )
    }
}
