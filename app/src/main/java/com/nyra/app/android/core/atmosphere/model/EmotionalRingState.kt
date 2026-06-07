package com.nyra.app.android.core.atmosphere.model

/**
 * Emotional state space for the atmospheric ring layer.
 *
 * Per spec:
 *  - [Calm]          — stable centered ring, symmetrical glow.
 *  - [Reflective]    — layered orbit traces, soft diffusion.
 *  - [Overwhelmed]   — fragmented asymmetry, blurred orbital edges.
 *  - [Hopeful]       — expanded horizon ring, upward light diffusion.
 *  - [Disconnected]  — isolated distant ring, low brightness.
 *
 * Each state maps to a [Parameters] block consumed by the ring layer renderer.
 * The parameter set is intentionally compact — anything richer should add a
 * field here, not branch inside the Composable.
 */
enum class EmotionalRingState {
    Calm,
    Reflective,
    Overwhelmed,
    Hopeful,
    Disconnected;

    val parameters: Parameters
        get() = when (this) {
            // Symmetric, centered, single ring + soft outer halo.
            Calm -> Parameters(
                strokeCount = 2,
                radiusMultiplier = 1.0f,
                centerYFraction = 0.30f,
                centerXJitter = 0.06f,
                strokeAlpha = 1.0f,
                blurAmount = 0.0f,
                asymmetry = 0.0f,
                verticalLift = 0.0f,
                outerHalo = true
            )
            // Multiple slow orbit traces, slight diffusion.
            Reflective -> Parameters(
                strokeCount = 4,
                radiusMultiplier = 1.05f,
                centerYFraction = 0.28f,
                centerXJitter = 0.04f,
                strokeAlpha = 0.85f,
                blurAmount = 1.4f,
                asymmetry = 0.0f,
                verticalLift = 0.0f,
                outerHalo = true
            )
            // Off-axis, broken arcs, edges feel blurred.
            Overwhelmed -> Parameters(
                strokeCount = 3,
                radiusMultiplier = 0.95f,
                centerYFraction = 0.32f,
                centerXJitter = 0.18f,
                strokeAlpha = 0.65f,
                blurAmount = 3.6f,
                asymmetry = 0.55f,
                verticalLift = 0.0f,
                outerHalo = false
            )
            // Expanded ring biased upward; brighter, open.
            Hopeful -> Parameters(
                strokeCount = 3,
                radiusMultiplier = 1.20f,
                centerYFraction = 0.22f,
                centerXJitter = 0.03f,
                strokeAlpha = 1.0f,
                blurAmount = 0.0f,
                asymmetry = 0.0f,
                verticalLift = 0.06f,
                outerHalo = true
            )
            // Far away, dim, single thin ring.
            Disconnected -> Parameters(
                strokeCount = 1,
                radiusMultiplier = 0.75f,
                centerYFraction = 0.18f,
                centerXJitter = 0.0f,
                strokeAlpha = 0.40f,
                blurAmount = 0.6f,
                asymmetry = 0.0f,
                verticalLift = 0.0f,
                outerHalo = false
            )
        }

    /**
     * @property strokeCount number of concentric strokes drawn for this state.
     * @property radiusMultiplier scales the base ring radius (state-dependent).
     * @property centerYFraction vertical fraction (0 top, 1 bottom) of the ring centre.
     * @property centerXJitter how far the ring drifts horizontally during animation.
     *   0 = locked centred. Used as the `* sin(phase)` amplitude.
     * @property strokeAlpha multiplier on the stroke colour alpha.
     * @property blurAmount soft-edge diffusion, in dp-equivalent units. 0 = sharp.
     * @property asymmetry 0..1 — when > 0 the renderer draws arcs instead of full
     *   circles, producing the "fragmented asymmetry" of Overwhelmed.
     * @property verticalLift Y offset added per concentric trace, in fraction of
     *   the screen height. Positive lifts upward — used for Hopeful.
     * @property outerHalo whether to draw the soft secondary halo behind the ring.
     */
    data class Parameters(
        val strokeCount: Int,
        val radiusMultiplier: Float,
        val centerYFraction: Float,
        val centerXJitter: Float,
        val strokeAlpha: Float,
        val blurAmount: Float,
        val asymmetry: Float,
        val verticalLift: Float,
        val outerHalo: Boolean
    )
}
