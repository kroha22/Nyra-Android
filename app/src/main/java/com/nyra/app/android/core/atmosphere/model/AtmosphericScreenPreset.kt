package com.nyra.app.android.core.atmosphere.model

import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.onboarding.model.AtmosphereVariant

/**
 * Per-screen atmospheric presets from the layer-system spec.
 *
 * Each preset enables a specific layer combination — composing these in feature
 * code is much shorter than building a [NyraEmotionalLayerState] from scratch.
 *
 *  - [Home]        — backbone + reflective floor + emotional light + fog +
 *                    minimal ribbons. Feels emotionally spacious.
 *  - [Calibration] — ribbons + glow pulse + adaptive haze + topology gradually
 *                    appearing. Feels like emotional attunement.
 *  - [Reflection]  — reflective interior atmosphere + softer warm light +
 *                    silhouette presence + reduced topology. Quiet intimacy.
 *  - [Blueprint]   — symbolic topology + adaptive geometry + layered resonance
 *                    structures + deeper gradients. Emotionally intelligent
 *                    synthesis.
 *  - [Transition]  — flowing ribbons + glow movement + fog drift + depth
 *                    morphing. Cinematic, alive, in motion.
 *
 * Each preset has a [toLayerState] builder that returns a fully populated
 * [NyraEmotionalLayerState]. Tweak fields after the fact if you want a one-off
 * variation; for systemic changes, edit the preset here.
 */
enum class AtmosphericScreenPreset {
    Home,
    Calibration,
    Reflection,
    Blueprint,
    Transition;

    fun toLayerState(
        timeOfDay: TimeOfDay = TimeOfDay.Evening,
        variant: AtmosphereVariant = AtmosphereVariant.HorizonDawn,
        journey: EmotionalJourneyState = EmotionalJourneyState.Empty,
        ringState: EmotionalRingState = EmotionalRingState.Calm,
        ribbonState: EmotionalRibbonState = EmotionalRibbonState.Calm
    ): NyraEmotionalLayerState {
        // Base on the journey state and apply screen-specific overrides.
        val base = journey.toLayerState(
            timeOfDay = timeOfDay,
            variant = variant,
            ringState = ringState,
            ribbonState = ribbonState
        )
        return when (this) {
            Home -> base.copy(
                // Home is the canonical surface; obey journey state directly.
                reflectiveDepthVisible = true
            )
            Calibration -> base.copy(
                // Glow pulse + ribbons + adaptive haze + topology gradually appearing.
                variant = AtmosphereVariant.StillnightMinimal,
                ringState = EmotionalRingState.Reflective,
                ribbonState = EmotionalRibbonState.Reflective,
                ribbonsVisible = true,
                topologyIntensity = maxOf(base.topologyIntensity, 2),
                particleDensity = 0.22f,
                reflectiveDepthVisible = false,
                interiorOverlayAlpha = 0.0f
            )
            Reflection -> base.copy(
                // Quiet intimacy: warm interior + softer light + silhouette + less topology.
                variant = AtmosphereVariant.WarmInterior,
                ringState = EmotionalRingState.Reflective,
                ribbonState = EmotionalRibbonState.Reflective,
                ribbonsVisible = true,
                topologyIntensity = 0,
                particleDensity = 0.14f,
                reflectiveDepthVisible = true,
                interiorOverlayAlpha = 0.22f,
                warmthBias = maxOf(base.warmthBias, 0.30f)
            )
            Blueprint -> base.copy(
                // Synthesis layer — symbolic topology takes precedence.
                ringState = EmotionalRingState.Reflective,
                ribbonState = EmotionalRibbonState.Reflective,
                ribbonsVisible = true,
                topologyIntensity = 3,
                particleDensity = 0.18f,
                reflectiveDepthVisible = true,
                warmthBias = 0.10f
            )
            Transition -> base.copy(
                // Pure cinematic motion — ribbons + fog drift, no topology dot field.
                ringState = EmotionalRingState.Hopeful,
                ribbonState = EmotionalRibbonState.Hopeful,
                ribbonsVisible = true,
                topologyIntensity = 0,
                particleDensity = 0.24f,
                reflectiveDepthVisible = false
            )
        }
    }
}
