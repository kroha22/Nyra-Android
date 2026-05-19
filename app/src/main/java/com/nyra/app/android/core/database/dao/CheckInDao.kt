package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.CheckInEntryEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CheckInDao {
    @Query("SELECT * FROM check_in_entries ORDER BY createdAt DESC")
    fun getAllCheckIns(): Flow<List<CheckInEntryEntity>>

    @Query("SELECT * FROM check_in_entries ORDER BY createdAt DESC LIMIT 1")
    fun getLatestCheckIn(): Flow<CheckInEntryEntity?>

    @Query("SELECT * FROM check_in_entries WHERE localDay = :date ORDER BY createdAt DESC")
    fun getCheckInsByDay(date: LocalDate): Flow<List<CheckInEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(entry: CheckInEntryEntity)

    @Delete
    suspend fun deleteCheckIn(entry: CheckInEntryEntity)
}