package com.nyra.app.android.core.rules

import com.nyra.app.android.core.model.TimeOfDay
import java.time.LocalTime
import javax.inject.Inject

class TimeOfDayResolver @Inject constructor() {
    fun resolve(time: LocalTime = LocalTime.now()): TimeOfDay {
        return when (time.hour) {
            in 5..11 -> TimeOfDay.Morning
            in 12..16 -> TimeOfDay.Afternoon
            in 17..21 -> TimeOfDay.Evening
            else -> TimeOfDay.Night
        }
    }
}