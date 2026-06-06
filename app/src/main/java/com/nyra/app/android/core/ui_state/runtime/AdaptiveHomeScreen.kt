package com.nyra.app.android.core.ui_state.runtime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.composition.HomeUiComposition
import com.nyra.app.android.core.ui_state.composition.HomeUiCompositionResolver
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolver

@Composable
fun AdaptiveHomeScreen(
    profile: NyraUserProfile,
    uiStateResolver: NyraUiStateConfigResolver,
    compositionResolver: HomeUiCompositionResolver,
    modifier: Modifier = Modifier
) {
    val uiState = rememberNyraUiState(profile = profile, resolver = uiStateResolver)
    val composition = compositionResolver.resolve(profile = profile, uiState = uiState)

    NyraAdaptiveLayer(uiState = uiState, modifier = modifier.fillMaxSize()) {
        AdaptiveHomeContent(composition = composition)
    }
}

@Composable
private fun AdaptiveHomeContent(
    composition: HomeUiComposition,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(composition.uiState.cardStyle.verticalSpacing.dp)
    ) {
        Text(
            text = composition.atmosphericTitle ?: "Nyra",
            color = composition.uiState.palette.textPrimary.toComposeColor()
        )
        composition.topPrompt?.let { prompt ->
            Text(
                text = prompt,
                color = composition.uiState.palette.textSecondary.toComposeColor()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        composition.modules.forEach { module ->
            NyraGlassCard(
                uiState = composition.uiState,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = module.id,
                    color = composition.uiState.palette.textPrimary.toComposeColor(),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
