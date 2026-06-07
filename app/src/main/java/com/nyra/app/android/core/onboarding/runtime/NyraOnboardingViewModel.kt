package com.nyra.app.android.core.onboarding.runtime

import androidx.lifecycle.ViewModel
import com.nyra.app.android.core.onboarding.model.CalibrationInputs
import com.nyra.app.android.core.onboarding.model.CalibrationStage
import com.nyra.app.android.core.onboarding.model.CalibrationStep
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for Nyra's Emotional Calibration flow.
 *
 * Calibration is a *modal* flow over the Home surface — not a gate before it.
 * The user enters Home in Empty state first, then chooses to begin
 * personalization. This ViewModel exposes:
 *
 *  - [inProgress] — true while calibration is on screen. `NyraRootRoute`
 *    watches this and overlays [NyraOnboardingRoute] when true.
 *  - [step] — which step is currently presented inside the calibration overlay.
 *  - [inputs] — collected name + birth date.
 *  - [stage] — which sub-stage of the final calibration phase we're in.
 *
 * No persistence yet: state is lost across process death. DataStore wiring is
 * a follow-up; nothing in the public API changes.
 */
@HiltViewModel
class NyraOnboardingViewModel @Inject constructor() : ViewModel() {

    private val _inProgress = MutableStateFlow(false)
    val inProgress: StateFlow<Boolean> = _inProgress.asStateFlow()

    private val _step = MutableStateFlow(CalibrationStep.EntryWelcome)
    val step: StateFlow<CalibrationStep> = _step.asStateFlow()

    private val _inputs = MutableStateFlow(CalibrationInputs())
    val inputs: StateFlow<CalibrationInputs> = _inputs.asStateFlow()

    private val _stage = MutableStateFlow(CalibrationStage.MappingPatterns)
    val stage: StateFlow<CalibrationStage> = _stage.asStateFlow()

    // ─── Entering and leaving the calibration overlay ────────────────────────

    /** Called when the user taps "Begin personalization" from Empty Home. */
    fun startCalibration() {
        _step.value = CalibrationStep.EntryWelcome
        _stage.value = CalibrationStage.MappingPatterns
        _inProgress.value = true
    }

    /** Called when the final calibration stage completes. */
    fun finishCalibration() {
        _inProgress.value = false
    }

    /** Allow the user to abandon calibration mid-flow without losing inputs. */
    fun dismissCalibration() {
        _inProgress.value = false
    }

    // ─── Navigation inside the overlay ───────────────────────────────────────

    fun goNext() {
        _step.value.next()?.let { _step.value = it }
    }

    fun goBack() {
        _step.value.previous()?.let { _step.value = it }
    }

    // ─── Input setters ───────────────────────────────────────────────────────

    fun setDisplayName(name: String) {
        _inputs.value = _inputs.value.copy(displayName = name)
    }

    fun setBirthDate(date: LocalDate) {
        _inputs.value = _inputs.value.copy(birthDate = date)
    }

    // ─── Calibration stage progression ───────────────────────────────────────

    fun advanceStage() {
        _stage.value.next()?.let { _stage.value = it }
    }

    fun isCalibrationStageComplete(): Boolean =
        _stage.value == CalibrationStage.entries.last()
}
