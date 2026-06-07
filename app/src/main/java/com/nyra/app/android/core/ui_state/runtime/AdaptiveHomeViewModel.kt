package com.nyra.app.android.core.ui_state.runtime

import androidx.lifecycle.ViewModel
import com.nyra.app.android.core.profile.model.NyraHomeStateLevel
import com.nyra.app.android.core.profile.model.NyraHomeStateSignals
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.profile.resolver.HomeStateLevelResolver
import com.nyra.app.android.core.ui_state.composition.HomeUiCompositionResolver
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

/**
 * Backing ViewModel for [AdaptiveHomeRoute].
 *
 * Owns three reactive streams:
 *  - [profile] — the user's synthesised profile (currently `NyraUserProfile.empty`
 *    until the onboarding → synthesis → storage pipeline lands).
 *  - [signals] — auxiliary Home-Screen state signals (trackers, natal, history…).
 *  - [stateLevel] — derived combination of the two via [HomeStateLevelResolver].
 *
 * Tests and runtime can update either stream independently:
 * `viewModel.updateSignals { it.copy(trackersEnabled = true) }`.
 */
@HiltViewModel
class AdaptiveHomeViewModel @Inject constructor(
    val uiStateResolver: NyraUiStateConfigResolver,
    val compositionResolver: HomeUiCompositionResolver,
    private val stateLevelResolver: HomeStateLevelResolver
) : ViewModel() {

    private val _profile = MutableStateFlow(NyraUserProfile.empty())
    val profile: StateFlow<NyraUserProfile> = _profile.asStateFlow()

    private val _signals = MutableStateFlow(NyraHomeStateSignals.Empty)
    val signals: StateFlow<NyraHomeStateSignals> = _signals.asStateFlow()

    /**
     * Current Home state level. Recomputed whenever [profile] or [signals] emit.
     * The combine call is intentionally lazy — UI collects it via
     * `collectAsStateWithLifecycle` and only pays the cost when subscribed.
     */
    val stateLevel: kotlinx.coroutines.flow.Flow<NyraHomeStateLevel> =
        combine(_profile, _signals) { p, s -> stateLevelResolver.resolve(p, s) }

    fun updateSignals(transform: (NyraHomeStateSignals) -> NyraHomeStateSignals) {
        _signals.value = transform(_signals.value)
    }

    fun updateProfile(profile: NyraUserProfile) {
        _profile.value = profile
    }
}
