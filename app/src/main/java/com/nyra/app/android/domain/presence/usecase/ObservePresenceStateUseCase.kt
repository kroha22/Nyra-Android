package com.nyra.app.android.domain.presence.usecase

import com.nyra.app.android.core.model.PresenceState
import com.nyra.app.android.domain.checkin.repository.CheckInRepository
import com.nyra.app.android.domain.presence.usecase.ResolvePresenceStateUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObservePresenceStateUseCase @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val resolvePresenceStateUseCase: ResolvePresenceStateUseCase
) {
    operator fun invoke(): Flow<PresenceState> {
        return checkInRepository.observeLatestCheckIn().map { lastCheckIn ->
            resolvePresenceStateUseCase(lastCheckIn)
        }
    }
}