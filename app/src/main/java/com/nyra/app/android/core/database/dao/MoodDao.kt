package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.MoodStateEntity
import com.nyra.app.android.core.database.entity.PresenceStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_states WHERE isActive = 1")
    fun getActiveMoodStates(): Flow<List<MoodStateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoodStates(moods: List<MoodStateEntity>)

    @Query("SELECT * FROM presence_states")
    fun getPresenceStates(): Flow<List<PresenceStateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresenceStates(states: List<PresenceStateEntity>)
}