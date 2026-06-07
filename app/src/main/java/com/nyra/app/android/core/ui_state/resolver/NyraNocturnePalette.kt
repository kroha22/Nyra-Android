package com.nyra.app.android.core.ui_state.resolver

/**
 * Warm Horizon + Warm Nocturne palette — the canonical Home Screen surface.
 *
 * These are the exact cinematic tokens from the Adaptive Emotional Surface spec.
 * They sit alongside [NyraFoundationColors] (which carries the broader visual-state
 * vocabulary) and are used specifically for Home Screen backgrounds, hero cards,
 * and the orb. Atmospheric, restrained — never gloss, never neon.
 *
 * Color values are ARGB Long (0xAARRGGBB).
 */
object NyraNocturnePalette {

    // ─── Deep backbone ────────────────────────────────────────────────────────
    /** Deepest cinematic black-blue — used at the bottom of fades. */
    const val VoidNavy: Long          = 0xFF070B16
    /** Backbone background for the Home Screen surface. */
    const val DeepSpaceIndigo: Long   = 0xFF0D1330
    /** Mid-layer atmospheric tint, used in horizon gradients. */
    const val AtmosphericViolet: Long = 0xFF2A1F4A

    // ─── Warm focal accents ───────────────────────────────────────────────────
    /** Soft warm-violet that anchors Warm Horizon. */
    const val HorizonLavender: Long   = 0xFFC8B6FF
    /** Aurora-tinted rose — used for hero glow and emotional warmth. */
    const val AuroraRose: Long        = 0xFFF2A7C8
    /** Dawn peach — the warmest focal accent. Used very sparingly. */
    const val PeachDawn: Long         = 0xFFFFC7A6

    // ─── Cool atmospheric accents ─────────────────────────────────────────────
    /** Cool blue mist — gives breathing room and cognitive clarity. */
    const val MistBlue: Long          = 0xFF8BA8FF
    /** Pale cosmic-dust for thin orbital rings and topology lines. */
    const val CosmicDust: Long        = 0xFFD8CCFF

    // ─── Typography on dark surface ───────────────────────────────────────────
    /** Primary text — soft ivory-violet, never pure white. */
    const val TextPrimary: Long       = 0xFFEDE6F5
    /** Secondary text — cooler, lower contrast. */
    const val TextSecondary: Long     = 0xCC9FA1C2
    /** Tertiary text — disabled / footnote tone. */
    const val TextTertiary: Long      = 0x806F7396

    // ─── Surface translucency (for cards "like light in atmosphere") ──────────
    /** ~6% opacity warm surface — primary card translucency. */
    const val WarmSurface: Long       = 0x0FF2A7C8
    /** ~4% opacity cool surface — secondary card translucency. */
    const val CoolSurface: Long       = 0x0A8BA8FF
    /** Very faint surface for resting cards. */
    const val RestingSurface: Long    = 0x08C8B6FF

    // ─── Glow / ring intensities ──────────────────────────────────────────────
    /** Primary atmospheric ring stroke color, low alpha. */
    const val RingPrimary: Long       = 0x66C8B6FF
    /** Secondary ring (outer halo). */
    const val RingSecondary: Long     = 0x33F2A7C8
    /** Topology line tint — used very lightly. */
    const val TopologyLine: Long      = 0x22D8CCFF
}
