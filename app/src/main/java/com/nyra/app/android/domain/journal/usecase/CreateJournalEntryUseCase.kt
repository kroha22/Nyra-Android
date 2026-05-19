package com.nyra.app.android.domain.journal.usecase

import com.nyra.app.android.core.model.JournalEntry
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.domain.journal.repository.JournalRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

data class CreateJournalParams(
    val title: String? = null,
    val body: String,
    val linkedCheckInId: String? = null,
    val moodCode: MoodCode? = null,
    val tags: List<String> = emptyList()
)

class CreateJournalEntryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(params: CreateJournalParams) {
        val now = LocalDateTime.now()
        val entry = JournalEntry(
            id = UUID.randomUUID().toString(),
            title = params.title,
            body = params.body,
            linkedCheckInId = params.linkedCheckInId,
            moodCode = params.moodCode,
            tags = params.tags,
            createdAt = now,
            updatedAt = now,
            localDay = LocalDate.now()
        )
        repository.saveJournalEntry(entry)
    }
}