package com.nyra.app.android.core.astrology

import com.nyra.app.android.core.astrology.calculator.ApproximateEphemerisAstrologyPositionCalculator
import com.nyra.app.android.core.astrology.mapper.ZodiacLongitudeMapper
import com.nyra.app.android.core.astrology.model.BirthData
import com.nyra.app.android.core.astrology.model.PlacementAccuracy
import com.nyra.app.android.core.astrology.model.Planet
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AstrologyPositionCalculatorTest {

    private val calculator = ApproximateEphemerisAstrologyPositionCalculator(
        longitudeMapper = ZodiacLongitudeMapper()
    )

    @Test
    fun returnsExactlyFivePlanetPlacements() {
        val result = calculator.calculateFivePlanetPlacements(
            BirthData(
                date = LocalDate.of(1994, 7, 22),
                time = LocalTime.of(9, 30),
                zoneId = ZoneId.of("UTC"),
                latitude = null,
                longitude = null
            )
        )

        assertEquals(5, result.placements.size)
        assertEquals(
            listOf(Planet.SUN, Planet.MOON, Planet.MERCURY, Planet.VENUS, Planet.MARS),
            result.placements.map { it.planet }
        )
    }

    @Test
    fun marksMoonApproximateWhenBirthTimeIsMissing() {
        val result = calculator.calculateFivePlanetPlacements(
            BirthData(
                date = LocalDate.of(1994, 7, 22),
                time = null,
                zoneId = ZoneId.of("UTC"),
                latitude = 38.7223,
                longitude = -9.1393
            )
        )

        val moon = result.placements.first { it.planet == Planet.MOON }
        assertEquals(PlacementAccuracy.APPROXIMATE_MOON, moon.accuracy)
        assertTrue(result.warnings.contains("Moon placement may be inaccurate without birth time."))
    }
}
