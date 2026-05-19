package com.nyra.app.android.core.database.dao

import androidx.room.*
import com.nyra.app.android.core.database.entity.InsightSnapshotEntity
import com.nyra.app.android.core.model.InsightPeriodType
import kotlinx.coroutines.flow.Flow

@Dao
interface InsightDao {
    @Query("SELECT * FROM insight_snapshots WHERE periodType = :periodType ORDER BY periodEnd DESC LIMIT 1")
    fun getLatestInsight(periodType: InsightPeriodType): Flow<InsightSnapshotEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsight(insight: InsightSnapshotEntity)

    @Query("DELETE FROM insight_snapshots WHERE periodEnd < :timestamp")
    suspend fun deleteOldInsights(timestamp: String)
}