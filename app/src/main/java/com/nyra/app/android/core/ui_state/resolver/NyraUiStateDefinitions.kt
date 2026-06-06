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
import com.nyra.app.android.core.ui_state.resolver.NyraFoundationColors as Color
import javax.inject.Inject

/**
 * Canonical definitions for the 8 Nyra atmospheric states.
 *
 * All palettes compose from [NyraFoundationColors] — never invent free-floating hex.
 * `warm_horizon` is the **permanent emotional foundation**; the other 7 are atmospheric
 * variations that must keep its cinematic continuity:
 *  - never over-saturate the background
 *  - keep warm colors as focal accents, not full-screen
 *  - one major glow focal per screen
 *  - matte cinematic darkness, not gloss or neon
 */
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
            // ─── 1. Warm Horizon — the canonical Nyra foundation ──────────────
            // Navy base + dusk peach focal + lavender haze middle layer.
            // Every other state is a variation on this; never lose its silhouette.
            NyraVisualState.WARM_HORIZON to definition(
                state = NyraVisualState.WARM_HORIZON,
                palette = NyraPalette(
                    backgroundPrimary = Color.MidnightBlue,
                    backgroundSecondary = 0xFF1F2A3A,
                    surface = Color.WarmSurface,
                    surfaceSecondary = Color.WarmSurfaceLow,
                    primaryGlow = Color.DuskPeach,
                    secondaryGlow = Color.LavenderHaze,
                    textPrimary = Color.IvoryHigh,
                    textSecondary = Color.IvoryMid,
                    accent = Color.DuskPeach
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

            // ─── 2. Night Reflection — deep emotional introspection ───────────
            // Deep plum + mauve + ember + midnight navy.
            // Slower motion, more blur, isolated glow.
            NyraVisualState.NIGHT_REFLECTION to definition(
                state = NyraVisualState.NIGHT_REFLECTION,
                palette = NyraPalette(
                    backgroundPrimary = Color.DeepPlum,
                    backgroundSecondary = Color.MidnightBlue,
                    surface = 0x26E0D6E0,
                    surfaceSecondary = 0x14AE889B,
                    primaryGlow = Color.WarmMauve,
                    secondaryGlow = Color.EmberGlow,
                    textPrimary = 0xFFEDE3E8,
                    textSecondary = 0xBFCFC0CB,
                    accent = Color.WarmMauve
                ),
                blurStrength = 24f,
                glowIntensity = 0.32f,
                motion = NyraMotionProfile(0.08f, 0.18f, 0.04f, 1200, 0.14f),
                cardStyle = NyraCardStyle(16f, 18f, 0.16f, 0.14f, 18f),
                orbPulseSpeed = 0.18f,
                orbSizeMultiplier = 0.94f,
                textTone = NyraTextTone.REFLECTIVE,
                defaultModules = listOf("inner_world", "reflection", "symbolic_patterns", "journal")
            ),

            // ─── 3. Deep Water — emotional absorption + symbolic depth ────────
            // Abyss blue + deep indigo + water cyan + pearl lavender.
            // Fluid diffuse gradients; underwater-like blur.
            NyraVisualState.DEEP_WATER to definition(
                state = NyraVisualState.DEEP_WATER,
                palette = NyraPalette(
                    backgroundPrimary = Color.AbyssBlue,
                    backgroundSecondary = 0xFF0D2230,
                    surface = 0x28D8D4E8,
                    surfaceSecondary = 0x16599AB0,
                    primaryGlow = Color.WaterGlow,
                    secondaryGlow = Color.PearlLavender,
                    textPrimary = 0xFFE7F0F4,
                    textSecondary = 0xBFBFD0D8,
                    accent = Color.WaterGlow
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

            // ─── 4. Gold Warmth — expressive creative identity ────────────────
            // Warm peach + soft amber + copper rose + velvet plum.
            // CRITICAL: never flash luxury gold. Amber stays muted.
            NyraVisualState.GOLD_WARMTH to definition(
                state = NyraVisualState.GOLD_WARMTH,
                palette = NyraPalette(
                    backgroundPrimary = Color.WarmGraphite,
                    backgroundSecondary = Color.VelvetPlum,
                    surface = 0x28F3DDC8,
                    surfaceSecondary = 0x16E0B080,
                    primaryGlow = Color.SoftAmber,
                    secondaryGlow = Color.CopperRose,
                    textPrimary = Color.WarmIvory,
                    textSecondary = 0xCCE9D5C4,
                    accent = Color.CopperRose
                ),
                blurStrength = 20f,
                glowIntensity = 0.56f,
                motion = NyraMotionProfile(0.20f, 0.42f, 0.10f, 820, 0.32f),
                cardStyle = NyraCardStyle(18f, 16f, 0.20f, 0.26f, 15f),
                orbPulseSpeed = 0.42f,
                orbSizeMultiplier = 1.04f,
                textTone = NyraTextTone.EXPRESSIVE,
                defaultModules = listOf("relationships", "identity", "creative_prompt", "reflection")
            ),

            // ─── 5. Soft Dawn — healing + emotional openness ──────────────────
            // Mist blue + pale lavender + dawn peach + pearl grey.
            // Lighter atmosphere, more breathing space, restorative pacing.
            NyraVisualState.SOFT_DAWN to definition(
                state = NyraVisualState.SOFT_DAWN,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF1A2235,
                    backgroundSecondary = 0xFF2A3548,
                    surface = 0x2EE9D8E0,
                    surfaceSecondary = 0x18BDB8D5,
                    primaryGlow = Color.DawnPeach,
                    secondaryGlow = Color.MistBlue,
                    textPrimary = 0xFFF1ECE8,
                    textSecondary = 0xCCD0CDD8,
                    accent = Color.PaleLavender
                ),
                blurStrength = 17f,
                glowIntensity = 0.44f,
                motion = NyraMotionProfile(0.16f, 0.28f, 0.06f, 900, 0.22f),
                cardStyle = NyraCardStyle(18f, 14f, 0.18f, 0.18f, 16f),
                orbPulseSpeed = 0.28f,
                orbSizeMultiplier = 0.98f,
                textTone = NyraTextTone.SUPPORTIVE,
                defaultModules = listOf("reflection", "check_in", "healing", "identity")
            ),

            // ─── 6. Crystal Clarity — cognitive understanding + structure ─────
            // Graphite navy + moonstone + silver indigo + cool atmospheric blue.
            // Cleaner topology, reduced fog, sharper structure.
            NyraVisualState.CRYSTAL_CLARITY to definition(
                state = NyraVisualState.CRYSTAL_CLARITY,
                palette = NyraPalette(
                    backgroundPrimary = 0xFF0E1722,
                    backgroundSecondary = Color.GraphiteBlue,
                    surface = 0x2AE3EAF0,
                    surfaceSecondary = 0x149FA8C8,
                    primaryGlow = Color.Moonstone,
                    secondaryGlow = 0xFFE5EDF2,
                    textPrimary = 0xFFEEF3F7,
                    textSecondary = 0xC7CFD6E0,
                    accent = Color.Moonstone
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

            // ─── 7. Horizon Motion — growth + forward movement ────────────────
            // Indigo + aurora blue + horizon peach + atmospheric lavender.
            // Directional gradients, momentum composition.
            NyraVisualState.HORIZON_MOTION to definition(
                state = NyraVisualState.HORIZON_MOTION,
                palette = NyraPalette(
                    backgroundPrimary = Color.DeepIndigo,
                    backgroundSecondary = 0xFF1C2F44,
                    surface = 0x2CE3D7C8,
                    surfaceSecondary = 0x167FA6A9,
                    primaryGlow = Color.AuroraBlue,
                    secondaryGlow = Color.DuskPeach,
                    textPrimary = 0xFFEEEAE0,
                    textSecondary = 0xC8D5D2CA,
                    accent = Color.LavenderHaze
                ),
                blurStrength = 15f,
                glowIntensity = 0.48f,
                motion = NyraMotionProfile(0.30f, 0.36f, 0.15f, 720, 0.30f),
                cardStyle = NyraCardStyle(16f, 12f, 0.18f, 0.18f, 15f),
                orbPulseSpeed = 0.36f,
                orbSizeMultiplier = 1.02f,
                textTone = NyraTextTone.EXPLORATORY,
                defaultModules = listOf("exploration", "purpose", "creative_prompt", "identity")
            ),

            // ─── 8. Velvet Intimacy — private emotional resonance + trust ─────
            // Velvet navy + warm plum + ember rose + dusk peach.
            // Soft intimate lighting, reduced UI noise, protective composition.
            NyraVisualState.VELVET_INTIMACY to definition(
                state = NyraVisualState.VELVET_INTIMACY,
                palette = NyraPalette(
                    backgroundPrimary = Color.VelvetPlum,
                    backgroundSecondary = 0xFF291B26,
                    surface = 0x2EE8C7CF,
                    surfaceSecondary = 0x18AE889B,
                    primaryGlow = Color.EmberGlow,
                    secondaryGlow = Color.DuskPeach,
                    textPrimary = 0xFFF4E5E2,
                    textSecondary = 0xCBD8C5C5,
                    accent = Color.DustRose
                ),
                blurStrength = 24f,
                glowIntensity = 0.50f,
                motion = NyraMotionProfile(0.11f, 0.25f, 0.05f, 1050, 0.18f),
                cardStyle = NyraCardStyle(20f, 20f, 0.15f, 0.22f, 18f),
                orbPulseSpeed = 0.25f,
                orbSizeMultiplier = 1.05f,
                textTone = NyraTextTone.INTIMATE,
                defaultModules = listOf("relationships", "reflection", "inner_world", "journal")
            )
        )
    }
}
