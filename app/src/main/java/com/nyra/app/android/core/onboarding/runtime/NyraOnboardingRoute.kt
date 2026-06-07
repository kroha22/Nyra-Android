package com.nyra.app.android.core.onboarding.runtime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyra.app.android.core.onboarding.model.AtmosphereVariant
import com.nyra.app.android.core.onboarding.model.CalibrationStep
import com.nyra.app.android.core.onboarding.runtime.components.OnboardingAtmosphere
import com.nyra.app.android.core.onboarding.runtime.screens.BirthDateScreen
import com.nyra.app.android.core.onboarding.runtime.screens.CalibratingScreen
import com.nyra.app.android.core.onboarding.runtime.screens.EntryWelcomeScreen
import com.nyra.app.android.core.onboarding.runtime.screens.NameInputScreen
import com.nyra.app.android.core.onboarding.runtime.screens.TrustLayerScreen

/**
 * Composable host for the Emotional Calibration overlay.
 *
 * Hosts the [OnboardingAtmosphere] backdrop and switches between per-step
 * Composables. The atmosphere variant gently rotates across the flow to hint
 * at the three visual directions (HorizonDawn → WarmInterior → StillnightMinimal),
 * and the calibration stage feeds a `calibrationIntensity` that layers ribbons /
 * reaction / topology on top of the base variant.
 */
@Composable
fun NyraOnboardingRoute(
    modifier: Modifier = Modifier,
    viewModel: NyraOnboardingViewModel = hiltViewModel()
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val stage by viewModel.stage.collectAsStateWithLifecycle()

    val variant = variantFor(step)
    val intensity = if (step == CalibrationStep.Calibrating) stage.atmosphereIntensity else null

    OnboardingAtmosphere(
        variant = variant,
        calibrationIntensity = intensity,
        modifier = modifier.fillMaxSize()
    ) {
        when (step) {
            CalibrationStep.EntryWelcome -> EntryWelcomeScreen(viewModel)
            CalibrationStep.NameInput -> NameInputScreen(viewModel)
            CalibrationStep.BirthDate -> BirthDateScreen(viewModel)
            CalibrationStep.TrustLayer -> TrustLayerScreen(viewModel)
            CalibrationStep.Calibrating -> CalibratingScreen(viewModel)
        }
    }
}

/**
 * Atmospheric variant pacing across the flow. Entry and name input feel like
 * arriving at a horizon; birth date / trust feel like a warm intimate room;
 * the calibration sequence settles into a stillnight minimal backdrop so the
 * ribbon + topology overlays read most cleanly.
 */
private fun variantFor(step: CalibrationStep): AtmosphereVariant = when (step) {
    CalibrationStep.EntryWelcome,
    CalibrationStep.NameInput -> AtmosphereVariant.HorizonDawn
    CalibrationStep.BirthDate,
    CalibrationStep.TrustLayer -> AtmosphereVariant.WarmInterior
    CalibrationStep.Calibrating -> AtmosphereVariant.StillnightMinimal
}
