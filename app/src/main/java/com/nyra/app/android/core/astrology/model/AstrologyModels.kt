package com.nyra.app.android.core.astrology.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

data class BirthData(
    val date: LocalDate,
    val time: LocalTime?,
    val zoneId: ZoneId?,
    val latitude: Double?,
    val longitude: Double?
)

data class PlanetPlacement(
    val planet: Planet,
    val sign: ZodiacSign,
    val longitude: Double,
    val degreeInSign: Double,
    val retrograde: Boolean? = null,
    val accuracy: PlacementAccuracy
)

enum class Planet {
    SUN,
    MOON,
    MERCURY,
    VENUS,
    MARS
}

enum class ZodiacSign {
    ARIES,
    TAURUS,
    GEMINI,
    CANCER,
    LEO,
    VIRGO,
    LIBRA,
    SCORPIO,
    SAGITTARIUS,
    CAPRICORN,
    AQUARIUS,
    PISCES
}

enum class PlacementAccuracy {
    EXACT,
    APPROXIMATE_TIME,
    APPROXIMATE_TIMEZONE,
    APPROXIMATE_MOON
}

data class AstrologyCalculationResult(
    val input: BirthData,
    val placements: List<PlanetPlacement>,
    val calculationMode: CalculationMode,
    val warnings: List<String>
)

enum class CalculationMode {
    EPHEMERIS_RUNTIME,
    LOCAL_TABLE_LOOKUP,
    APPROXIMATE_FALLBACK
}
