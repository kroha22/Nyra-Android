package com.nyra.app.android.core.ui_state.composition

import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig
import javax.inject.Inject

class HomeUiCompositionResolver @Inject constructor(
    private val promptResolver: HomePromptResolver
) {

    fun resolve(
        profile: NyraUserProfile,
        uiState: NyraUiStateConfig
    ): HomeUiComposition {
        val promptSet = promptResolver.resolve(profile, uiState.textTone)
        return HomeUiComposition(
            uiState = uiState,
            modules = uiState.moduleOrder.mapIndexed { index, moduleId ->
                HomeModuleUiModel(
                    id = moduleId,
                    priority = uiState.moduleOrder.size - index,
                    textToneToken = uiState.textTone.name.lowercase()
                )
            },
            topPrompt = promptSet.topPrompt,
            atmosphericTitle = resolveAtmosphericTitle(profile, uiState)
        )
    }

    private fun resolveAtmosphericTitle(
        profile: NyraUserProfile,
        uiState: NyraUiStateConfig
    ): String =
        when {
            profile.activeArchetypes.isNotEmpty() -> profile.activeArchetypes.first().archetypeId
                .split("_")
                .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
            else -> uiState.primaryState.name
                .lowercase()
                .split("_")
                .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }
        }
}
