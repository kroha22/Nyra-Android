package com.nyra.app.android.domain.journal.repository

import com.nyra.app.android.core.model.JournalEntry
import kotlinx.coroutines.flow.Flow

interface JournalRepository {
    fun observeJournalEntries(): Flow<List<JournalEntry>>
    fun observeJournalEntry(id: String): Flow<JournalEntry?>
    suspend fun getJournalEntry(id: String): JournalEntry?
    suspend fun saveJournalEntry(entry: JournalEntry)
    suspend fun deleteJournalEntry(id: String)
}