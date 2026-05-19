package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.NotificationScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification_schedules")
    fun getSchedules(): Flow<List<NotificationScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<NotificationScheduleEntity>)

    @Query("UPDATE notification_schedules SET enabled = :enabled WHERE id = :id")
    suspend fun updateScheduleEnabled(id: String, enabled: Boolean)
}