package com.nyra.app.android.core.content

import com.nyra.app.android.core.content.loader.NyraContentLoadResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

/**
 * Step 2: the real NyraContentLoader parses every canonical file with no errors.
 *
 * Failures here surface immediately (with the validator-style error list) so the
 * test report shows which file / id is bad without chasing stack traces.
 */
class NyraContentParsingTest {

    @Test
    fun loaderReturnsSuccess() {
        when (val result = ContentTestFixtures.loader.load()) {
            is NyraContentLoadResult.Success -> {
                assertNotEquals("Bundle has no traits", 0, result.bundle.traits.size)
                assertNotEquals("Bundle has no life areas", 0, result.bundle.lifeAreas.size)
            }
            is NyraContentLoadResult.Failure -> {
                val summary = result.errors.joinToString("\n") {
                    "  [${it.code}] ${it.source} ${it.id ?: ""}: ${it.message}"
                }
                fail("Loader returned Failure:\n$summary")
            }
        }
    }

    @Test
    fun bundleCollectionsAreNonEmpty() {
        val b = ContentTestFixtures.bundle
        assertTrue("traits empty", b.traits.isNotEmpty())
        assertTrue("lifeAreas empty", b.lifeAreas.isNotEmpty())
        assertTrue("areaComboRules empty", b.areaComboRules.isNotEmpty())
        assertTrue("mbtiResults empty", b.mbtiResults.isNotEmpty())
        assertTrue("astrologyPlacements empty", b.astrologyPlacements.isNotEmpty())
        assertTrue("singleTraitInterpretations empty", b.singleTraitInterpretations.isNotEmpty())
        assertTrue("reflectionPrompts empty", b.reflectionPrompts.isNotEmpty())
        assertTrue("archetypes empty", b.archetypes.isNotEmpty())
    }

    /** MBTI must produce exactly 16 codes. */
    @Test
    fun mbtiResultsHaveCanonicalSixteenCodes() {
        val codes = ContentTestFixtures.bundle.mbtiResults.map { it.code.uppercase() }.toSet()
        val expected = setOf(
            "INFJ", "INFP", "INTJ", "INTP",
            "ISFJ", "ISFP", "ISTJ", "ISTP",
            "ENFJ", "ENFP", "ENTJ", "ENTP",
            "ESFJ", "ESFP", "ESTJ", "ESTP"
        )
        assertEquals(
            "MBTI codes mismatch (extra=${codes - expected}, missing=${expected - codes})",
            expected, codes
        )
    }

    /** Astrology coverage = 5 planets × 12 signs = 60. */
    @Test
    fun astrologyPlacementsCoverAllPlanetSignCombinations() {
        val placements = ContentTestFixtures.bundle.astrologyPlacements
        assertEquals("Expected 60 placements", 60, placements.size)
        val planets = setOf("sun", "moon", "mercury", "venus", "mars")
        val signs = setOf(
            "aries", "taurus", "gemini", "cancer", "leo", "virgo",
            "libra", "scorpio", "sagittarius", "capricorn", "aquarius", "pisces"
        )
        val keys = placements.map { it.planet.lowercase() to it.sign.lowercase() }.toSet()
        val expected = planets.flatMap { p -> signs.map { s -> p to s } }.toSet()
        val missing = expected - keys
        assertTrue("Missing planet/sign combos: $missing", missing.isEmpty())
    }
}
