package com.nyra.app.android.domain.presence.usecase

import com.nyra.app.android.core.model.CheckInEntry
import com.nyra.app.android.core.model.PresenceState
import com.nyra.app.android.core.rules.PresenceStateResolver
import javax.inject.Inject

class ResolvePresenceStateUseCase @Inject constructor(
    private val presenceStateResolver: PresenceStateResolver
) {
    operator fun invoke(lastCheckIn: CheckInEntry?): PresenceState {
        return presenceStateResolver.resolve(lastCheckIn)
    }
}