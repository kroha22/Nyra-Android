package com.nyra.app.android.core.atmosphere.palette

/**
 * Warm Horizon palette — cinematic splash / horizon-scene colours.
 *
 * Separate from [com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette]:
 *  - Nocturne is the *Home / onboarding* surface (cool cinematic Warm Nocturne).
 *  - Horizon is the *Splash / hero introduction* surface (muted, dusty, more
 *    peach-anchored — the spec's "luxury cinematic emotional OS" presentation).
 *
 * Both palettes deliberately overlap in tone family but use distinct hexes so
 * the splash never feels identical to the Home backdrop; emotional pacing
 * relies on the eye reading a soft handover from Horizon → Nocturne as the
 * app starts.
 *
 * Color values are ARGB Long (0xAARRGGBB).
 */
object NyraHorizonPalette {

    // ─── Atmospheric darks ────────────────────────────────────────────────────
    /** Deep indigo-black — backbone top of the splash gradient. */
    const val IndigoBlack: Long       = 0xFF090B1A
    /** Midnight violet — mid backbone, just below the eclipse ring. */
    const val MidnightViolet: Long    = 0xFF111428
    /** Dark mauve — backbone bottom, base of the water surface. */
    const val DarkMauve: Long         = 0xFF1A1830

    // ─── Atmospheric mid-tone ─────────────────────────────────────────────────
    /** Dusty mauve — fog, distant mountains, mid-air haze. */
    const val DustyMauve: Long        = 0xFF6F557E
    /** Midnight mauve — slightly warmer dusty haze, sky vertical diffusion. */
    const val MidnightMauve: Long     = 0xFF7C5A86

    // ─── Warm focal accents ───────────────────────────────────────────────────
    /** Soft peach — the horizon bloom + eclipse ring halo focal. */
    const val PeachAtmospheric: Long  = 0xFFF4A97E
    /** Warm ivory — highest highlights, starfield, UI typography. */
    const val IvoryHighlight: Long    = 0xFFFFD8B8

    // ─── Derived tokens (alpha pre-applied) ───────────────────────────────────
    /** Starfield dot tint (low alpha ivory). */
    const val StarfieldDot: Long      = 0x33FFD8B8
    /** Distant mountain silhouette (very low contrast against backbone). */
    const val MountainsBack: Long     = 0x88111428
    /** Mid mountain silhouette (slightly more defined). */
    const val MountainsMid: Long      = 0xCC0A0C18
    /** **V1.0** Foreground mountains — darkest tier, sharp angular silhouettes. */
    const val MountainsForeground: Long = 0xF0050616

    /** Water surface striation. */
    const val WaterSheen: Long        = 0x336F557E
    /** **V1.0** Reflection horizon bloom — wider warm light at the waterline. */
    const val ReflectionBloom: Long   = 0x44F4A97E
    /** Reflection streak (narrow vertical light off the eclipse). */
    const val ReflectionStreak: Long  = 0x33F4A97E
    /** **V1.0** Shimmer highlight — fragmented sparkle dots on the water. */
    const val ShimmerHighlight: Long  = 0x55FFD8B8

    /** Eclipse ring outer stroke (existing). */
    const val EclipseRingStroke: Long = 0xFFFFD8B8
    /** **V1.0** Eclipse inner bright edge — the brightest point of the scene. */
    const val EclipseInnerEdge: Long  = 0xFFFFE9CC
    /** Eclipse outer halo. */
    const val EclipseHalo: Long       = 0x33F4A97E
    /** **V1.0** Eclipse very-wide low-opacity bloom. */
    const val EclipseBloom: Long      = 0x14F4A97E
    /** **V1.0** Eclipse dusty violet diffusion behind the ring. */
    const val EclipseDiffusion: Long  = 0x22534168

    /** Fog diffusion. */
    const val FogHaze: Long           = 0x226F557E
    /** **V1.0** Sky vertical diffusion overlay. */
    const val SkyDiffusion: Long      = 0x117C5A86
    /** **V1.0** Cinematic grain dot tint — extremely subtle. */
    const val CinematicGrain: Long    = 0x06FFD8B8

    /** Silhouette figure colour — V1.0 slightly lifted vs pure shadow. */
    const val FigureShadow: Long      = 0xC8060818
    /** **V1.0** Subtle rim light along the figure edge. */
    const val FigureRimLight: Long    = 0x38FFD8B8
    /** Drift particle tint. */
    const val DriftParticle: Long     = 0x44FFD8B8

    /** Primary text tint for the UI overlay (NYRA wordmark). */
    const val OverlayPrimaryText: Long   = 0xFFFFD8B8
    /** Secondary text tint (tagline). */
    const val OverlaySecondaryText: Long = 0xCC6F557E
}
