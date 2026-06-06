package com.nyra.app.android.core.astrology

import com.nyra.app.android.core.astrology.mapper.ZodiacLongitudeMapper
import com.nyra.app.android.core.astrology.model.ZodiacSign
import org.junit.Assert.assertEquals
import org.junit.Test

class ZodiacLongitudeMapperTest {

    private val mapper = ZodiacLongitudeMapper()

    @Test
    fun mapsLongitudeBoundariesToZodiacSigns() {
        assertMapping(0.0, ZodiacSign.ARIES, 0.0)
        assertMapping(29.999, ZodiacSign.ARIES, 29.999)
        assertMapping(30.0, ZodiacSign.TAURUS, 0.0)
        assertMapping(119.999, ZodiacSign.CANCER, 29.999)
        assertMapping(120.0, ZodiacSign.LEO, 0.0)
        assertMapping(359.999, ZodiacSign.PISCES, 29.999)
    }

    private fun assertMapping(
        longitude: Double,
        expectedSign: ZodiacSign,
        expectedDegreeInSign: Double
    ) {
        val result = mapper.map(longitude)

        assertEquals(expectedSign, result.sign)
        assertEquals(expectedDegreeInSign, result.degreeInSign, 0.0001)
    }
}
