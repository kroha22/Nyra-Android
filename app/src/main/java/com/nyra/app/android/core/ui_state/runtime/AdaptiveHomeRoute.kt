package com.nyra.app.android.core.ui_state.runtime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Public entry point for Nyra's adaptive home surface.
 *
 * Resolves [AdaptiveHomeViewModel] via Hilt, observes the current profile,
 * and forwards it (along with the injected resolvers) to [AdaptiveHomeScreen].
 *
 * Replace the [HomeMockup] call site in `MainActivity` with this Composable to
 * activate the adaptive UI runtime.
 */
@Composable
fun AdaptiveHomeRoute(
    modifier: Modifier = Modifier,
    viewModel: AdaptiveHomeViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()

    AdaptiveHomeScreen(
        profile = profile,
        uiStateResolver = viewModel.uiStateResolver,
        compositionResolver = viewModel.compositionResolver,
        modifier = modifier.fillMaxSize()
    )
}
