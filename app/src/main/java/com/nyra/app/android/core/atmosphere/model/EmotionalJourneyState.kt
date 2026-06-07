package com.nyra.app.android.core.atmosphere.model

import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.onboarding.model.AtmosphereVariant

/**
 * Emotional Journey states from the layer-system spec.
 *
 * **Different axis from [EmotionalRingState] / [EmotionalRibbonState].** Those five
 * states (Calm/Reflective/Overwhelmed/Hopeful/Disconnected) describe the *shape*
 * of the user's current emotional weather. Journey states describe the *depth of
 * the relationship* with Nyra — how far the atmosphere has been allowed to
 * unfold. Each higher state unlocks additional layers without ever swapping the
 * palette wholesale (per the spec's "colors slowly drift emotionally" principle).
 *
 *  - [Empty]       — calm, open, spacious. Minimal motion.
 *  - [Awakening]   — adds subtle ribbon flow, slightly warmer glow.
 *  - [Reflective]  — emotional flow ribbons, softer haze.
 *  - [Insight]     — adds symbolic topology + cooler moonstone highlights.
 *  - [Memory]      — warmer light, layered topology, soft ambient particles.
 *
 * Each state has a [toLayerState] builder that produces a [NyraEmotionalLayerState]
 * with the right combination of toggles. Use this when you want a quick preset;
 * for finer control, build the state object directly.
 */
enum class EmotionalJourneyState {
    Empty,
    Awakening,
    Reflective,
    Insight,
    Memory;

    fun toLayerState(
        timeOfDay: TimeOfDay = TimeOfDay.Evening,
        variant: AtmosphereVariant = AtmosphereVariant.HorizonDawn,
        ringState: EmotionalRingState = EmotionalRingState.Calm,
        ribbonState: EmotionalRibbonState = EmotionalRibbonState.Calm
    ): NyraEmotionalLayerState = when (this) {
        Empty -> NyraEmotionalLayerState(
            variant = variant,
            timeOfDay = timeOfDay,
            ringState = ringState,
            ribbonState = ribbonState,
            topologyIntensity = 0,
            ribbonsVisible = false,
            particleDensity = 0.10f,
            interiorOverlayAlpha = 0.0f,
            reflectiveDepthVisible = true,
            warmthBias = 0.0f
        )
        Awakening -> NyraEmotionalLayerState(
            variant = variant,
            timeOfDay = timeOfDay,
            ringState = ringState,
            ribbonState = ribbonState,
            topologyIntensity = 0,
            ribbonsVisible = true,
            particleDensity = 0.18f,
            interiorOverlayAlpha = 0.0f,
            reflectiveDepthVisible = true,
            warmthBias = 0.15f
        )
        Reflective -> NyraEmotionalLayerState(
            variant = variant,
            timeOfDay = timeOfDay,
            ringState = EmotionalRingState.Reflective,
            ribbonState = EmotionalRibbonState.Reflective,
            topologyIntensity = 1,
            ribbonsVisible = true,
            particleDensity = 0.22f,
            interiorOverlayAlpha = 0.0f,
            reflectiveDepthVisible = true,
            warmthBias = 0.20f
        )
        Insight -> NyraEmotionalLayerState(
            variant = variant,
            timeOfDay = timeOfDay,
            ringState = EmotionalRingState.Reflective,
            ribbonState = ribbonState,
            topologyIntensity = 2,
            ribbonsVisible = true,
            particleDensity = 0.20f,
            interiorOverlayAlpha = 0.0f,
            reflectiveDepthVisible = true,
            warmthBias = 0.10f       // cooler moonstone highlights — pull warmth back
        )
        Memory -> NyraEmotionalLayerState(
            variant = variant,
            timeOfDay = timeOfDay,
            ringState = ringState,
            ribbonState = ribbonState,
            topologyIntensity = 3,
            ribbonsVisible = true,
            particleDensity = 0.30f,
            interiorOverlayAlpha = 0.18f, // optional silhouette presence becomes available
            reflectiveDepthVisible = true,
            warmthBias = 0.35f
        )
    }
}
