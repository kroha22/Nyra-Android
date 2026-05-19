package com.nyra.app.android.domain.presence

import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.MoodCode
import com.nyra.app.android.core.model.PresenceCode
import com.nyra.app.android.core.model.PresenceState
import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.data.presence.NyraPresenceCatalog
import javax.inject.Inject

class ResolvePresenceStateUseCase @Inject constructor(
    private val catalog: NyraPresenceCatalog
) {
    operator fun invoke(
        lastCheckIn: CheckInEntry?,
        timeOfDay: TimeOfDay
    ): PresenceState {
        val code = when (lastCheckIn?.moodCode) {
            MoodCode.Calm -> PresenceCode.CalmPresence
            MoodCode.Tired,
            MoodCode.LowEnergy -> PresenceCode.DrainedPresence
            MoodCode.Anxious,
            MoodCode.Restless -> PresenceCode.RestlessPresence
            MoodCode.Open -> PresenceCode.OpenPresence
            MoodCode.Quiet -> PresenceCode.QuietPresence
            MoodCode.Clear -> PresenceCode.CalmPresence
            null -> when (timeOfDay) {
                TimeOfDay.Morning -> PresenceCode.OpenPresence
                TimeOfDay.Afternoon -> PresenceCode.NeutralPresence
                TimeOfDay.Evening -> PresenceCode.QuietPresence
                TimeOfDay.Night -> PresenceCode.GroundedPresence
            }
        }

        return catalog.getByCode(code)
    }
}