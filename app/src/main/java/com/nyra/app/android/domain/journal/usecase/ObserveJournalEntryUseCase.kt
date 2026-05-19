package com.nyra.app.android.domain.journal.usecase

import com.nyra.app.android.core.model.JournalEntry
import com.nyra.app.android.domain.journal.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveJournalEntryUseCase @Inject constructor(
    private val repository: JournalRepository
) {
    operator fun invoke(id: String): Flow<JournalEntry?> = repository.observeJournalEntry(id)
}