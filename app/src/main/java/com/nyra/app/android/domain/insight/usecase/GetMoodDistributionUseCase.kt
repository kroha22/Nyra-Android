package com.nyra.app.android.domain.insight.usecase

import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.domain.insight.repository.InsightRepository
import com.nyra.app.android.core.model.InsightPeriodType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMoodDistributionUseCase @Inject constructor(
    private val repository: InsightRepository
) {
    operator fun invoke(): Flow<Map<MoodCode, Int>> {
        return repository.observeLatestInsight(InsightPeriodType.Week).map {
            it?.moodDistribution ?: emptyMap()
        }
    }
}