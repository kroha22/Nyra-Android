package com.nyra.app.android.core.ui_state.runtime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyra.app.android.core.profile.model.NyraHomeStateLevel

/**
 * Public entry point for Nyra's adaptive Home surface.
 *
 * Observes the ViewModel for profile + Home state level, then forwards everything
 * to [AdaptiveHomeScreen]. `NyraRootRoute` hosts this Composable underneath the
 * Calibration overlay and passes a callback that flips Calibration on when the
 * user taps the "Begin personalization" CTA on the Empty Home hero card.
 *
 * @param onBeginPersonalization callback fired when the user invokes the
 *   personalization CTA. `NyraRootRoute` wires this to `startCalibration()`.
 *   Optional — defaults to a no-op for previews / standalone use.
 */
@Composable
fun AdaptiveHomeRoute(
    modifier: Modifier = Modifier,
    onBeginPersonalization: () -> Unit = {},
    viewModel: AdaptiveHomeViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val stateLevel by viewModel.stateLevel.collectAsStateWithLifecycle(
        initialValue = NyraHomeStateLevel.Empty
    )

    AdaptiveHomeScreen(
        profile = profile,
        stateLevel = stateLevel,
        uiStateResolver = viewModel.uiStateResolver,
        compositionResolver = viewModel.compositionResolver,
        onBeginPersonalization = onBeginPersonalization,
        modifier = modifier.fillMaxSize()
    )
}
