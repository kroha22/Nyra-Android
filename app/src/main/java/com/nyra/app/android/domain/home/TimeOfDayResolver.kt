package com.nyra.app.android.domain.home

import com.nyra.app.android.core.model.TimeOfDay
import java.time.LocalTime
import javax.inject.Inject

class TimeOfDayResolver @Inject constructor() {

    fun resolve(now: LocalTime = LocalTime.now()): TimeOfDay {
        return when (now.hour) {
            in 5..11 -> TimeOfDay.Morning
            in 12..16 -> TimeOfDay.Afternoon
            in 17..21 -> TimeOfDay.Evening
            else -> TimeOfDay.Night
        }
    }
}