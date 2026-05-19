package com.nyra.app.android.data.presence

import com.nyra.app.android.core.catalog.NyraContentCatalog
import com.nyra.app.android.core.model.PresenceCode
import com.nyra.app.android.core.model.PresenceState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NyraPresenceCatalog @Inject constructor() {
    fun getByCode(code: PresenceCode): PresenceState {
        return NyraContentCatalog.presenceStates.find { it.code == code }
            ?: NyraContentCatalog.presenceStates.first()
    }
}