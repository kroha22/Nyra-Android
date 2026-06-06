package com.nyra.app.android.core.astrology.mapper

import com.nyra.app.android.core.astrology.model.ZodiacSign
import javax.inject.Inject
import kotlin.math.floor

data class ZodiacLongitude(
    val sign: ZodiacSign,
    val normalizedLongitude: Double,
    val degreeInSign: Double
)

class ZodiacLongitudeMapper @Inject constructor() {

    fun map(longitude: Double): ZodiacLongitude {
        val normalized = normalize(longitude)
        val signIndex = floor(normalized / SIGN_DEGREES).toInt()
        val degreeInSign = normalized - signIndex * SIGN_DEGREES
        return ZodiacLongitude(
            sign = ZodiacSign.entries[signIndex],
            normalizedLongitude = normalized,
            degreeInSign = degreeInSign
        )
    }

    fun normalize(longitude: Double): Double =
        ((longitude % FULL_CIRCLE_DEGREES) + FULL_CIRCLE_DEGREES) % FULL_CIRCLE_DEGREES

    private companion object {
        const val SIGN_DEGREES = 30.0
        const val FULL_CIRCLE_DEGREES = 360.0
    }
}
