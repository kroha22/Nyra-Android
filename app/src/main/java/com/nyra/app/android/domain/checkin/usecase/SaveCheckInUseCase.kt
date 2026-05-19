package com.nyra.app.android.domain.checkin.usecase

import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.CheckInSource
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.rules.TimeOfDayResolver
import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

data class SaveCheckInParams(
    val moodCode: MoodCode,
    val energyLevel: Int,
    val intensityLevel: Int,
    val note: String? = null,
    val tags: List<String> = emptyList(),
    val source: CheckInSource = CheckInSource.Manual
)

class SaveCheckInUseCase @Inject constructor(
    private val repository: CheckInRepository,
    private val timeOfDayResolver: TimeOfDayResolver
) {
    suspend operator fun invoke(params: SaveCheckInParams) {
        val now = LocalDateTime.now()
        val entry = CheckInEntry(
            id = UUID.randomUUID().toString(),
            moodCode = params.moodCode,
            energyLevel = params.energyLevel,
            intensityLevel = params.intensityLevel,
            note = params.note,
            tags = params.tags,
            createdAt = now,
            localDay = LocalDate.now(),
            timeOfDay = timeOfDayResolver.resolve(now.toLocalTime()),
            source = params.source
        )
        repository.saveCheckIn(entry)
    }
}