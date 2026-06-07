package com.nyra.app.android.core.onboarding.runtime.components

/**
 * Emotional Calibration palette — V1.0.
 *
 * Separate from [com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette]
 * because V1.0 deliberately reduces lavender / purple dominance by ~50–60% in
 * favour of deep navy + moonstone + smoky violet. The Nocturne palette stays in
 * place for Home and other surfaces so the rebalance only ships where the spec
 * asked for it (the Calibration overlay).
 *
 *  - Foundation tones carry the new "premium emotional architecture" feel.
 *  - Accents (Warm Peach Glow / Aurora Rose / Horizon Lavender V1) appear
 *    sparingly — focal light + selected interactive states only, never
 *    full-screen haze.
 *  - Translucent surface tokens replace direct `Color.copy(alpha = …)` so
 *    cards float consistently on the new darker backbone.
 */
object NyraCalibrationPaletteV1 {

    // ─── Foundation ──────────────────────────────────────────────────────────
    /** Deepest base — top of the backbone. */
    const val VoidNavy: Long              = 0xFF070B16
    /** Mid backbone — most of the screen sits in this tone. */
    const val MidnightIndigo: Long        = 0xFF10182A
    /** Lower backbone — bottom 30% of frame. */
    const val DeepAtmosphereBlue: Long    = 0xFF162033
    /** Cool atmospheric mid-tone — fog, distant haze. */
    const val MoonstoneMist: Long         = 0xFF3A4158
    /** Desaturated violet — used sparingly so the screen doesn't go monochrome navy. */
    const val SmokyViolet: Long           = 0xFF2A263B

    // ─── Accents (use sparingly) ─────────────────────────────────────────────
    /** Focal emotional light — the single peach glow per screen. */
    const val WarmPeachGlow: Long         = 0xFFF4B08A
    /** Aurora rose — primary CTA glow, selected wheel row warm tint. */
    const val AuroraRose: Long            = 0xFFD48CA6
    /** Horizon lavender — desaturated V1, replaces the brighter Nocturne version. */
    const val HorizonLavenderV1: Long     = 0xFF6E5FA8

    // ─── Typography ──────────────────────────────────────────────────────────
    /** Primary text — ivory-violet, never pure white. */
    const val TextPrimary: Long           = 0xFFEDE6F5
    /** Secondary text — cooler, lower contrast. */
    const val TextSecondary: Long         = 0xCCB7B0CF
    /** Tertiary text — atmospheric labels, captions. */
    const val TextTertiary: Long          = 0x807D7896
    /** Faded wheel row text — cold, desaturated. */
    const val WheelRowFaded: Long         = 0x803A4158

    // ─── Translucent surfaces (cards floating above atmosphere) ──────────────
    /** Standard card surface — neutral floating glass. */
    const val SurfaceCard: Long           = 0x14EDE6F5
    /** Hero / strong card — slightly warmer, slightly brighter. */
    const val SurfaceCardStrong: Long     = 0x18F4B08A
    /** Wheel picker inner panel — colder, more isolation. */
    const val SurfaceWheel: Long          = 0x14162033
    /** Wheel picker selected-row underlay — local warmth. */
    const val SurfaceWheelSelected: Long  = 0x20F4B08A

    // ─── Borders ─────────────────────────────────────────────────────────────
    /** Standard card border — barely visible. */
    const val BorderCard: Long            = 0x336E5FA8
    /** Strong card border — slightly more present. */
    const val BorderCardStrong: Long      = 0x55D48CA6
    /** Wheel picker selected row border. */
    const val BorderWheelSelected: Long   = 0x55F4B08A

    // ─── Glow / atmosphere derived tokens ────────────────────────────────────
    /** Inner ring stroke (single focal). */
    const val FocalRingStroke: Long       = 0x77F4B08A
    /** Focal ring halo. */
    const val FocalRingHalo: Long         = 0x22F4B08A
    /** Corner fog — top-left / top-right. */
    const val FogCool: Long               = 0x14163358
    /** Corner fog — bottom edges, slightly warmer. */
    const val FogWarm: Long               = 0x10C8AAB0
    /** Ribbon stroke (5–12% range). */
    const val RibbonStroke: Long          = 0x18B7B0CF
    /** Resonance arc stroke — replaces polygon topology. */
    const val ResonanceArc: Long          = 0x186E5FA8
    /** Reflective depth plane sheen line. */
    const val ReflectiveSheen: Long       = 0x22F4B08A
    /** Wheel picker outer vignette (sits behind the picker for focal isolation). */
    const val PickerVignette: Long        = 0xCC070B16
}
