package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.JournalEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries WHERE localDay = :date AND isDeleted = 0")
    fun getJournalEntriesByDay(date: LocalDate): Flow<List<JournalEntryEntity>>

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getJournalEntryById(id: String): JournalEntryEntity?

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    fun observeJournalEntryById(id: String): Flow<JournalEntryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(entry: JournalEntryEntity)

    @Query("UPDATE journal_entries SET isDeleted = 1 WHERE id = :id")
    suspend fun deleteJournalEntryById(id: String)
}