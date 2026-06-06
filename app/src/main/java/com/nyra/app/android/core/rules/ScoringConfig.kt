package com.nyra.app.android.core.rules

/**
 * Runtime scoring orchestration layer for Nyra's local emotional synthesis engine.
 *
 * Mirrors `app/src/main/assets/canonical/scoring_config.json` as compile-time constants,
 * so the engine can run without parsing JSON at startup. Keep both files in sync —
 * the JSON stays as the canonical source of truth; this object is the typed mirror.
 */
object ScoringConfig {

    const val VERSION = "1.0"

    // ─── 1. Source weights ──────────────────────────────────────────────────────
    // System-level influence multipliers. MBTI and astrology kept balanced so
    // neither overwhelms the profile; reflection_patterns lighter because journal
    // signal is unsteady; manual_self_selection moderate because user choice
    // deserves weight but isn't always self-honest.

    val SOURCE_WEIGHTS: Map<String, Float> = mapOf(
        "mbti" to 1.0f,
        "astrology" to 0.95f,
        "archetype" to 0.9f,
        "reflection_patterns" to 0.75f,
        "manual_self_selection" to 0.85f
    )

    // ─── 2. Planet weights ──────────────────────────────────────────────────────
    // UX synthesis weights, NOT astrological correctness. Moon highest because
    // emotional regulation drives most atmospheric UX decisions.

    val PLANET_WEIGHTS: Map<String, Float> = mapOf(
        "sun" to 1.0f,
        "moon" to 1.15f,
        "mercury" to 0.85f,
        "venus" to 0.95f,
        "mars" to 0.9f
    )

    val PLANET_CATEGORIES: Map<String, String> = mapOf(
        "sun" to "identity_core",
        "moon" to "emotional_core",
        "mercury" to "cognitive_style",
        "venus" to "relational_aesthetic",
        "mars" to "activation_drive"
    )

    // ─── 3. Area activation ─────────────────────────────────────────────────────
    // Mirrors life_areas.json tiers. Core areas always visible; extended areas
    // activate only when signal score crosses threshold (or a strong combo
    // overrides via strong_combo_override).

    val CORE_AREAS: List<String> = listOf(
        "identity",
        "inner_world",
        "relationships",
        "communication",
        "motivation",
        "growth",
        "creativity",
        "stability"
    )

    val EXTENDED_AREAS: List<String> = listOf(
        "purpose",
        "belonging",
        "exploration",
        "healing"
    )

    const val EXTENDED_AREA_THRESHOLD = 0.72f
    const val STRONG_COMBO_OVERRIDE = true
    const val MIN_ACTIVE_AREAS = 6
    const val MAX_ACTIVE_AREAS = 10
    const val CORE_AREA_MIN_SCORE = 0.0f
    const val AREA_SCORE_NORMALIZATION = "soft_cap"

    // ─── 4. Combo matching ──────────────────────────────────────────────────────

    const val MINIMUM_MATCH_RATIO = 0.7f

    /** Required matches per total-trait count. 3-trait combo → 2, 4-trait → 3, 5-trait → 4. */
    val MINIMUM_MATCH_RULES: Map<Int, Int> = mapOf(
        3 to 2,
        4 to 3,
        5 to 4
    )

    const val WEIGHT_PRIORITY_FACTOR = 1.0f
    const val MATCH_STRENGTH_FACTOR = 1.15f
    const val TRAIT_SCORE_FACTOR = 1.05f
    const val FALLBACK_PRIORITY_FACTOR = 0.9f
    const val MAX_PRIMARY_COMBOS_PER_AREA = 2
    const val MAX_TOTAL_COMBOS = 12
    const val PREMIUM_COMBO_PENALTY = 0.0f
    const val COMBO_TIE_BREAKER = "weight_then_priority"

    // ─── 5. Trait aggregation ───────────────────────────────────────────────────
    // Soft, additive accumulation across sources. duplicate_trait_decay prevents
    // runaway amplification when the same trait appears in MBTI + multiple
    // astrology placements.

    const val TRAIT_NORMALIZATION = "soft_cap"
    const val MAX_TRAIT_SCORE = 100
    const val CROSS_SOURCE_BLEND_MODE = "weighted_average"
    const val DUPLICATE_TRAIT_DECAY = 0.92f
    const val NOISE_FLOOR = 0.15f

    const val DOMINANT_TRAIT_THRESHOLD = 75
    const val SECONDARY_TRAIT_THRESHOLD = 55
    const val SHADOW_TRAIT_THRESHOLD = 65
    const val GROWTH_TRAIT_THRESHOLD = 50

    const val MAX_DOMINANT_TRAITS_SHOWN = 12
    const val MAX_SHADOW_TRAITS_SHOWN = 5
    const val MAX_GROWTH_TRAITS_SHOWN = 5

    const val DIMENSION_BLEND_MODE = "weighted_average"
    const val DIMENSION_MAX_SCORE = 1.0f

    // ─── 6. Fallback logic ──────────────────────────────────────────────────────

    const val USE_PARTIAL_MATCHES = true
    const val MINIMUM_PARTIAL_MATCH_STRENGTH = 0.55f
    const val FALLBACK_TO_SINGLE_TRAITS = true
    const val MAX_SINGLE_TRAIT_INTERPRETATIONS = 3
    const val GENERIC_AREA_SUMMARY_ENABLED = true
    const val EMPTY_AREA_PLACEHOLDER_ENABLED = false
    const val FALLBACK_AESTHETIC_STATE = "soft_dawn"

    val FALLBACK_EMOTIONAL_TONE: Map<String, String> = mapOf(
        "warmth" to "tempered",
        "movement" to "flowing",
        "density" to "light",
        "social_texture" to "selective"
    )

    // ─── 7. Extended area rules ─────────────────────────────────────────────────

    data class ExtendedAreaRule(
        val requiredDimensions: List<String>,
        val activationScore: Int,
        val minSignalsPresent: Int
    )

    val EXTENDED_AREA_RULES: Map<String, ExtendedAreaRule> = mapOf(
        "purpose" to ExtendedAreaRule(
            requiredDimensions = listOf(
                "meaning_seeking",
                "purpose_drive",
                "future_orientation",
                "value_centered_thinking"
            ),
            activationScore = 70,
            minSignalsPresent = 2
        ),
        "belonging" to ExtendedAreaRule(
            requiredDimensions = listOf(
                "belonging_needing",
                "collective_orientation",
                "people_focused",
                "relationship_nurturing"
            ),
            activationScore = 68,
            minSignalsPresent = 2
        ),
        "exploration" to ExtendedAreaRule(
            requiredDimensions = listOf(
                "novelty_seeking",
                "discovery_orientation",
                "freedom_needing",
                "adventurous_drive"
            ),
            activationScore = 68,
            minSignalsPresent = 2
        ),
        "healing" to ExtendedAreaRule(
            requiredDimensions = listOf(
                "emotional_overwhelm",
                "emotional_exhaustion",
                "introspective_processing",
                "psychological_insight",
                "protective_withdrawal"
            ),
            activationScore = 65,
            minSignalsPresent = 2
        )
    )

    // ─── 8. Dynamic adaptation ──────────────────────────────────────────────────

    /** Multiplicative area boosts by time of day. Inner: area → multiplier. */
    val TIME_OF_DAY_BIAS: Map<String, Map<String, Float>> = mapOf(
        "morning" to mapOf(
            "motivation" to 1.05f,
            "growth" to 1.03f,
            "purpose" to 1.02f
        ),
        "midday" to mapOf(
            "communication" to 1.04f,
            "exploration" to 1.02f
        ),
        "evening" to mapOf(
            "inner_world" to 1.08f,
            "healing" to 1.06f,
            "relationships" to 1.04f
        ),
        "night" to mapOf(
            "inner_world" to 1.1f,
            "healing" to 1.05f
        )
    )

    const val RECENT_CHECKIN_WEIGHT = 1.15f
    const val JOURNAL_PATTERN_WEIGHT = 1.1f
    const val TRACKER_PATTERN_WEIGHT = 1.05f
    const val REFLECTION_RECENCY_DECAY_DAYS = 14

    const val SEASONAL_BIAS_ENABLED = false

    val SEASONAL_BIAS: Map<String, Map<String, Float>> = mapOf(
        "winter" to mapOf(
            "inner_world" to 1.05f,
            "healing" to 1.04f,
            "stability" to 1.03f
        ),
        "spring" to mapOf(
            "growth" to 1.05f,
            "creativity" to 1.04f,
            "exploration" to 1.03f
        ),
        "summer" to mapOf(
            "motivation" to 1.04f,
            "exploration" to 1.05f,
            "communication" to 1.03f
        ),
        "autumn" to mapOf(
            "purpose" to 1.04f,
            "stability" to 1.03f,
            "inner_world" to 1.03f
        )
    )

    val USER_STATE_BIAS: Map<String, Map<String, Float>> = mapOf(
        "low_energy" to mapOf(
            "healing" to 1.1f,
            "stability" to 1.05f,
            "inner_world" to 1.04f
        ),
        "high_energy" to mapOf(
            "motivation" to 1.05f,
            "exploration" to 1.04f,
            "creativity" to 1.03f
        ),
        "tender" to mapOf(
            "healing" to 1.08f,
            "relationships" to 1.04f,
            "inner_world" to 1.04f
        ),
        "outward" to mapOf(
            "communication" to 1.05f,
            "relationships" to 1.04f,
            "belonging" to 1.03f
        )
    )

    /** Compound bias guardrails — no area can drift more than ±30% even with stacked biases. */
    const val MAX_BIAS_COMPOUND = 1.3f
    const val MIN_BIAS_COMPOUND = 0.85f
}
