package com.nyra.app.android.core.atmosphere.model

/**
 * Emotional state space for the flow-ribbon layer.
 *
 * Per spec:
 *  - [Calm]          — smooth symmetric flow.
 *  - [Reflective]    — slower layered movement.
 *  - [Overwhelmed]   — fragmented directional flow, subtle instability.
 *  - [Hopeful]       — upward movement, expanded openness.
 *  - [Disconnected]  — almost static, reduced flow density.
 *
 * Each state maps to a [Parameters] block consumed by the ribbon renderer.
 */
enum class EmotionalRibbonState {
    Calm,
    Reflective,
    Overwhelmed,
    Hopeful,
    Disconnected;

    val parameters: Parameters
        get() = when (this) {
            Calm -> Parameters(
                ribbonCount = 3,
                speedMultiplier = 1.0f,
                amplitudeMultiplier = 1.0f,
                verticalDrift = 0.0f,
                instability = 0.0f,
                alphaMultiplier = 1.0f
            )
            Reflective -> Parameters(
                ribbonCount = 4,
                speedMultiplier = 0.7f,
                amplitudeMultiplier = 0.9f,
                verticalDrift = 0.0f,
                instability = 0.0f,
                alphaMultiplier = 0.95f
            )
            Overwhelmed -> Parameters(
                ribbonCount = 3,
                speedMultiplier = 1.15f,
                amplitudeMultiplier = 1.4f,
                verticalDrift = 0.0f,
                instability = 0.35f,
                alphaMultiplier = 0.85f
            )
            Hopeful -> Parameters(
                ribbonCount = 3,
                speedMultiplier = 0.9f,
                amplitudeMultiplier = 0.95f,
                verticalDrift = -0.05f,        // negative = up
                instability = 0.0f,
                alphaMultiplier = 1.05f
            )
            Disconnected -> Parameters(
                ribbonCount = 2,
                speedMultiplier = 0.30f,
                amplitudeMultiplier = 0.4f,
                verticalDrift = 0.0f,
                instability = 0.0f,
                alphaMultiplier = 0.55f
            )
        }

    /**
     * @property ribbonCount number of parallel ribbons to draw.
     * @property speedMultiplier scales the base drift period. < 1 = slower.
     * @property amplitudeMultiplier scales the sine wave height.
     * @property verticalDrift fraction of screen height to shift baseline per ribbon
     *   index. Negative = upward (used by Hopeful).
     * @property instability 0..1 — per-segment jitter, breaks the silk feeling
     *   into something more uneven. Used by Overwhelmed.
     * @property alphaMultiplier multiplies the ribbon stroke alpha.
     */
    data class Parameters(
        val ribbonCount: Int,
        val speedMultiplier: Float,
        val amplitudeMultiplier: Float,
        val verticalDrift: Float,
        val instability: Float,
        val alphaMultiplier: Float
    )
}
