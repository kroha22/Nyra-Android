package com.nyra.app.android.data.insight.mapper

import com.nyra.app.android.core.database.entity.InsightSnapshotEntity
import com.nyra.app.android.core.model.InsightSnapshot

fun InsightSnapshotEntity.toDomain(): InsightSnapshot = InsightSnapshot(
    id = id,
    periodType = periodType,
    periodStart = periodStart,
    periodEnd = periodEnd,
    checkInCount = checkInCount,
    journalCount = journalCount,
    mostCommonMoodCode = mostCommonMoodCode,
    averageEnergy = averageEnergy,
    averageIntensity = averageIntensity,
    moodDistribution = moodDistribution,
    dailyMoodMap = dailyMoodMap,
    createdAt = createdAt
)

fun InsightSnapshot.toEntity(): InsightSnapshotEntity = InsightSnapshotEntity(
    id = id,
    periodType = periodType,
    periodStart = periodStart,
    periodEnd = periodEnd,
    checkInCount = checkInCount,
    journalCount = journalCount,
    mostCommonMoodCode = mostCommonMoodCode,
    averageEnergy = averageEnergy,
    averageIntensity = averageIntensity,
    moodDistribution = moodDistribution,
    dailyMoodMap = dailyMoodMap,
    createdAt = createdAt
)