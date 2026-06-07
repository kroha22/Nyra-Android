package com.nyra.app.android.core.integration

import com.nyra.app.android.core.content.ContentTestFixtures
import com.nyra.app.android.core.content.availability.NyraContentAvailability
import com.nyra.app.android.core.content.availability.NyraFeatureAvailabilityResolver
import com.nyra.app.android.core.content.repository.NyraContentRepository
import com.nyra.app.android.core.content.repository.NyraContentRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Step 9: NyraContentRepository wired against the real loader / validator / availability
 * resolver. Verifies common accessor patterns return non-empty, well-shaped data.
 */
class NyraRepositoryRealDataTest {

    private lateinit var repository: NyraContentRepository

    @Before
    fun setUp() = runTest {
        repository = NyraContentRepositoryImpl(
            loader = ContentTestFixtures.loader,
            validator = ContentTestFixtures.validator,
            availabilityResolver = NyraFeatureAvailabilityResolver()
        )
        val availability = repository.loadContent()
        assertEquals(NyraContentAvailability.Available, availability)
        assertTrue("Content not marked ready", repository.getContentState().isContentReady)
    }

    @Test
    fun getTraitReturnsKnownTrait() {
        val trait = repository.getTrait("emotional_depth")
        assertNotNull("emotional_depth missing", trait)
        assertEquals("emotional_depth", trait!!.id)
    }

    @Test
    fun getTraitReturnsNullForUnknown() {
        assertEquals(null, repository.getTrait("totally_fake_trait_id_zzz"))
    }

    @Test
    fun getAreaReturnsKnownArea() {
        val area = repository.getArea("inner_world")
        assertNotNull("inner_world area missing", area)
        assertEquals("inner_world", area!!.id)
    }

    @Test
    fun getMbtiResultReturnsKnownType() {
        val infj = repository.getMbtiResult("INFJ")
        assertNotNull("INFJ MBTI result missing", infj)
        assertEquals("INFJ", infj!!.code.uppercase())
    }

    @Test
    fun getAstrologyPlacementResolvesPlanetAndSign() {
        val sunCancer = repository.getAstrologyPlacement("sun", "cancer")
        assertNotNull("sun_cancer placement missing", sunCancer)
        assertEquals("sun", sunCancer!!.planet.lowercase())
        assertEquals("cancer", sunCancer.sign.lowercase())
    }

    @Test
    fun getCombosForAreaReturnsNonEmptyList() {
        val combos = repository.getCombosForArea("inner_world")
        assertTrue("No combos for inner_world", combos.isNotEmpty())
        assertTrue(
            "Combos should be returned sorted by weight desc",
            combos.zipWithNext().all { (a, b) -> a.weight >= b.weight }
        )
    }

    @Test
    fun getPromptsByTagsReturnsRelevantPrompts() {
        val prompts = repository.getPromptsByTags(setOf("inner_world"))
        assertTrue("Expected at least one prompt tagged inner_world", prompts.isNotEmpty())
    }

    @Test
    fun getArchetypesReturnsNonEmptyList() {
        val all = repository.getArchetypes()
        assertTrue("No archetypes loaded", all.isNotEmpty())
        assertTrue(
            "Archetypes should be sorted by priority desc",
            all.zipWithNext().all { (a, b) -> a.priority >= b.priority }
        )
    }

    @Test
    fun getSingleTraitInterpretationsReturnsRankedByPriority() {
        val area = repository.getArea("inner_world")!!
        val matches = repository.getSingleTraitInterpretations(
            areaId = area.id,
            traitIds = setOf("emotional_depth")
        )
        assertTrue("Expected single-trait interpretation for inner_world + emotional_depth", matches.isNotEmpty())
        assertTrue(
            "Should be priority-sorted desc",
            matches.zipWithNext().all { (a, b) -> a.priority >= b.priority }
        )
    }
}
