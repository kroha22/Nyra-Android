package com.nyra.app.android.core.model

import java.time.LocalDateTime

data class InsightSnapshot(
    val id: String,
    val periodType: InsightPeriodType,
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
    val checkInCount: Int,
    val journalCount: Int,
    val mostCommonMoodCode: MoodCode? = null,
    val averageEnergy: Float? = null,
    val averageIntensity: Float? = null,
    val moodDistribution: Map<MoodCode, Int> = emptyMap(),
    val dailyMoodMap: Map<String, MoodCode> = emptyMap(),
    val createdAt: LocalDateTime
)

enum class InsightPeriodType {
    Week,
    Month
}