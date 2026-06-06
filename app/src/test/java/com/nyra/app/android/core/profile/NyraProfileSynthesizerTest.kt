package com.nyra.app.android.core.profile

import com.nyra.app.android.core.astrology.model.AstrologyCalculationResult
import com.nyra.app.android.core.astrology.model.BirthData
import com.nyra.app.android.core.astrology.model.CalculationMode
import com.nyra.app.android.core.astrology.model.PlacementAccuracy
import com.nyra.app.android.core.astrology.model.Planet
import com.nyra.app.android.core.astrology.model.PlanetPlacement
import com.nyra.app.android.core.astrology.model.ZodiacSign
import com.nyra.app.android.core.profile.matcher.ArchetypeMatcher
import com.nyra.app.android.core.profile.matcher.ComboMatcher
import com.nyra.app.android.core.profile.resolver.HomeModuleResolver
import com.nyra.app.android.core.profile.resolver.VisualStateResolver
import com.nyra.app.android.core.profile.synthesis.AreaScoreCalculator
import com.nyra.app.android.core.profile.synthesis.DimensionAggregator
import com.nyra.app.android.core.profile.synthesis.NyraProfileSynthesizerImpl
import com.nyra.app.android.core.profile.synthesis.TraitScoreAggregator
import java.time.LocalDate
import org.junit.Assert.assertTrue
import org.junit.Test

class NyraProfileSynthesizerTest {

    @Test
    fun skipsMissingPlacementContentAndAddsWarning() {
        val repository = FakeNyraContentRepository()
        val synthesizer = NyraProfileSynthesizerImpl(
            repository = repository,
            traitAggregator = TraitScoreAggregator(),
            dimensionAggregator = DimensionAggregator(repository),
            areaScoreCalculator = AreaScoreCalculator(repository),
            comboMatcher = ComboMatcher(repository),
            archetypeMatcher = ArchetypeMatcher(repository),
            visualStateResolver = VisualStateResolver(),
            homeModuleResolver = HomeModuleResolver()
        )

        val profile = synthesizer.synthesizeFromAstrology(
            AstrologyCalculationResult(
                input = BirthData(
                    date = LocalDate.of(1994, 7, 22),
                    time = null,
                    zoneId = null,
                    latitude = null,
                    longitude = null
                ),
                placements = listOf(
                    PlanetPlacement(
                        planet = Planet.SUN,
                        sign = ZodiacSign.CANCER,
                        longitude = 119.0,
                        degreeInSign = 29.0,
                        accuracy = PlacementAccuracy.EXACT
                    )
                ),
                calculationMode = CalculationMode.APPROXIMATE_FALLBACK,
                warnings = emptyList()
            )
        )

        assertTrue(profile.warnings.any { it.contains("Missing astrology content record for sun_cancer") })
        assertTrue(profile.visualStates.isNotEmpty())
    }
}
