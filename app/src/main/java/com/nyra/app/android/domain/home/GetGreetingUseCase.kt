package com.nyra.app.android.domain.home

import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.TimeOfDay
import javax.inject.Inject

class GetGreetingUseCase @Inject constructor() {

    operator fun invoke(
        lastCheckIn: CheckInEntry?,
        timeOfDay: TimeOfDay
    ): Pair<String, String?> {
        return when {
            lastCheckIn == null -> {
                "Welcome to Nyra" to "A quiet space is here when you need it."
            }

            timeOfDay == TimeOfDay.Morning -> {
                "Good morning" to "Start softly. Nothing needs to rush."
            }

            timeOfDay == TimeOfDay.Afternoon -> {
                "Welcome back" to "Notice what feels present right now."
            }

            timeOfDay == TimeOfDay.Evening -> {
                "Good evening" to "You can slow down here."
            }

            else -> {
                "It’s quiet here" to "Let the day settle for a moment."
            }
        }
    }
}