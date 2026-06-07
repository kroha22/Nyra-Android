package com.nyra.app.android.core.content

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Step 3: id integrity — unique, non-empty, snake_case.
 *
 * snake_case rule: lowercase ASCII letters, digits, and underscores; cannot start
 * with a digit or underscore. MBTI codes are exempt (uppercase by convention).
 */
class NyraContentIntegrityTest {

    private val snakeCase = Regex("^[a-z][a-z0-9_]*$")

    @Test
    fun traitIdsAreUniqueAndSnakeCase() {
        val ids = ContentTestFixtures.bundle.traits.map { it.id }
        assertUniqueSnakeCase("traits.json", ids)
    }

    @Test
    fun lifeAreaIdsAreUniqueAndSnakeCase() {
        val ids = ContentTestFixtures.bundle.lifeAreas.map { it.id }
        assertUniqueSnakeCase("life_areas.json", ids)
    }

    @Test
    fun areaComboRuleIdsAreUniqueAndSnakeCase() {
        val ids = ContentTestFixtures.bundle.areaComboRules.map { it.id }
        assertUniqueSnakeCase("area_combo_rules.json", ids)
    }

    @Test
    fun mbtiResultIdsAreUniqueAndCodesAreUnique() {
        val b = ContentTestFixtures.bundle
        val ids = b.mbtiResults.map { it.id }
        assertUniqueSnakeCase("mbti_results.json (id)", ids)
        val codes = b.mbtiResults.map { it.code.uppercase() }
        assertTrue("MBTI codes duplicated", codes.size == codes.toSet().size)
    }

    @Test
    fun astrologyPlacementIdsAreUniqueAndSnakeCase() {
        val ids = ContentTestFixtures.bundle.astrologyPlacements.map { it.id }
        assertUniqueSnakeCase("astrology_planet_sign.json", ids)
    }

    @Test
    fun singleTraitInterpretationIdsAreUniqueAndSnakeCase() {
        val ids = ContentTestFixtures.bundle.singleTraitInterpretations.map { it.id }
        assertUniqueSnakeCase("single_trait_interpretations.json", ids)
    }

    @Test
    fun reflectionPromptIdsAreUniqueAndSnakeCase() {
        val ids = ContentTestFixtures.bundle.reflectionPrompts.map { it.id }
        assertUniqueSnakeCase("reflection_prompts.json", ids)
    }

    @Test
    fun archetypeIdsAreUniqueAndSnakeCase() {
        val ids = ContentTestFixtures.bundle.archetypes.map { it.id }
        assertUniqueSnakeCase("archetypes.json", ids)
    }

    private fun assertUniqueSnakeCase(source: String, ids: List<String>) {
        val blanks = ids.filter { it.isBlank() }
        assertTrue("$source has blank ids", blanks.isEmpty())

        val dupes = ids.groupingBy { it }.eachCount().filterValues { it > 1 }
        assertTrue("$source duplicates: $dupes", dupes.isEmpty())

        val malformed = ids.filterNot { snakeCase.matches(it) }
        assertTrue("$source non-snake_case ids: $malformed", malformed.isEmpty())
    }
}
