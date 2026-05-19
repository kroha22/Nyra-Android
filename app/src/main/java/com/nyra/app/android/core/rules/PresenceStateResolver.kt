package com.nyra.app.android.core.rules

import com.nyra.app.android.core.catalog.NyraContentCatalog
import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.PresenceCode
import com.nyra.app.android.core.model.PresenceState
import javax.inject.Inject

class PresenceStateResolver @Inject constructor() {
    fun resolve(lastCheckIn: CheckInEntry?): PresenceState {
        val presenceCode = if (lastCheckIn == null) {
            PresenceCode.NeutralPresence
        } else {
            NyraContentCatalog.moodStates
                .find { it.code == lastCheckIn.moodCode }
                ?.presenceCode ?: PresenceCode.NeutralPresence
        }

        return NyraContentCatalog.presenceStates.find { it.code == presenceCode }
            ?: NyraContentCatalog.presenceStates.first()
    }
}