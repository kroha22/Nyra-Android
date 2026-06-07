package com.nyra.app.android.core.atmosphere.palette

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/**
 * Smooth color-drift wrapper around [TimeOfDayPalette].
 *
 * Per spec: "colors should slowly drift emotionally" — never theme-switch, never
 * pop. When the runtime state shifts (time of day rolls over, the user enters a
 * new emotional journey state, etc.) every palette channel animates between
 * the old and new value over the same long, calm duration. The eye reads this
 * as the atmosphere breathing, not as a re-skin.
 *
 * Also folds in [warmthBias] — a 0..1 nudge that bumps the focal halo and ring
 * primary toward warmer tones without redefining the underlying palette. Used
 * by [com.nyra.app.android.core.atmosphere.model.EmotionalJourneyState.Memory]
 * to feel warmer than [com.nyra.app.android.core.atmosphere.model.EmotionalJourneyState.Empty]
 * inside the same time-of-day setting.
 */
object AnimatedAtmospherePalette {

    /** Default drift duration — extremely slow, cinematic, meditative. */
    private const val DEFAULT_DRIFT_MS = 1800

    /**
     * Returns an animated [TimeOfDayPalette] that drifts toward [target] as
     * inputs change. Recomposition propagates new colour values smoothly.
     *
     * @param target the desired palette for the current state.
     * @param warmthBias 0..1 — applied as a warm tint to focal & ring channels.
     * @param driftDurationMs how long to drift between palette versions.
     */
    @Composable
    fun rememberAnimated(
        target: TimeOfDayPalette,
        warmthBias: Float = 0f,
        driftDurationMs: Int = DEFAULT_DRIFT_MS
    ): TimeOfDayPalette {
        val biased = if (warmthBias > 0f) applyWarmth(target, warmthBias) else target
        val spec = tween<Color>(durationMillis = driftDurationMs)

        val backboneTop by animateColorAsState(biased.backboneTop, spec, label = "backboneTop")
        val backboneMid by animateColorAsState(biased.backboneMid, spec, label = "backboneMid")
        val backboneBottom by animateColorAsState(biased.backboneBottom, spec, label = "backboneBottom")
        val focalInner by animateColorAsState(biased.focalInner, spec, label = "focalInner")
        val focalOuter by animateColorAsState(biased.focalOuter, spec, label = "focalOuter")
        val ringPrimary by animateColorAsState(biased.ringPrimary, spec, label = "ringPrimary")
        val ringSecondary by animateColorAsState(biased.ringSecondary, spec, label = "ringSecondary")
        val fog by animateColorAsState(biased.fog, spec, label = "fog")
        val ribbon by animateColorAsState(biased.ribbon, spec, label = "ribbon")
        val topology by animateColorAsState(biased.topology, spec, label = "topology")

        return TimeOfDayPalette(
            backboneTop = backboneTop,
            backboneMid = backboneMid,
            backboneBottom = backboneBottom,
            focalInner = focalInner,
            focalOuter = focalOuter,
            ringPrimary = ringPrimary,
            ringSecondary = ringSecondary,
            fog = fog,
            ribbon = ribbon,
            topology = topology
        )
    }

    /**
     * Apply a warm tint to focal + ring channels.
     *
     * Pulls focal halo and ring colors toward a soft amber rose by [bias] amount.
     * Backbone is left alone — warmth shows up as a focal shift, never a
     * background swap.
     */
    private fun applyWarmth(palette: TimeOfDayPalette, bias: Float): TimeOfDayPalette {
        val clamped = bias.coerceIn(0f, 1f)
        if (clamped <= 0.01f) return palette
        val warmAnchor = Color(red = 1.0f, green = 0.78f, blue = 0.66f, alpha = 1.0f)
        fun blend(source: Color): Color =
            lerp(source, warmAnchor.copy(alpha = source.alpha), clamped * 0.45f)
        return palette.copy(
            focalInner = blend(palette.focalInner),
            focalOuter = blend(palette.focalOuter),
            ringPrimary = blend(palette.ringPrimary)
        )
    }
}
