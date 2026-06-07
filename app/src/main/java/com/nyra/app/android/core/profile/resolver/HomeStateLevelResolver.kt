package com.nyra.app.android.core.profile.resolver

import com.nyra.app.android.core.profile.model.NyraHomeStateLevel
import com.nyra.app.android.core.profile.model.NyraHomeStateSignals
import com.nyra.app.android.core.profile.model.NyraUserProfile
import javax.inject.Inject

/**
 * Maps the current profile + auxiliary signal flags into a [NyraHomeStateLevel].
 *
 * Ladder logic (highest match wins):
 *
 *  - MultiSystem            ← natal + secondary personality system available
 *  - HistoryAccumulated     ← ≥ ~21 days of tracker / journal history
 *  - TransitsActive         ← natal + active symbolic transits
 *  - NatalEnabled           ← natal data parsed + profile placements present
 *  - TrackersEnabled        ← at least one tracker enabled (no natal yet)
 *  - Empty                  ← everything else
 *
 * The thresholds err on the side of *staying lower* — the spec wants the surface
 * to feel earned, not pushy.
 */
class HomeStateLevelResolver @Inject constructor() {

    fun resolve(
        profile: NyraUserProfile,
        signals: NyraHomeStateSignals
    ): NyraHomeStateLevel {
        val hasProfilePlacements = profile.placements.isNotEmpty()
        val natalReady = signals.hasNatalData && hasProfilePlacements

        return when {
            natalReady && signals.hasSecondarySystem ->
                NyraHomeStateLevel.MultiSystem

            signals.historyDays >= HISTORY_THRESHOLD_DAYS ->
                NyraHomeStateLevel.HistoryAccumulated

            natalReady && signals.hasActiveTransits ->
                NyraHomeStateLevel.TransitsActive

            natalReady ->
                NyraHomeStateLevel.NatalEnabled

            signals.trackersEnabled ->
                NyraHomeStateLevel.TrackersEnabled

            else ->
                NyraHomeStateLevel.Empty
        }
    }

    private companion object {
        /** Three weeks of tracker history feels like Nyra has "remembered" enough. */
        const val HISTORY_THRESHOLD_DAYS = 21
    }
}
