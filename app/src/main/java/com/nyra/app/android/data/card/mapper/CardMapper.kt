package com.nyra.app.android.data.card.mapper

import com.nyra.app.android.core.database.entity.CardActionEntity
import com.nyra.app.android.core.database.entity.CardTemplateEntity
import com.nyra.app.android.core.model.CardAction
import com.nyra.app.android.core.model.CardTemplate

fun CardTemplateEntity.toDomain(): CardTemplate = CardTemplate(
    id = id,
    type = type,
    title = title,
    subtitle = subtitle,
    iconToken = iconToken,
    visualToken = visualToken,
    action = action.toDomain(),
    priority = priority,
    isActive = isActive
)

fun CardTemplate.toEntity(): CardTemplateEntity = CardTemplateEntity(
    id = id,
    type = type,
    title = title,
    subtitle = subtitle,
    iconToken = iconToken,
    visualToken = visualToken,
    action = action.toEntity(),
    priority = priority,
    isActive = isActive
)

fun CardActionEntity.toDomain(): CardAction = CardAction(
    type = type,
    payload = payload
)

fun CardAction.toEntity(): CardActionEntity = CardActionEntity(
    type = type,
    payload = payload
)