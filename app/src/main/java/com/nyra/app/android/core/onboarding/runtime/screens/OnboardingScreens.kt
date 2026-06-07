package com.nyra.app.android.core.onboarding.runtime.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nyra.app.android.core.onboarding.model.CalibrationStage
import com.nyra.app.android.core.onboarding.runtime.NyraOnboardingViewModel
import com.nyra.app.android.core.onboarding.runtime.components.AtmosphericGhostButton
import com.nyra.app.android.core.onboarding.runtime.components.AtmosphericPrimaryButton
import com.nyra.app.android.core.onboarding.runtime.components.AtmosphericSurface
import com.nyra.app.android.core.onboarding.runtime.components.AtmosphericTextField
import com.nyra.app.android.core.onboarding.runtime.components.CinematicDatePicker
import com.nyra.app.android.core.onboarding.runtime.components.OnboardingCaption
import com.nyra.app.android.core.onboarding.runtime.components.OnboardingEyebrow
import com.nyra.app.android.core.onboarding.runtime.components.OnboardingScaffold
import com.nyra.app.android.core.onboarding.runtime.components.OnboardingSubtitle
import com.nyra.app.android.core.onboarding.runtime.components.OnboardingTitle
import com.nyra.app.android.core.onboarding.runtime.components.toColor
import com.nyra.app.android.core.onboarding.runtime.components.NyraCalibrationPaletteV1
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette
import java.time.LocalDate
import kotlinx.coroutines.delay

private const val CALIBRATION_TOTAL_STEPS = 5

// ─── SCREEN 1 — Entry Into Calibration ────────────────────────────────────────

@Composable
fun EntryWelcomeScreen(viewModel: NyraOnboardingViewModel) {
    OnboardingScaffold(
        progress = 0 to CALIBRATION_TOTAL_STEPS,
        bottom = {
            AtmosphericPrimaryButton(
                label = "Begin personalization",
                onClick = viewModel::goNext
            )
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            OnboardingEyebrow("Emotional calibration")
            OnboardingTitle("Build your emotional profile")
            OnboardingSubtitle(
                "Nyra becomes more personal as you add emotional signals."
            )
        }
    }
}

// ─── SCREEN 2 — Name input ────────────────────────────────────────────────────

@Composable
fun NameInputScreen(viewModel: NyraOnboardingViewModel) {
    val inputs by viewModel.inputs.collectAsStateWithLifecycle()
    OnboardingScaffold(
        onBack = viewModel::goBack,
        progress = 1 to CALIBRATION_TOTAL_STEPS,
        bottom = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AtmosphericPrimaryButton(label = "Continue", onClick = viewModel::goNext)
                AtmosphericGhostButton(label = "Skip for now", onClick = viewModel::goNext)
            }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            OnboardingTitle("What should Nyra call you?")
            AtmosphericTextField(
                value = inputs.displayName,
                onValueChange = viewModel::setDisplayName,
                placeholder = "Your name"
            )
            OnboardingCaption("Used to personalize your emotional space.")
        }
    }
}

// ─── SCREEN 3 — Birth date ────────────────────────────────────────────────────

@Composable
fun BirthDateScreen(viewModel: NyraOnboardingViewModel) {
    val inputs by viewModel.inputs.collectAsStateWithLifecycle()
    val seed = inputs.birthDate ?: LocalDate.now().minusYears(28)

    OnboardingScaffold(
        onBack = viewModel::goBack,
        progress = 2 to CALIBRATION_TOTAL_STEPS,
        bottom = {
            AtmosphericPrimaryButton(
                label = "Continue",
                enabled = inputs.hasBirthDate,
                onClick = viewModel::goNext
            )
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(28.dp)) {
            OnboardingTitle("When were you born?")
            CinematicDatePicker(
                day = seed.dayOfMonth,
                month = seed.monthValue,
                year = seed.year,
                onDateChange = { d, m, y ->
                    viewModel.setBirthDate(LocalDate.of(y, m, d))
                }
            )
            OnboardingCaption(
                "Your birth date helps Nyra create a more personal emotional interpretation layer."
            )
        }
    }
}

// ─── SCREEN 4 — Trust Layer ───────────────────────────────────────────────────

@Composable
fun TrustLayerScreen(viewModel: NyraOnboardingViewModel) {
    OnboardingScaffold(
        onBack = viewModel::goBack,
        progress = 3 to CALIBRATION_TOTAL_STEPS,
        bottom = {
            AtmosphericPrimaryButton(label = "I understand", onClick = viewModel::goNext)
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(26.dp)) {
            OnboardingEyebrow("How Nyra reads you")
            AtmosphericSurface(modifier = Modifier.fillMaxWidth(), isStrong = true) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Nyra uses symbolic emotional patterns and emotional signals — not deterministic predictions.",
                        color = NyraCalibrationPaletteV1.TextPrimary.toColor(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Light,
                        lineHeight = 26.sp
                    )
                    OnboardingCaption(
                        "Your data stays on this device. You can change or remove it any time."
                    )
                }
            }
        }
    }
}

// ─── SCREENS 5–7 — Calibration Sequence ───────────────────────────────────────
//
// One composable that progresses through [CalibrationStage] values. The
// atmospheric background simultaneously layers ribbons → reaction → topology
// via `OnboardingAtmosphere.calibrationIntensity`. When the final stage settles,
// we hint at adaptive Home arriving and call `finishCalibration()` to dismiss
// the overlay — `NyraRootRoute` cross-fades back to `AdaptiveHomeRoute`.

@Composable
fun CalibratingScreen(viewModel: NyraOnboardingViewModel) {
    val stage by viewModel.stage.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // Slow cinematic cadence — 2.0s per stage, then a calm hold on the
        // final stage before the overlay dismisses.
        while (true) {
            delay(2000)
            if (viewModel.isCalibrationStageComplete()) {
                delay(1400) // hint at Home adapting; keep it calm.
                viewModel.finishCalibration()
                break
            }
            viewModel.advanceStage()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            OnboardingEyebrow("Calibrating")

            // Cross-fade between stage texts — previous fades, next emerges.
            Box(modifier = Modifier.height(80.dp)) {
                Crossfade(
                    targetState = stage,
                    animationSpec = tween(durationMillis = 1000),
                    label = "calibration_stage_text"
                ) { current ->
                    Text(
                        text = "${current.body}…",
                        color = NyraCalibrationPaletteV1.TextPrimary.toColor(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Light,
                        lineHeight = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(0.95f)
                    )
                }
            }

            StageBar(activeStage = stage)
        }
    }
}

@Composable
private fun StageBar(activeStage: CalibrationStage) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalibrationStage.entries.forEach { phase ->
            val active = phase.ordinal <= activeStage.ordinal
            Box(
                modifier = Modifier
                    .alpha(if (active) 0.85f else 0.20f)
                    .height(3.dp)
                    .width(28.dp)
                    .background(
                        color = NyraCalibrationPaletteV1.HorizonLavenderV1.toColor(0.7f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}
