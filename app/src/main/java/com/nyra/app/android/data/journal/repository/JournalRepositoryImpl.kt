package com.nyra.app.android.data.journal.repository

import com.nyra.app.android.core.database.dao.JournalDao
import com.nyra.app.android.core.model.JournalEntry
import com.nyra.app.android.data.journal.mapper.toDomain
import com.nyra.app.android.data.journal.mapper.toEntity
import com.nyra.app.android.domain.journal.repository.JournalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JournalRepositoryImpl @Inject constructor(
    private val dao: JournalDao
) : JournalRepository {

    override fun observeJournalEntries(): Flow<List<JournalEntry>> {
        return dao.getAllJournalEntries().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun observeJournalEntry(id: String): Flow<JournalEntry?> {
        // Room provides reactive flows for queries. 
        // We'll need to add a specific query to JournalDao for Flow<JournalEntry?>
        return dao.observeJournalEntryById(id).map { it?.toDomain() }
    }

    override suspend fun getJournalEntry(id: String): JournalEntry? {
        return dao.getJournalEntryById(id)?.toDomain()
    }

    override suspend fun saveJournalEntry(entry: JournalEntry) {
        dao.insertJournalEntry(entry.toEntity())
    }

    override suspend fun deleteJournalEntry(id: String) {
        dao.deleteJournalEntryById(id)
    }
}