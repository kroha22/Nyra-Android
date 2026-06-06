package com.nyra.app.android.core.ui_state.resolver

/**
 * Cinematic foundation palette for Nyra.
 *
 * These are the named atmospheric tokens that every [NyraUiStateDefinition] composes
 * from. Adding a new visual state should mean *combining* these — never inventing a
 * new free-floating hex.
 *
 * Three families:
 *  - **Atmospheric darks** carry the cinematic backbone (background, deep surfaces).
 *    They keep the product from ever feeling bright, glossy, or galaxy-purple.
 *  - **Warm layer** is for emotional accents — peach, mauve, amber, rose, ember.
 *    Warm colors are *focal*, never saturated full-screen.
 *  - **Cool layer** is for clarity / cognition / atmospheric breathing room —
 *    mist, water, moonstone, pearl. Cool tones balance the warm focal points.
 *
 * Color values are ARGB Long (0xAARRGGBB) so they drop directly into [NyraPalette].
 */
object NyraFoundationColors {

    // ─── Atmospheric darks ─────────────────────────────────────────────────────
    /** Deepest night base — used at the bottom of fades, isolated late-night atmosphere. */
    const val VoidNavy: Long      = 0xFF0A1020
    /** Background-primary default for inward / introspective states. */
    const val DeepIndigo: Long    = 0xFF12182B
    /** The canonical Warm Horizon background-primary. */
    const val MidnightBlue: Long  = 0xFF0E1726
    /** Slightly lifted background-secondary; used for clarity / structure states. */
    const val GraphiteBlue: Long  = 0xFF1A2333
    /** Warm-tinted graphite — base for gold_warmth so it never flashes luxury-gold. */
    const val WarmGraphite: Long  = 0xFF1C1A22

    // ─── Specialty darks ───────────────────────────────────────────────────────
    /** Velvet plum base — intimacy + private warmth without becoming wine-red. */
    const val VelvetPlum: Long    = 0xFF1F1825
    /** Deep plum — the night_reflection backbone. */
    const val DeepPlum: Long      = 0xFF1C1626
    /** Abyssal water-blue — deep_water base. */
    const val AbyssBlue: Long     = 0xFF071520

    // ─── Warm emotional layer (accents only — never full-screen) ──────────────
    /** Canonical warm focal — soft peach with a desaturated, evening quality. */
    const val DuskPeach: Long     = 0xFFD6A28D
    /** Muted plum-pink for night_reflection, velvet_intimacy. */
    const val WarmMauve: Long     = 0xFFAE889B
    /** Restrained amber — replaces luxury-gold. Never bright. */
    const val SoftAmber: Long     = 0xFFE0B080
    /** Faded rose — relational warmth without being pink. */
    const val DustRose: Long      = 0xFFC89AA0
    /** Burnt-edge amber for night, intimate firelight feeling. */
    const val EmberGlow: Long     = 0xFFC07A66
    /** Copper-rose for gold_warmth identity accent. */
    const val CopperRose: Long    = 0xFFB8836E

    // ─── Cool atmospheric layer ───────────────────────────────────────────────
    /** Misty blue-grey — soft_dawn breathing, morning openness. */
    const val MistBlue: Long      = 0xFF8DA8BC
    /** Aurora-like teal-blue — horizon_motion forward feeling. */
    const val AuroraBlue: Long    = 0xFF7FA6A9
    /** Water-glow cyan — deep_water highlight. */
    const val WaterGlow: Long     = 0xFF88AFC1
    /** Moonstone — crystal_clarity cognitive light. */
    const val Moonstone: Long     = 0xFF9FA8C8
    /** Pale lavender — dawn / clarity highlight. */
    const val PaleLavender: Long  = 0xFFBDB8D5
    /** Pearl lavender — deep_water surface highlight. */
    const val PearlLavender: Long = 0xFFD8D4E8

    // ─── Soft neutrals ────────────────────────────────────────────────────────
    /** Pearl grey — neutral atmospheric token. */
    const val PearlGrey: Long     = 0xFFD8D4D0
    /** Dawn peach — soft_dawn warm accent, lighter than dusk peach. */
    const val DawnPeach: Long     = 0xFFE9BFAE
    /** Lavender haze — warm_horizon middle layer. */
    const val LavenderHaze: Long  = 0xFFB1A6B8

    // ─── Typography ──────────────────────────────────────────────────────────
    /** Primary text on dark cinematic backgrounds — ivory, soft, never pure white. */
    const val IvoryHigh: Long     = 0xFFF3ECE5
    /** Secondary text — slightly cooler ivory for hierarchy. */
    const val IvoryMid: Long      = 0xCCD8D4D0
    /** Disabled / tertiary text. */
    const val IvoryLow: Long      = 0x99B8B5B0
    /** Warm-side text (pairs with gold_warmth, velvet_intimacy). */
    const val WarmIvory: Long     = 0xFFF7EEE8

    // ─── Translucent surface tokens (for glass cards on dark backgrounds) ─────
    /** Generic warm translucent surface (~18% on ivory). */
    const val WarmSurface: Long       = 0x2EF3ECE5
    /** Cool translucent surface (~18% on moonstone). */
    const val CoolSurface: Long       = 0x2ED8D4E8
    /** Warm secondary surface (~10% on dusk peach). */
    const val WarmSurfaceLow: Long    = 0x1AD6A28D
    /** Cool secondary surface (~10% on moonstone). */
    const val CoolSurfaceLow: Long    = 0x1A9FA8C8
}
