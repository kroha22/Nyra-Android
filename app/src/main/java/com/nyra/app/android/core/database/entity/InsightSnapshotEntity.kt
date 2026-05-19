package com.nyra.app.android.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nyra.app.android.core.model.InsightPeriodType
import com.nyra.app.android.core.model.MoodCode
import java.time.LocalDateTime

@Entity(tableName = "insight_snapshots")
data class InsightSnapshotEntity(
    @PrimaryKey val id: String,
    val periodType: InsightPeriodType,
    val periodStart: LocalDateTime,
    val periodEnd: LocalDateTime,
    val checkInCount: Int,
    val journalCount: Int,
    val mostCommonMoodCode: MoodCode?,
    val averageEnergy: Float?,
    val averageIntensity: Float?,
    val moodDistribution: Map<MoodCode, Int>,
    val dailyMoodMap: Map<String, MoodCode>,
    val createdAt: LocalDateTime
)