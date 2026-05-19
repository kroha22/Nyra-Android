package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferenceDao {
    @Query("SELECT * FROM user_preferences WHERE id = 0")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUserPreferences(preferences: UserPreferencesEntity)
}