package com.nyra.app.android.core.atmosphere.model

import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.onboarding.model.AtmosphereVariant

/**
 * Single runtime state that drives every atmospheric layer.
 *
 * The compositor [com.nyra.app.android.core.atmosphere.runtime.NyraAtmosphericCompositor]
 * accepts this object and decides how to render the spec's nine layers:
 *
 *  1. procedural atmospheric gradient
 *  2. horizon glow
 *  3. fog diffusion
 *  4. atmospheric rings
 *  5. flow ribbons
 *  6. symbolic topology
 *  7. emotional overlay asset (silhouette / reflective interior — optional)
 *  8. particles
 *  9. glass atmosphere cards / UI content
 *
 * Every layer reads from this single state; nothing else in the runtime needs
 * to know about layer internals.
 *
 * @param variant base atmospheric variant (HorizonDawn / WarmInterior / StillnightMinimal).
 * @param timeOfDay nudges color tints per circadian time-of-day.
 * @param ringState emotional ring geometry — Calm / Reflective / Overwhelmed / Hopeful / Disconnected.
 * @param ribbonState emotional ribbon flow under the same five-state taxonomy.
 * @param topologyIntensity 0–3. 0 hides the topology lattice; 3 shows the densest.
 * @param ribbonsVisible whether ribbons render at all (Empty Home leaves them off).
 * @param particleDensity 0..1, fed into particle layer drift count.
 * @param interiorOverlayAlpha 0..1, opacity of the optional silhouette / interior overlay
 *   image. 0 means the overlay slot is hidden.
 */
data class NyraEmotionalLayerState(
    val variant: AtmosphereVariant = AtmosphereVariant.HorizonDawn,
    val timeOfDay: TimeOfDay = TimeOfDay.Evening,
    val ringState: EmotionalRingState = EmotionalRingState.Calm,
    val ribbonState: EmotionalRibbonState = EmotionalRibbonState.Calm,
    val topologyIntensity: Int = 0,
    val ribbonsVisible: Boolean = false,
    val particleDensity: Float = 0.20f,
    val interiorOverlayAlpha: Float = 0.0f,
    /**
     * Whether the layer 02 "infinite emotional floor" — a soft reflective sheen
     * + downward fade in the lower half of the screen — is rendered.
     * Enabled by Home and Reflection presets; off for narrow modal flows.
     */
    val reflectiveDepthVisible: Boolean = false,
    /**
     * Soft warmth bias 0..1 used by transitions between [EmotionalJourneyState]
     * variants — lets memory / integration scenes feel slightly warmer than
     * empty/awakening without swapping the palette.
     */
    val warmthBias: Float = 0.0f
) {
    /** Convenience constant for a calm Empty-Home baseline. */
    companion object {
        val Resting: NyraEmotionalLayerState = NyraEmotionalLayerState()
    }
}
