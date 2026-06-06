package com.nyra.app.android.core.profile.synthesis

internal fun Double.coerceUnit(): Double = coerceIn(0.0, 1.0)

internal fun Map<String, Double>.normalizedScores(): Map<String, Double> {
    val max = values.maxOrNull() ?: return emptyMap()
    if (max <= 0.0) return keys.associateWith { 0.0 }
    return mapValues { (_, value) -> (value / max).coerceUnit() }
}

internal fun Map<String, Double>.weightedMean(): Double {
    if (isEmpty()) return 0.0
    return values.average().coerceUnit()
}

internal fun Iterable<String>.stableDistinct(): List<String> =
    fold(mutableListOf()) { acc, value ->
        if (value.isNotBlank() && value !in acc) acc += value
        acc
    }
