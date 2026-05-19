package com.nyra.app.android.data.gentleaction.mapper

import com.nyra.app.android.core.database.entity.GentleActionEntity
import com.nyra.app.android.core.model.GentleAction

fun GentleActionEntity.toDomain(): GentleAction = GentleAction(
    id = id,
    title = title,
    description = description,
    durationSeconds = durationSeconds,
    category = category,
    suitableMoodCodes = suitableMoodCodes,
    unsuitableMoodCodes = unsuitableMoodCodes,
    timeOfDay = timeOfDay,
    intensity = intensity,
    isActive = isActive
)

fun GentleAction.toEntity(): GentleActionEntity = GentleActionEntity(
    id = id,
    title = title,
    description = description,
    durationSeconds = durationSeconds,
    category = category,
    suitableMoodCodes = suitableMoodCodes,
    unsuitableMoodCodes = unsuitableMoodCodes,
    timeOfDay = timeOfDay,
    intensity = intensity,
    isActive = isActive
)