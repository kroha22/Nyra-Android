package com.nyra.app.android.domain.journal.usecase

import com.nyra.app.android.domain.journal.repository.JournalRepository
import javax.inject.Inject

class DeleteJournalEntryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteJournalEntry(id)
    }
}