package com.nyra.app.android.core.ui_state.resolver

import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig
import javax.inject.Inject

/**
 * Subtle time-of-day modulation applied on top of an already-resolved [NyraUiStateConfig].
 *
 * Per the atmospheric system spec:
 *  - **No hard switching** between visual states.
 *  - The current visual state's identity (palette, module order, text tone) is preserved.
 *  - Only atmospheric *overlay parameters* (blur, glow, motion pacing, particle density)
 *    are nudged — small enough that the eye reads "time of day" without losing
 *    the Warm Horizon continuity.
 *
 * Mapping (deltas applied to whatever the state provides):
 *
 *  | Time      | Blur  | Glow   | Motion  | Particles | Feeling                          |
 *  |-----------|-------|--------|---------|-----------|----------------------------------|
 *  | Morning   | +2.0  | -0.10  | ×0.92   | +0.05     | mist, diffuse, openness          |
 *  | Afternoon | -3.0  | -0.06  | ×1.00   | -0.04     | clean structure, low fog         |
 *  | Evening   | 0     | 0      | ×1.00   | 0         | canonical Nyra evening (no-op)   |
 *  | Night     | +3.0  | -0.14  | ×0.80   | -0.05     | slower, deeper, isolated glow    |
 *
 * Evening is intentionally a no-op: it IS the canonical Nyra mood.
 */
class NyraAtmosphereTimeModulator @Inject constructor() {

    fun modulate(config: NyraUiStateConfig, timeOfDay: TimeOfDay): NyraUiStateConfig {
        val m = modulationFor(timeOfDay)
        if (m.isNoOp) return config

        return config.copy(
            background = config.background.copy(
                blurStrength = (config.background.blurStrength + m.blurDelta)
                    .coerceIn(MIN_BLUR, MAX_BLUR),
                glowIntensity = (config.background.glowIntensity + m.glowDelta)
                    .coerceIn(MIN_GLOW, MAX_GLOW)
            ),
            motion = config.motion.copy(
                driftSpeed = (config.motion.driftSpeed * m.driftMultiplier)
                    .coerceAtLeast(MIN_DRIFT),
                orbBreathingSpeed = (config.motion.orbBreathingSpeed * m.driftMultiplier)
                    .coerceAtLeast(MIN_DRIFT),
                particleDensity = (config.motion.particleDensity + m.particleDelta)
                    .coerceIn(0f, 1f)
            )
        )
    }

    private data class Modulation(
        val blurDelta: Float,
        val glowDelta: Float,
        val driftMultiplier: Float,
        val particleDelta: Float
    ) {
        val isNoOp: Boolean
            get() = blurDelta == 0f &&
                glowDelta == 0f &&
                driftMultiplier == 1f &&
                particleDelta == 0f
    }

    private fun modulationFor(time: TimeOfDay): Modulation = when (time) {
        TimeOfDay.Morning -> Modulation(
            blurDelta = 2.0f,
            glowDelta = -0.10f,
            driftMultiplier = 0.92f,
            particleDelta = 0.05f
        )
        TimeOfDay.Afternoon -> Modulation(
            blurDelta = -3.0f,
            glowDelta = -0.06f,
            driftMultiplier = 1.0f,
            particleDelta = -0.04f
        )
        TimeOfDay.Evening -> Modulation(
            blurDelta = 0f,
            glowDelta = 0f,
            driftMultiplier = 1.0f,
            particleDelta = 0f
        )
        TimeOfDay.Night -> Modulation(
            blurDelta = 3.0f,
            glowDelta = -0.14f,
            driftMultiplier = 0.80f,
            particleDelta = -0.05f
        )
    }

    private companion object {
        const val MIN_BLUR = 6f
        const val MAX_BLUR = 40f
        const val MIN_GLOW = 0.12f
        const val MAX_GLOW = 1.0f
        const val MIN_DRIFT = 0.04f
    }
}
