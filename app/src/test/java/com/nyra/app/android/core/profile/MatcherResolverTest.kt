package com.nyra.app.android.core.profile

import com.nyra.app.android.core.content.model.ArchetypeDefinition
import com.nyra.app.android.core.content.model.AreaComboRule
import com.nyra.app.android.core.content.model.LifeAreaDefinition
import com.nyra.app.android.core.profile.matcher.ArchetypeMatcher
import com.nyra.app.android.core.profile.matcher.ComboMatcher
import com.nyra.app.android.core.profile.model.NyraAreaScore
import com.nyra.app.android.core.profile.model.NyraDimensionProfile
import com.nyra.app.android.core.profile.model.NyraTraitScore
import com.nyra.app.android.core.profile.model.NyraVisualState
import com.nyra.app.android.core.profile.resolver.HomeModuleResolver
import com.nyra.app.android.core.profile.resolver.VisualStateResolver
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MatcherResolverTest {

    @Test
    fun activatesComboWhenMinimumTraitMatchIsMet() {
        val repository = FakeNyraContentRepository(
            combos = mapOf(
                "identity" to listOf(
                    AreaComboRule(
                        id = "identity_symbolic",
                        area = "identity",
                        requiredTraits = listOf("meaning_seeking", "symbolic_thinking"),
                        minimumMatch = 1,
                        weight = 0.8,
                        title = text(),
                        shortSummary = text(),
                        summary = text()
                    )
                )
            )
        )

        val result = ComboMatcher(repository).match(
            topTraits = listOf(NyraTraitScore("meaning_seeking", 1.0)),
            areas = listOf(NyraAreaScore("identity", 1.0))
        )

        assertEquals("identity_symbolic", result.first().comboId)
    }

    @Test
    fun returnsTopArchetypeWhenTraitDimensionAndAreaFit() {
        val repository = FakeNyraContentRepository(
            archetypes = listOf(
                ArchetypeDefinition(
                    id = "quiet_seeker",
                    priority = 90,
                    title = text(),
                    shortSummary = text(),
                    requiredTraits = listOf("meaning_seeking", "emotional_depth"),
                    minimumMatch = 1,
                    dimensionBias = mapOf("depth" to 0.9),
                    areaBias = mapOf("inner_world" to 1.0),
                    summary = text()
                )
            )
        )

        val result = ArchetypeMatcher(repository).match(
            topTraits = listOf(NyraTraitScore("meaning_seeking", 1.0)),
            dimensions = NyraDimensionProfile(mapOf("depth" to 0.9)),
            areas = listOf(NyraAreaScore("inner_world", 1.0))
        )

        assertEquals("quiet_seeker", result.first().archetypeId)
    }

    @Test
    fun fallsBackToWarmHorizonWhenNoVisualSignalsExist() {
        val result = VisualStateResolver().resolve(
            placements = emptyList(),
            activeArchetypes = emptyList(),
            aestheticTags = emptyList(),
            dimensions = NyraDimensionProfile(emptyMap())
        )

        assertEquals(listOf(NyraVisualState.WARM_HORIZON), result)
    }

    @Test
    fun prioritizesHomeModulesFromPlacementsArchetypesAndAreas() {
        val modules = HomeModuleResolver().resolve(
            placements = listOf(placement(preferredHomeModules = listOf("reflection"))),
            areas = listOf(NyraAreaScore("inner_world", 1.0)),
            activeArchetypes = listOf(
                ArchetypeDefinition(
                    id = "quiet_seeker",
                    priority = 90,
                    title = text(),
                    shortSummary = text(),
                    requiredTraits = emptyList(),
                    summary = text(),
                    preferredHomeModules = listOf("symbolic_patterns")
                )
            )
        )

        assertTrue("symbolic_patterns" in modules)
        assertTrue("inner_world" in modules)
    }
}
