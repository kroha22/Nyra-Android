package com.nyra.app.android.data.mood.mapper

import com.nyra.app.android.core.database.entity.MoodStateEntity
import com.nyra.app.android.core.database.entity.PresenceStateEntity
import com.nyra.app.android.core.model.MoodState
import com.nyra.app.android.core.model.PresenceState

fun MoodStateEntity.toDomain(): MoodState = MoodState(
    id = id,
    code = code,
    title = title,
    description = description,
    valence = valence,
    energy = energy,
    intensity = intensity,
    presenceCode = presenceCode,
    isActive = isActive
)

fun MoodState.toEntity(): MoodStateEntity = MoodStateEntity(
    id = id,
    code = code,
    title = title,
    description = description,
    valence = valence,
    energy = energy,
    intensity = intensity,
    presenceCode = presenceCode,
    isActive = isActive
)

fun PresenceStateEntity.toDomain(): PresenceState = PresenceState(
    id = id,
    code = code,
    title = title,
    description = description,
    shapeToken = shapeToken,
    glowToken = glowToken,
    textureToken = textureToken,
    motionToken = motionToken,
    backgroundToken = backgroundToken,
    brightness = brightness,
    density = density,
    expansion = expansion,
    warmth = warmth
)

fun PresenceState.toEntity(): PresenceStateEntity = PresenceStateEntity(
    id = id,
    code = code,
    title = title,
    description = description,
    shapeToken = shapeToken,
    glowToken = glowToken,
    textureToken = textureToken,
    motionToken = motionToken,
    backgroundToken = backgroundToken,
    brightness = brightness,
    density = density,
    expansion = expansion,
    warmth = warmth
)