package com.nyra.app.android.domain.home.usecase

import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.model.TimeOfDay
import javax.inject.Inject

class GetGreetingUseCase @Inject constructor() {
    operator fun invoke(timeOfDay: TimeOfDay, lastMood: MoodCode?): String {
        val baseGreeting = when (timeOfDay) {
            TimeOfDay.Morning -> "Good morning."
            TimeOfDay.Afternoon -> "Good afternoon."
            TimeOfDay.Evening -> "Good evening."
            TimeOfDay.Night -> "Quiet night."
        }

        return when (lastMood) {
            MoodCode.Calm, MoodCode.Quiet -> "$baseGreeting Welcome back."
            MoodCode.Tired, MoodCode.LowEnergy -> "$baseGreeting It feels quieter today."
            MoodCode.Anxious, MoodCode.Restless -> "Breathe. $baseGreeting"
            else -> "$baseGreeting Welcome back."
        }
    }
}