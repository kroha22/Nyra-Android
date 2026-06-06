package com.nyra.app.android.core.ui_state.resolver

import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig

interface NyraUiStateConfigResolver {
    fun resolve(profile: NyraUserProfile): NyraUiStateConfig
}
