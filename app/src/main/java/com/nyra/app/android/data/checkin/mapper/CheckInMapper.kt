package com.nyra.app.android.data.checkin.mapper

import com.nyra.app.android.core.database.entity.CheckInEntryEntity
import com.nyra.app.android.core.model.CheckInEntry

fun CheckInEntryEntity.toDomain(): CheckInEntry = CheckInEntry(
    id = id,
    moodCode = moodCode,
    energyLevel = energyLevel,
    intensityLevel = intensityLevel,
    note = note,
    tags = tags,
    createdAt = createdAt,
    localDay = localDay,
    timeOfDay = timeOfDay,
    source = source
)

fun CheckInEntry.toEntity(): CheckInEntryEntity = CheckInEntryEntity(
    id = id,
    moodCode = moodCode,
    energyLevel = energyLevel,
    intensityLevel = intensityLevel,
    note = note,
    tags = tags,
    createdAt = createdAt,
    localDay = localDay,
    timeOfDay = timeOfDay,
    source = source
)