package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.CardTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM card_templates WHERE isActive = 1 ORDER BY priority DESC")
    fun getActiveCards(): Flow<List<CardTemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardTemplateEntity>)
}