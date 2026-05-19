package com.nyra.app.android.core.rules

import com.nyra.app.android.core.catalog.NyraContentCatalog
import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.GentleAction
import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.model.UserPreferences
import javax.inject.Inject

class GentleActionResolver @Inject constructor() {
    fun resolve(
        lastCheckIn: CheckInEntry?,
        timeOfDay: TimeOfDay,
        preferences: UserPreferences
    ): List<GentleAction> {
        val allActions = NyraContentCatalog.gentleActions
        
        return allActions.filter { action ->
            val timeMatch = action.timeOfDay.isEmpty() || action.timeOfDay.contains(timeOfDay)
            val moodMatch = if (lastCheckIn != null) {
                action.suitableMoodCodes.isEmpty() || action.suitableMoodCodes.contains(lastCheckIn.moodCode)
            } else {
                true // Suggest general actions if no check-in
            }
            
            // Filter by atmosphere intensity if needed (logic can be more complex)
            val intensityMatch = when (action.intensity) {
                com.nyra.app.android.core.model.GentleActionIntensity.Low -> true
                com.nyra.app.android.core.model.GentleActionIntensity.Medium -> preferences.atmosphereIntensity > 40
            }

            timeMatch && moodMatch && intensityMatch
        }.take(3) // Limit to 3 suggestions
    }
}