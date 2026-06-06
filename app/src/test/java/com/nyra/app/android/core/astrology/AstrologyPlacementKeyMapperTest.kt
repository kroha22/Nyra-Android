package com.nyra.app.android.core.astrology

import com.nyra.app.android.core.astrology.mapper.AstrologyPlacementKeyMapper
import com.nyra.app.android.core.astrology.model.PlacementAccuracy
import com.nyra.app.android.core.astrology.model.Planet
import com.nyra.app.android.core.astrology.model.PlanetPlacement
import com.nyra.app.android.core.astrology.model.ZodiacSign
import org.junit.Assert.assertEquals
import org.junit.Test

class AstrologyPlacementKeyMapperTest {

    private val mapper = AstrologyPlacementKeyMapper()

    @Test
    fun createsPlacementKeysForContentLookup() {
        assertEquals(
            "sun_cancer",
            mapper.toPlacementKey(placement(Planet.SUN, ZodiacSign.CANCER))
        )
        assertEquals(
            "moon_pisces",
            mapper.toPlacementKey(placement(Planet.MOON, ZodiacSign.PISCES))
        )
    }

    private fun placement(
        planet: Planet,
        sign: ZodiacSign
    ): PlanetPlacement =
        PlanetPlacement(
            planet = planet,
            sign = sign,
            longitude = 0.0,
            degreeInSign = 0.0,
            accuracy = PlacementAccuracy.EXACT
        )
}
