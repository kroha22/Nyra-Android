package com.nyra.app.android.core.onboarding.model

import java.time.LocalDate

/**
 * Emotional Calibration sequence — 5 steps.
 *
 * The user reaches calibration *intentionally* by tapping "Begin personalization"
 * from the Empty Home state. Calibration is a modal flow over the Home surface,
 * not a gate before it. When it completes, the system returns to Home with the
 * atmosphere subtly adapted.
 *
 * Removed from this flow on purpose (will return as optional deeper personalization
 * later): current location, birth time, birth place, full Home preview.
 */
enum class CalibrationStep {
    EntryWelcome,     // Screen 1: "Build your emotional profile" + Begin personalization CTA
    NameInput,        // Screen 2: glassmorphism name field
    BirthDate,        // Screen 3: cinematic wheel picker
    TrustLayer,       // Screen 4: how Nyra reads you (symbolic, not deterministic)
    Calibrating;      // Screens 5–7: 3-stage sequence with visual atmospheric evolution

    fun next(): CalibrationStep? = entries.getOrNull(ordinal + 1)
    fun previous(): CalibrationStep? = entries.getOrNull(ordinal - 1)
}

/**
 * Internal stages of the [CalibrationStep.Calibrating] phase.
 * Each stage adds a layer to the atmospheric background — ribbons first,
 * then atmospheric reaction, then very light emotional topology.
 *
 * The body line rotates with the stage; previous text fades as the next appears.
 */
enum class CalibrationStage(val body: String) {
    MappingPatterns("Mapping emotional patterns"),
    BuildingSymbolic("Building symbolic identity"),
    Synthesizing("Synthesizing emotional signals"),
    Calibrating("Calibrating emotional atmosphere");

    fun next(): CalibrationStage? = entries.getOrNull(ordinal + 1)

    /**
     * Atmospheric intensity level driven by this stage. The background uses this
     * to layer ribbons (≥ 1), reaction (≥ 2), and topology (≥ 3).
     */
    val atmosphereIntensity: Int
        get() = when (this) {
            MappingPatterns -> 1
            BuildingSymbolic -> 2
            Synthesizing -> 3
            Calibrating -> 3
        }
}

/**
 * Atmospheric variants — shown subtly across the calibration to hint at the
 * three visual directions. Not surfaced to the user as a picker in this flow
 * (per the revised spec — calibration is intentionally minimal).
 */
enum class AtmosphereVariant {
    HorizonDawn,
    WarmInterior,
    StillnightMinimal
}

/**
 * Collected inputs from the Emotional Calibration flow.
 *
 * Only what calibration actually asks: display name + birth date. Everything
 * else (birth place, birth time, current location) is optional deeper
 * personalization deferred to a later flow.
 */
data class CalibrationInputs(
    val displayName: String = "",
    val birthDate: LocalDate? = null
) {
    val hasBirthDate: Boolean get() = birthDate != null
}
