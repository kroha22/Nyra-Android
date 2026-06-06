package com.nyra.app.android.core.ui_state.runtime

import androidx.lifecycle.ViewModel
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.composition.HomeUiCompositionResolver
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Backing ViewModel for [AdaptiveHomeRoute].
 *
 * Owns the [NyraUserProfile] flow and exposes the runtime resolvers so the route
 * Composable stays thin. The profile source is intentionally a placeholder right now:
 * we emit [NyraUserProfile.empty] until the real onboarding → synthesis → storage
 * pipeline lands. When that pipeline exists, swap the placeholder with a
 * `observeProfile()` flow from the profile repository — the rest of the runtime
 * doesn't need to change.
 */
@HiltViewModel
class AdaptiveHomeViewModel @Inject constructor(
    val uiStateResolver: NyraUiStateConfigResolver,
    val compositionResolver: HomeUiCompositionResolver
) : ViewModel() {

    private val _profile = MutableStateFlow(NyraUserProfile.empty())

    /**
     * Current profile. Emits [NyraUserProfile.empty] until a synthesized profile is wired in.
     */
    val profile: StateFlow<NyraUserProfile> = _profile.asStateFlow()
}
