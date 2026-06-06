package com.nyra.app.android.core.profile

import com.nyra.app.android.core.profile.synthesis.TraitScoreAggregator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TraitScoreAggregatorTest {

    private val aggregator = TraitScoreAggregator()

    @Test
    fun aggregatesCoreTraitsAndTraitWeights() {
        val result = aggregator.aggregateTopTraits(
            placements = listOf(
                placement(
                    coreTraits = listOf("emotional_depth", "meaning_seeking"),
                    traitWeights = mapOf("emotional_depth" to 0.5, "warmth" to 0.8)
                )
            )
        )

        assertEquals("emotional_depth", result.first().traitId)
        assertTrue(result.first { it.traitId == "warmth" }.score > 0.0)
    }

    @Test
    fun aggregatesShadowTraitsWithReducedWeight() {
        val result = aggregator.aggregateShadowTraits(
            placements = listOf(placement(shadowTraits = listOf("overthinking")))
        )

        assertEquals(0.7, result.first { it.traitId == "overthinking" }.score, 0.0001)
    }
}
