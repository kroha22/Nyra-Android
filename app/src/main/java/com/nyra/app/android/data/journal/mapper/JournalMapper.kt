package com.nyra.app.android.data.journal.mapper

import com.nyra.app.android.core.database.entity.JournalEntryEntity
import com.nyra.app.android.core.model.JournalEntry

fun JournalEntryEntity.toDomain(): JournalEntry = JournalEntry(
    id = id,
    title = title,
    body = body,
    linkedCheckInId = linkedCheckInId,
    moodCode = moodCode,
    tags = tags,
    createdAt = createdAt,
    updatedAt = updatedAt,
    localDay = localDay,
    isArchived = isArchived,
    isDeleted = isDeleted
)

fun JournalEntry.toEntity(): JournalEntryEntity = JournalEntryEntity(
    id = id,
    title = title,
    body = body,
    linkedCheckInId = linkedCheckInId,
    moodCode = moodCode,
    tags = tags,
    createdAt = createdAt,
    updatedAt = updatedAt,
    localDay = localDay,
    isArchived = isArchived,
    isDeleted = isDeleted
)