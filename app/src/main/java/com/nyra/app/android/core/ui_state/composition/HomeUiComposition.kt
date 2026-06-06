package com.nyra.app.android.core.ui_state.composition

import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig

data class HomeUiComposition(
    val uiState: NyraUiStateConfig,
    val modules: List<HomeModuleUiModel>,
    val topPrompt: String?,
    val atmosphericTitle: String?
)

data class HomeModuleUiModel(
    val id: String,
    val priority: Int,
    val textToneToken: String
)

data class HomePromptSet(
    val topPrompt: String?,
    val eveningReflectionPrompt: String?,
    val checkInQuestion: String?
)
