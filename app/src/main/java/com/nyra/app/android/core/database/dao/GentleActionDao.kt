package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.GentleActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GentleActionDao {
    @Query("SELECT * FROM gentle_actions WHERE isActive = 1")
    fun getActiveActions(): Flow<List<GentleActionEntity>>

    @Query("SELECT * FROM gentle_actions WHERE id = :id")
    suspend fun getActionById(id: String): GentleActionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActions(actions: List<GentleActionEntity>)
}