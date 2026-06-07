package com.nyra.app.android.core.onboarding.runtime

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyra.app.android.core.ui_state.runtime.AdaptiveHomeRoute

/**
 * Top-level Composable for the app's foreground surface.
 *
 * Calibration is **modal over Home** — not a gate before it. The user enters
 * `AdaptiveHomeRoute` in its Empty state, then chooses to begin personalization;
 * tapping that CTA flips [NyraOnboardingViewModel.inProgress] to `true` and
 * this surface cross-fades to [NyraOnboardingRoute].
 *
 * When calibration completes (or is dismissed), the overlay fades out and Home
 * is back in front — now subtly more personal.
 *
 * Future: replace the in-memory `inProgress` + `hasCalibrated` flags with
 * DataStore-backed persistence so the state survives process death.
 */
@Composable
fun NyraRootRoute(
    modifier: Modifier = Modifier,
    viewModel: NyraOnboardingViewModel = hiltViewModel()
) {
    val calibrationActive by viewModel.inProgress.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        // Home is always present underneath. The Begin-personalization CTA
        // sits on Home's Empty state hero card; tapping it asks the same
        // ViewModel to switch to calibration.
        AdaptiveHomeRoute(
            modifier = Modifier.fillMaxSize(),
            onBeginPersonalization = viewModel::startCalibration
        )

        AnimatedVisibility(
            visible = calibrationActive,
            enter = fadeIn(animationSpec = tween(durationMillis = 700)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1100, delayMillis = 200))
        ) {
            NyraOnboardingRoute(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
            )
        }
    }
}
