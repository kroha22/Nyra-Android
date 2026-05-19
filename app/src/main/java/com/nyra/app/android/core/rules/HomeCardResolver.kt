package com.nyra.app.android.core.rules

import com.nyra.app.android.core.catalog.NyraContentCatalog
import com.nyra.app.android.core.model.*
import javax.inject.Inject

class HomeCardResolver @Inject constructor() {
    fun resolve(
        lastCheckIn: CheckInEntry?,
        timeOfDay: TimeOfDay,
        preferences: UserPreferences
    ): List<CardTemplate> {
        val cards = NyraContentCatalog.cardTemplates.toMutableList()

        // logic: if no check-in today or in this time of day, prioritize check-in card
        val needsCheckIn = lastCheckIn == null || lastCheckIn.timeOfDay != timeOfDay
        
        return cards.filter { card ->
            when (card.type) {
                CardType.CheckIn -> needsCheckIn
                CardType.Reflection -> timeOfDay == preferences.defaultRhythm.toTimeOfDay()
                else -> true
            }
        }.sortedByDescending { it.priority }
    }

    private fun ReflectionRhythm.toTimeOfDay(): TimeOfDay = when (this) {
        ReflectionRhythm.Morning -> TimeOfDay.Morning
        ReflectionRhythm.Afternoon -> TimeOfDay.Afternoon
        ReflectionRhythm.Evening -> TimeOfDay.Evening
        ReflectionRhythm.LateNight -> TimeOfDay.Night
    }
}