package com.nyra.app.android.domain.insight.usecase

import com.nyra.app.android.core.model.InsightPeriodType
import com.nyra.app.android.core.model.InsightSnapshot
import com.nyra.app.android.domain.insight.repository.InsightRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeeklyInsightUseCase @Inject constructor(
    private val repository: InsightRepository
) {
    operator fun invoke(): Flow<InsightSnapshot?> {
        return repository.observeLatestInsight(InsightPeriodType.Week)
    }
}