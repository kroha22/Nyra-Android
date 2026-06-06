package com.nyra.app.android.core.ui_state.resolver

import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig

interface NyraUiStateConfigResolver {

    /**
     * Resolve the runtime [NyraUiStateConfig] for [profile].
     *
     * @param timeOfDay subtle atmospheric modulation. Pass `null` (default) to use the
     *   current system time. Passing an explicit value is useful in tests or when
     *   previewing a specific atmosphere from settings.
     */
    fun resolve(
        profile: NyraUserProfile,
        timeOfDay: TimeOfDay? = null
    ): NyraUiStateConfig
}
