package com.nyra.app.android.data.insight.repository

import com.nyra.app.android.core.database.dao.InsightDao
import com.nyra.app.android.core.model.InsightPeriodType
import com.nyra.app.android.core.model.InsightSnapshot
import com.nyra.app.android.data.insight.mapper.toDomain
import com.nyra.app.android.data.insight.mapper.toEntity
import com.nyra.app.android.domain.insight.repository.InsightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsightRepositoryImpl @Inject constructor(
    private val dao: InsightDao
) : InsightRepository {

    override fun observeLatestInsight(periodType: InsightPeriodType): Flow<InsightSnapshot?> {
        return dao.getLatestInsight(periodType).map { it?.toDomain() }
    }

    override suspend fun saveInsight(insight: InsightSnapshot) {
        dao.insertInsight(insight.toEntity())
    }

    override suspend fun deleteOldInsights(beforeTimestamp: String) {
        dao.deleteOldInsights(beforeTimestamp)
    }
}