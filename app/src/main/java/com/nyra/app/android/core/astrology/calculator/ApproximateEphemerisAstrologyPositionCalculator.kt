package com.nyra.app.android.core.astrology.calculator

import com.nyra.app.android.core.astrology.mapper.ZodiacLongitudeMapper
import com.nyra.app.android.core.astrology.model.AstrologyCalculationResult
import com.nyra.app.android.core.astrology.model.BirthData
import com.nyra.app.android.core.astrology.model.CalculationMode
import com.nyra.app.android.core.astrology.model.PlacementAccuracy
import com.nyra.app.android.core.astrology.model.Planet
import com.nyra.app.android.core.astrology.model.PlanetPlacement
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ApproximateEphemerisAstrologyPositionCalculator @Inject constructor(
    private val longitudeMapper: ZodiacLongitudeMapper
) : AstrologyPositionCalculator {

    override fun calculateFivePlanetPlacements(
        birthData: BirthData
    ): AstrologyCalculationResult {
        val warnings = buildWarnings(birthData)
        val julianDay = birthData.toJulianDay()
        val accuracy = birthData.baseAccuracy()

        val placements = Planet.entries.map { planet ->
            val longitude = when (planet) {
                Planet.SUN -> sunLongitude(julianDay)
                Planet.MOON -> moonLongitude(julianDay)
                Planet.MERCURY -> planetLongitude(julianDay, Planet.MERCURY)
                Planet.VENUS -> planetLongitude(julianDay, Planet.VENUS)
                Planet.MARS -> planetLongitude(julianDay, Planet.MARS)
            }
            val zodiac = longitudeMapper.map(longitude)
            PlanetPlacement(
                planet = planet,
                sign = zodiac.sign,
                longitude = zodiac.normalizedLongitude,
                degreeInSign = zodiac.degreeInSign,
                retrograde = null,
                accuracy = if (planet == Planet.MOON && birthData.time == null) {
                    PlacementAccuracy.APPROXIMATE_MOON
                } else {
                    accuracy
                }
            )
        }

        return AstrologyCalculationResult(
            input = birthData,
            placements = placements,
            calculationMode = CalculationMode.APPROXIMATE_FALLBACK,
            warnings = warnings
        )
    }

    private fun BirthData.toJulianDay(): Double {
        val localTime = time ?: DEFAULT_BIRTH_TIME
        val zone = zoneId ?: ZoneId.of("UTC")
        val instant = date
            .atTime(localTime)
            .atZone(zone)
            .toInstant()
        return instant.toEpochMilli() / MILLIS_PER_DAY + UNIX_EPOCH_JULIAN_DAY
    }

    private fun BirthData.baseAccuracy(): PlacementAccuracy =
        when {
            time == null -> PlacementAccuracy.APPROXIMATE_TIME
            zoneId == null -> PlacementAccuracy.APPROXIMATE_TIMEZONE
            else -> PlacementAccuracy.EXACT
        }

    private fun buildWarnings(birthData: BirthData): List<String> =
        buildList {
            if (birthData.time == null) {
                add("Birth time is missing; 12:00 local time was used.")
                add("Moon placement may be inaccurate without birth time.")
            }
            if (birthData.zoneId == null) {
                add("Timezone is missing; UTC was used for planet sign calculation.")
            }
            if (birthData.latitude == null || birthData.longitude == null) {
                add("Location is not required for planet signs, but will be needed for ascendant and houses later.")
            }
        }

    private fun sunLongitude(julianDay: Double): Double {
        val days = daysSinceJ2000(julianDay)
        val meanLongitude = normalize(280.460 + 0.9856474 * days)
        val meanAnomaly = normalize(357.528 + 0.9856003 * days)
        return normalize(
            meanLongitude +
                1.915 * sinDeg(meanAnomaly) +
                0.020 * sinDeg(2.0 * meanAnomaly)
        )
    }

    private fun moonLongitude(julianDay: Double): Double {
        val days = daysSinceJ2000(julianDay)
        val elements = OrbitalElements(
            ascendingNode = normalize(125.1228 - 0.0529538083 * days),
            inclination = 5.1454,
            perihelion = normalize(318.0634 + 0.1643573223 * days),
            semiMajorAxis = 60.2666,
            eccentricity = 0.054900,
            meanAnomaly = normalize(115.3654 + 13.0649929509 * days)
        )
        return normalize(elements.toEclipticCoordinates().longitude)
    }

    private fun planetLongitude(julianDay: Double, planet: Planet): Double {
        val days = daysSinceJ2000(julianDay)
        val earth = earthElements(days).toEclipticCoordinates()
        val planetCoordinates = when (planet) {
            Planet.MERCURY -> mercuryElements(days)
            Planet.VENUS -> venusElements(days)
            Planet.MARS -> marsElements(days)
            Planet.SUN,
            Planet.MOON -> error("Only Mercury, Venus, and Mars use heliocentric planet longitude")
        }.toEclipticCoordinates()

        return normalize(
            atan2Deg(
                planetCoordinates.y - earth.y,
                planetCoordinates.x - earth.x
            )
        )
    }

    private fun earthElements(days: Double): OrbitalElements =
        OrbitalElements(
            ascendingNode = 0.0,
            inclination = 0.0,
            perihelion = normalize(282.9404 + 0.0000470935 * days),
            semiMajorAxis = 1.000000,
            eccentricity = 0.016709 - 0.000000001151 * days,
            meanAnomaly = normalize(356.0470 + 0.9856002585 * days)
        )

    private fun mercuryElements(days: Double): OrbitalElements =
        OrbitalElements(
            ascendingNode = normalize(48.3313 + 0.0000324587 * days),
            inclination = 7.0047 + 0.0000000500 * days,
            perihelion = normalize(29.1241 + 0.0000101444 * days),
            semiMajorAxis = 0.387098,
            eccentricity = 0.205635 + 0.000000000559 * days,
            meanAnomaly = normalize(168.6562 + 4.0923344368 * days)
        )

    private fun venusElements(days: Double): OrbitalElements =
        OrbitalElements(
            ascendingNode = normalize(76.6799 + 0.0000246590 * days),
            inclination = 3.3946 + 0.0000000275 * days,
            perihelion = normalize(54.8910 + 0.0000138374 * days),
            semiMajorAxis = 0.723330,
            eccentricity = 0.006773 - 0.000000001302 * days,
            meanAnomaly = normalize(48.0052 + 1.6021302244 * days)
        )

    private fun marsElements(days: Double): OrbitalElements =
        OrbitalElements(
            ascendingNode = normalize(49.5574 + 0.0000211081 * days),
            inclination = 1.8497 - 0.0000000178 * days,
            perihelion = normalize(286.5016 + 0.0000292961 * days),
            semiMajorAxis = 1.523688,
            eccentricity = 0.093405 + 0.000000002516 * days,
            meanAnomaly = normalize(18.6021 + 0.5240207766 * days)
        )

    private data class OrbitalElements(
        val ascendingNode: Double,
        val inclination: Double,
        val perihelion: Double,
        val semiMajorAxis: Double,
        val eccentricity: Double,
        val meanAnomaly: Double
    ) {
        fun toEclipticCoordinates(): EclipticCoordinates {
            val eccentricAnomaly = solveEccentricAnomaly(meanAnomaly, eccentricity)
            val xv = semiMajorAxis * (cosDeg(eccentricAnomaly) - eccentricity)
            val yv = semiMajorAxis *
                (sqrt(1.0 - eccentricity * eccentricity) * sinDeg(eccentricAnomaly))
            val trueAnomaly = atan2Deg(yv, xv)
            val radius = sqrt(xv * xv + yv * yv)
            val argument = trueAnomaly + perihelion

            val x = radius *
                (cosDeg(ascendingNode) * cosDeg(argument) -
                    sinDeg(ascendingNode) * sinDeg(argument) * cosDeg(inclination))
            val y = radius *
                (sinDeg(ascendingNode) * cosDeg(argument) +
                    cosDeg(ascendingNode) * sinDeg(argument) * cosDeg(inclination))
            val longitude = atan2Deg(y, x)
            return EclipticCoordinates(x = x, y = y, longitude = longitude)
        }

        private fun solveEccentricAnomaly(meanAnomaly: Double, eccentricity: Double): Double {
            var eccentricAnomaly = meanAnomaly +
                RAD_TO_DEG * eccentricity * sinDeg(meanAnomaly) *
                (1.0 + eccentricity * cosDeg(meanAnomaly))
            repeat(5) {
                val delta = (
                    eccentricAnomaly -
                        RAD_TO_DEG * eccentricity * sinDeg(eccentricAnomaly) -
                        meanAnomaly
                    ) / (1.0 - eccentricity * cosDeg(eccentricAnomaly))
                eccentricAnomaly -= delta
            }
            return eccentricAnomaly
        }
    }

    private data class EclipticCoordinates(
        val x: Double,
        val y: Double,
        val longitude: Double
    )

    private companion object {
        val DEFAULT_BIRTH_TIME: LocalTime = LocalTime.NOON
        const val UNIX_EPOCH_JULIAN_DAY = 2440587.5
        const val MILLIS_PER_DAY = 86_400_000.0
        const val J2000_JULIAN_DAY = 2451545.0
        const val DEG_TO_RAD = PI / 180.0
        const val RAD_TO_DEG = 180.0 / PI

        fun daysSinceJ2000(julianDay: Double): Double = julianDay - J2000_JULIAN_DAY

        fun normalize(degrees: Double): Double = ((degrees % 360.0) + 360.0) % 360.0

        fun sinDeg(degrees: Double): Double = sin(degrees * DEG_TO_RAD)

        fun cosDeg(degrees: Double): Double = cos(degrees * DEG_TO_RAD)

        fun atan2Deg(y: Double, x: Double): Double = atan2(y, x) * RAD_TO_DEG
    }
}
