package com.nyra.app.android.domain.insight.repository

import com.nyra.app.android.core.model.InsightPeriodType
import com.nyra.app.android.core.model.InsightSnapshot
import kotlinx.coroutines.flow.Flow

interface InsightRepository {
    fun observeLatestInsight(periodType: InsightPeriodType): Flow<InsightSnapshot?>
    suspend fun saveInsight(insight: InsightSnapshot)
    suspend fun deleteOldInsights(beforeTimestamp: String)
}