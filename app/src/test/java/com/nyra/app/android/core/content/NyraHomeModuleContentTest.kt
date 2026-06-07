package com.nyra.app.android.core.content

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Step 8: every `preferred_home_modules` token in content matches a module the runtime
 * will know how to render.
 *
 * Note: the current `MbtiResultDefinition` model does not yet expose
 * `preferred_home_modules`. Its values in `mbti_results.json` are dropped at parse
 * time by `ignoreUnknownKeys = true`. When the model is extended, add a test mirror
 * of the astrology one below.
 *
 * Allowlist composition (union):
 *  - 12 canonical life area ids.
 *  - Aliases declared by `HomeModuleResolver.areaModuleAliases`.
 *  - Spec-required extras (reflection, emotional_check_in, journal, …).
 *  - Module extension vocabulary used by MBTI / astrology results today
 *    (pattern_insights, exploration_compass, …).
 *  - Archetype module extensions (boundary_reflection, gentle_action, …).
 */
class NyraHomeModuleContentTest {

    private val bundle = ContentTestFixtures.bundle

    private val lifeAreaIds: Set<String> = bundle.lifeAreas.map { it.id }.toSet()

    private val resolverAliases = setOf(
        "profile", "identity_journal",
        "reflection", "inner_world_journal",
        "relationship_reflection",
        "creative_prompt",
        "emotional_recovery",
        "purpose_reflection",
        "exploration_prompt"
    )

    private val specRequiredExtras = setOf(
        "reflection", "emotional_check_in", "journal",
        "core_self", "archetype", "visual_atmosphere"
    )

    private val mbtiAstroExtensions = setOf(
        "atmosphere_player", "belonging_circle", "creativity_studio",
        "exploration_compass", "growth_path", "healing_space",
        "inner_world_journal", "pattern_insights", "purpose_arc",
        "stability_anchor"
    )

    private val archetypeExtensions = setOf(
        "atmosphere_journal", "autonomy_check_in", "belonging_check_in",
        "boundary_reflection", "clarity_check_in", "communication_prompt",
        "creative_action", "creative_reflection", "daily_discovery",
        "emotional_boundaries", "emotional_processing",
        "gentle_action", "growth_map", "healing_reflection",
        "idea_capture", "identity_reflection", "motivation_check_in",
        "new_context_check_in", "pattern_reflection", "private_journal",
        "purpose_planning", "reflection_space", "relationship_nurturing",
        "sensory_reset", "stability_check_in", "structured_journal",
        "symbolic_patterns", "trust_check_in"
    )

    private val knownModules: Set<String> =
        lifeAreaIds +
            resolverAliases +
            specRequiredExtras +
            mbtiAstroExtensions +
            archetypeExtensions

    @Test
    fun astrologyPreferredHomeModulesAreKnown() {
        val unknown = bundle.astrologyPlacements
            .flatMap { p -> p.preferredHomeModules.map { p.id to it } }
            .filterNot { (_, token) -> token in knownModules }
            .map { (id, token) -> "$id → $token" }
        assertTrue("astrology_planet_sign.json preferred_home_modules unknown: $unknown", unknown.isEmpty())
    }

    @Test
    fun archetypePreferredHomeModulesAreKnown() {
        val unknown = bundle.archetypes
            .flatMap { a -> a.preferredHomeModules.map { a.id to it } }
            .filterNot { (_, token) -> token in knownModules }
            .map { (id, token) -> "$id → $token" }
        assertTrue("archetypes.json preferred_home_modules unknown: $unknown", unknown.isEmpty())
    }

    /**
     * Each canonical life area should be addressable as a home module, *either*
     * directly by its id or via a module whose name carries the area as a substring
     * (e.g. `inner_world` is reachable through `inner_world_journal`).
     */
    @Test
    fun everyCanonicalLifeAreaIsAddressableAsAHomeModule() {
        val allUsed = (bundle.astrologyPlacements.flatMap { it.preferredHomeModules } +
            bundle.archetypes.flatMap { it.preferredHomeModules }).toSet()
        val unreachable = lifeAreaIds.filter { areaId ->
            allUsed.none { module -> module == areaId || areaId in module }
        }
        assertTrue(
            "Life areas with no addressable home module: $unreachable",
            unreachable.isEmpty()
        )
    }
}
