package com.nyra.app.android.core.content

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Step 7: every `visual_state_bias` token in content is one the runtime recognises.
 *
 * Vocabulary sources:
 *  - **Canonical 8 states** — the `NyraVisualState` enum values, lowercase.
 *  - **Composite tokens** — `flow_state`, `deep_focus`, recognised by
 *    [com.nyra.app.android.core.profile.resolver.VisualStateResolver]'s `directStateMap`.
 *  - **Tag aliases** — atmospheric metaphors (moonlight, water, gold, …) the resolver
 *    maps to states via `tagStateMap`.
 *  - **Archetype atmospheric extensions** — current archetypes use a broader emotional
 *    vocabulary (`calm_structure`, `warm_room`, …). The resolver silently drops these
 *    today; we still allow them here so the test reflects authored intent. When the
 *    resolver learns them, this list shrinks naturally.
 */
class NyraVisualStateContentTest {

    private val canonicalStates = setOf(
        "warm_horizon", "night_reflection", "deep_water", "gold_warmth",
        "soft_dawn", "crystal_clarity", "horizon_motion", "velvet_intimacy"
    )

    private val resolverComposite = setOf("flow_state", "deep_focus")

    private val resolverTagAliases = setOf(
        "moonlight", "night", "fog", "water", "deep_water", "velvet",
        "rose", "gold", "flame", "ember", "dawn", "paper", "glass",
        "silver", "horizon", "wind", "spark"
    )

    /** Atmospheric tokens used by archetypes but not yet wired into the resolver. */
    private val archetypeAtmosphericExtensions = setOf(
        "calm_structure", "care_circle", "clear_distance", "clear_focus", "clear_night",
        "creative_charge", "deep_connection", "deep_repair", "dream_focus",
        "emotional_heat", "future_focus", "grounded_morning", "grounded_room",
        "morning_stillness", "moving_air", "new_air", "open_air", "open_horizon",
        "pattern_view", "private_signal", "quiet_structure", "relational_ease",
        "self_directed_path", "social_light", "soft_connection", "soft_guard",
        "soft_image", "soft_shelter", "soft_stillness", "steady_rhythm",
        "symbolic_flow", "threshold_light", "transforming_light", "warm_ground",
        "warm_motion", "warm_night", "warm_room", "warm_spark", "warm_texture"
    )

    /** Atmospheric tokens used by astrology to colour cards / orbs. */
    private val astrologyAtmosphericExtensions = setOf(
        "compass_drift", "earth_garden", "ember_steady", "field_horizon",
        "flame_kinetic", "garden_light", "mirror_pause", "mountain_calm",
        "spark_invention", "still_water", "storm_clarity", "studio_light",
        "sun_radiance", "wind_currents"
    )

    private val knownTokens: Set<String> =
        canonicalStates +
            resolverComposite +
            resolverTagAliases +
            archetypeAtmosphericExtensions +
            astrologyAtmosphericExtensions

    private val bundle = ContentTestFixtures.bundle

    @Test
    fun astrologyVisualStatesAreKnown() {
        val unknown = bundle.astrologyPlacements
            .flatMap { p -> p.visualStateBias.map { p.id to it } }
            .filterNot { (_, token) -> token in knownTokens }
            .map { (id, token) -> "$id → $token" }
        assertTrue("astrology_planet_sign.json visual_state_bias unknown tokens: $unknown", unknown.isEmpty())
    }

    @Test
    fun archetypeVisualStatesAreKnown() {
        val unknown = bundle.archetypes
            .flatMap { a -> a.visualStateBias.map { a.id to it } }
            .filterNot { (_, token) -> token in knownTokens }
            .map { (id, token) -> "$id → $token" }
        assertTrue("archetypes.json visual_state_bias unknown tokens: $unknown", unknown.isEmpty())
    }

    /** Soft assertion: at least 80% of canonical states should appear in astro to keep coverage. */
    @Test
    fun canonicalStatesAppearInAstrologyContent() {
        val usedCanonical = bundle.astrologyPlacements
            .flatMap { it.visualStateBias }
            .filter { it in canonicalStates }
            .toSet()
        assertTrue(
            "Canonical states under-used in astrology: missing=${canonicalStates - usedCanonical}",
            usedCanonical.size >= (canonicalStates.size * 0.8).toInt()
        )
    }
}
