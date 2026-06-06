package com.nyra.app.android.core.profile

import com.nyra.app.android.core.content.model.TraitDefinition
import com.nyra.app.android.core.profile.model.NyraTraitScore
import com.nyra.app.android.core.profile.synthesis.DimensionAggregator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DimensionAggregatorTest {

    @Test
    fun normalizesDimensionAggregationToUnitRange() {
        val repository = FakeNyraContentRepository(
            traits = mapOf(
                "emotional_depth" to TraitDefinition(
                    id = "emotional_depth",
                    category = "emotional",
                    dimensions = mapOf("depth" to 1.0)
                )
            )
        )
        val result = DimensionAggregator(repository).aggregate(
            placements = listOf(placement(dimensionProfile = mapOf("depth" to 0.8))),
            topTraits = listOf(NyraTraitScore("emotional_depth", 2.0))
        )

        assertTrue(result.dimensions.getValue("depth") in 0.0..1.0)
        assertEquals(0.862, result.dimensions.getValue("depth"), 0.01)
    }
}
