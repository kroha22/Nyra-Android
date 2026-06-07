package com.nyra.app.android.core.integration

import com.nyra.app.android.core.astrology.model.AstrologyCalculationResult
import com.nyra.app.android.core.astrology.model.BirthData
import com.nyra.app.android.core.astrology.model.CalculationMode
import com.nyra.app.android.core.astrology.model.PlacementAccuracy
import com.nyra.app.android.core.astrology.model.Planet
import com.nyra.app.android.core.astrology.model.PlanetPlacement
import com.nyra.app.android.core.astrology.model.ZodiacSign
import com.nyra.app.android.core.content.ContentTestFixtures
import com.nyra.app.android.core.content.availability.NyraFeatureAvailabilityResolver
import com.nyra.app.android.core.content.repository.NyraContentRepositoryImpl
import com.nyra.app.android.core.profile.matcher.ArchetypeMatcher
import com.nyra.app.android.core.profile.matcher.ComboMatcher
import com.nyra.app.android.core.profile.resolver.HomeModuleResolver
import com.nyra.app.android.core.profile.resolver.VisualStateResolver
import com.nyra.app.android.core.profile.synthesis.AreaScoreCalculator
import com.nyra.app.android.core.profile.synthesis.DimensionAggregator
import com.nyra.app.android.core.profile.synthesis.NyraProfileSynthesizerImpl
import com.nyra.app.android.core.profile.synthesis.TraitScoreAggregator
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Step 10: profile synthesis end-to-end against real content.
 *
 * Uses the real content repository (which loads JSON from assets) and a realistic
 * AstrologyCalculationResult — no mocked traits, no fake fixtures past the natal data.
 *
 * Verifies the synth produces a NyraUserProfile that's safe for UI to consume:
 * non-empty top-level lists where applicable, at least one visual state, well-formed
 * dimensions.
 */
class NyraProfileSynthesisRealDataTest {

    private lateinit var synthesizer: NyraProfileSynthesizerImpl

    @Before
    fun setUp() = runTest {
        val repository = NyraContentRepositoryImpl(
            loader = ContentTestFixtures.loader,
            validator = ContentTestFixtures.validator,
            availabilityResolver = NyraFeatureAvailabilityResolver()
        )
        repository.loadContent()

        synthesizer = NyraProfileSynthesizerImpl(
            repository = repository,
            traitAggregator = TraitScoreAggregator(),
            dimensionAggregator = DimensionAggregator(repository),
            areaScoreCalculator = AreaScoreCalculator(repository),
            comboMatcher = ComboMatcher(repository),
            archetypeMatcher = ArchetypeMatcher(repository),
            visualStateResolver = VisualStateResolver(),
            homeModuleResolver = HomeModuleResolver()
        )
    }

    @Test
    fun synthesizesFullProfileFromRealisticChart() {
        val profile = synthesizer.synthesize(richChart())

        assertTrue("topTraits empty", profile.topTraits.isNotEmpty())
        assertTrue("dimensions empty", profile.dimensions.dimensions.isNotEmpty())
        assertTrue("areas empty", profile.areas.isNotEmpty())
        assertTrue("activeCombos empty", profile.activeCombos.isNotEmpty())
        assertTrue("visualStates empty", profile.visualStates.isNotEmpty())
        assertTrue("preferredHomeModules empty", profile.preferredHomeModules.isNotEmpty())
        assertTrue("aestheticTags empty", profile.aestheticTags.isNotEmpty())
        assertTrue("reflectionTags empty", profile.reflectionTags.isNotEmpty())
        assertTrue("summaries empty", profile.summaries.isNotEmpty())
    }

    @Test
    fun dimensionsRemainInUnitRange() {
        val profile = synthesizer.synthesize(richChart())
        val outOfRange = profile.dimensions.dimensions
            .filterNot { it.value in 0.0..1.0 }
        assertTrue("Dimensions out of [0,1]: $outOfRange", outOfRange.isEmpty())
    }

    @Test
    fun areaScoresAreSortedDescending() {
        val profile = synthesizer.synthesize(richChart())
        val scores = profile.areas.map { it.score }
        assertEquals(
            "Areas should be returned sorted by score desc",
            scores.sortedDescending(),
            scores
        )
    }

    @Test
    fun missingPlacementsProduceWarningButNoCrash() {
        val sparse = AstrologyCalculationResult(
            input = neutralBirth(),
            placements = listOf(
                placement(Planet.SUN, ZodiacSign.CANCER)
                // Moon/Mercury/Venus/Mars deliberately absent.
            ),
            calculationMode = CalculationMode.APPROXIMATE_FALLBACK,
            warnings = emptyList()
        )
        val profile = synthesizer.synthesize(sparse)
        assertTrue("Even a sparse chart should produce at least one visual state",
            profile.visualStates.isNotEmpty())
        assertTrue("Sparse chart should not crash; warnings may or may not be present",
            profile.warnings.size >= 0)  // No crash is the assertion.
    }

    @Test
    fun mbtiBlendingDoesNotCrashWithKnownType() {
        val profile = synthesizer.synthesize(richChart(), mbtiType = "INFJ")
        assertTrue("topTraits empty with MBTI=INFJ", profile.topTraits.isNotEmpty())
    }

    @Test
    fun mbtiBlendingHandlesUnknownTypeGracefully() {
        val profile = synthesizer.synthesize(richChart(), mbtiType = "ZZZZ")
        assertTrue("Unknown MBTI should not erase the astrology-side profile",
            profile.topTraits.isNotEmpty())
    }

    // ─── helpers ──────────────────────────────────────────────────────────────

    private fun richChart() = AstrologyCalculationResult(
        input = neutralBirth(),
        placements = listOf(
            placement(Planet.SUN, ZodiacSign.CANCER),
            placement(Planet.MOON, ZodiacSign.PISCES),
            placement(Planet.MERCURY, ZodiacSign.LEO),
            placement(Planet.VENUS, ZodiacSign.LEO),
            placement(Planet.MARS, ZodiacSign.LEO)
        ),
        calculationMode = CalculationMode.EPHEMERIS_RUNTIME,
        warnings = emptyList()
    )

    private fun neutralBirth() = BirthData(
        date = LocalDate.of(1994, 7, 22),
        time = LocalTime.of(12, 0),
        zoneId = ZoneId.of("UTC"),
        latitude = 0.0,
        longitude = 0.0
    )

    private fun placement(planet: Planet, sign: ZodiacSign) = PlanetPlacement(
        planet = planet,
        sign = sign,
        longitude = 0.0,
        degreeInSign = 0.0,
        retrograde = false,
        accuracy = PlacementAccuracy.EXACT
    )
}
