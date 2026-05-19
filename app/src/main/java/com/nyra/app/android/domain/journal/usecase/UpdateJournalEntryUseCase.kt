package com.nyra.app.android.domain.journal.usecase

import com.nyra.app.android.core.model.JournalEntry
import com.nyra.app.android.domain.journal.repository.JournalRepository
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateJournalEntryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(entry: JournalEntry) {
        val updatedEntry = entry.copy(updatedAt = LocalDateTime.now())
        repository.saveJournalEntry(updatedEntry)
    }
}