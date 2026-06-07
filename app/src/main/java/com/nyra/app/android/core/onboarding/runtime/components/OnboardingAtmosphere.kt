package com.nyra.app.android.core.onboarding.runtime.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.nyra.app.android.core.onboarding.model.AtmosphereVariant
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Atmospheric backdrop for the onboarding flow.
 *
 * Procedural — no PNGs. Mirrors `EmotionalAtmosphereBackground` (the Home
 * version) but exposes a small set of [AtmosphereVariant] presets so the
 * spec's three visual directions live as data, not duplicated Composables:
 *
 *  - [AtmosphereVariant.HorizonDawn]      — open horizon, balanced ring drift.
 *  - [AtmosphereVariant.WarmInterior]     — warm side-lit gradient, quieter ring.
 *  - [AtmosphereVariant.StillnightMinimal]— deeper darks, almost no motion.
 */
/**
 * @param calibrationIntensity 0 = base atmosphere only; 1 = +ribbons gently emerging;
 * 2 = +atmospheric reaction (ring breathes warmer); 3 = +very light emotional
 * topology. Higher levels include lower ones. `null` means "not calibrating".
 */
@Composable
fun OnboardingAtmosphere(
    variant: AtmosphereVariant,
    calibrationIntensity: Int? = null,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val transition = rememberInfiniteTransition(label = "onboard_atmosphere")
    val driftSpeed = variant.driftSpeed()
    val ringPhase = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (32_000 / max(0.4f, driftSpeed)).toInt()),
            repeatMode = RepeatMode.Restart
        ),
        label = "onboard_ring"
    )
    val ribbonPhase = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (44_000 / max(0.4f, driftSpeed)).toInt()),
            repeatMode = RepeatMode.Restart
        ),
        label = "onboard_ribbons"
    )
    val intensity = (calibrationIntensity ?: 0).coerceIn(0, 3)

    Box(modifier = modifier.fillMaxSize()) {
        // Layer 1 — backbone gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = variant.backboneBrush())
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Layer 2 — focal horizon glow (position depends on variant)
            val focal = variant.focalCenter(w, h)
            // Stage 2+ subtly warms the focal halo as the atmosphere "reacts".
            val reactionWarming = if (intensity >= 2) 1.20f else 1.0f
            val focalAlpha = variant.focalAlpha() * reactionWarming
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NyraNocturnePalette.PeachDawn
                            .toComposeColor(alphaMultiplier = 0.18f * focalAlpha),
                        NyraNocturnePalette.AuroraRose
                            .toComposeColor(alphaMultiplier = 0.10f * focalAlpha),
                        Color.Transparent
                    ),
                    center = focal,
                    radius = w * 0.85f
                ),
                radius = w * 0.85f,
                center = focal
            )

            // Layer 3 — atmospheric ring
            // Stage 2 increases the breath amplitude — feels like recognition.
            val pulseAmplitude = if (intensity >= 2) 0.055f else 0.03f
            val ringPulse = 1f + pulseAmplitude * sin(ringPhase.value * Math.PI.toFloat() * 2f)
            val ringCenter = Offset(
                x = w * (0.45f + 0.08f * sin(ringPhase.value * Math.PI.toFloat() * 2f)),
                y = h * variant.ringYFraction()
            )
            val ringRadius = (min(w, h) * variant.ringSizeFraction()) * ringPulse
            drawCircle(
                color = NyraNocturnePalette.RingPrimary
                    .toComposeColor(alphaMultiplier = variant.ringAlpha()),
                radius = ringRadius,
                center = ringCenter,
                style = Stroke(width = 1.2f)
            )
            if (variant != AtmosphereVariant.StillnightMinimal) {
                drawCircle(
                    color = NyraNocturnePalette.RingSecondary
                        .toComposeColor(alphaMultiplier = variant.ringAlpha() * 0.8f),
                    radius = ringRadius * 1.18f,
                    center = ringCenter,
                    style = Stroke(width = 0.8f)
                )
            }

            // Layer 4 — soft fog mid-frame
            val fogCenter = Offset(w * 0.5f, h * 0.45f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NyraNocturnePalette.HorizonLavender
                            .toComposeColor(alphaMultiplier = 0.06f * variant.fogIntensity()),
                        Color.Transparent
                    ),
                    center = fogCenter,
                    radius = w * 0.7f
                ),
                radius = w * 0.7f,
                center = fogCenter
            )

            // ─── Calibration evolution overlays ───────────────────────────────
            if (intensity >= 1) {
                drawSoftRibbons(w, h, ribbonPhase.value, intensity)
            }
            if (intensity >= 3) {
                drawEmotionalTopology(w, h)
            }
        }

        content()
    }
}

/**
 * Slow silk-like ribbons drawn as sinusoidal line segments across the mid-frame.
 * Alpha scales with stage so they fade in rather than pop.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawSoftRibbons(
    w: Float,
    h: Float,
    phase: Float,
    intensity: Int
) {
    val baseAlpha = when (intensity) {
        1 -> 0.06f
        2 -> 0.09f
        else -> 0.11f
    }
    val color = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = baseAlpha)
    val ribbons = 3
    val segments = 56
    val amplitude = h * 0.05f

    for (ribbon in 0 until ribbons) {
        val baseY = h * (0.38f + ribbon * 0.06f)
        var prev = Offset(0f, baseY)
        repeat(segments) { i ->
            val t = (i + 1).toFloat() / segments
            val x = w * t
            val y = baseY + amplitude * sin(
                (t * 3.2f + phase + ribbon * 0.4f) * Math.PI.toFloat() * 2f
            )
            val next = Offset(x, y)
            drawLine(color = color, start = prev, end = next, strokeWidth = 1.4f)
            prev = next
        }
    }
}

/**
 * Very light "emotional topology" — a sparse, organic dot lattice with thin
 * connecting lines. Deliberately not analytic / not technical: no labels, no
 * grid, no axes. Reads as a faint constellation of feeling.
 */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawEmotionalTopology(
    w: Float,
    h: Float
) {
    val lineColor = NyraNocturnePalette.TopologyLine.toComposeColor()
    val dotColor = NyraNocturnePalette.CosmicDust.toComposeColor(alphaMultiplier = 0.18f)
    val anchors = listOf(
        Offset(w * 0.18f, h * 0.24f),
        Offset(w * 0.42f, h * 0.18f),
        Offset(w * 0.68f, h * 0.26f),
        Offset(w * 0.86f, h * 0.42f),
        Offset(w * 0.32f, h * 0.54f),
        Offset(w * 0.60f, h * 0.50f),
        Offset(w * 0.78f, h * 0.66f),
        Offset(w * 0.22f, h * 0.70f)
    )
    val edges = listOf(
        0 to 1, 1 to 2, 2 to 3, 0 to 4, 1 to 5, 2 to 5,
        4 to 5, 5 to 6, 3 to 6, 6 to 7, 4 to 7
    )
    edges.forEach { (a, b) ->
        drawLine(
            color = lineColor,
            start = anchors[a],
            end = anchors[b],
            strokeWidth = 0.7f
        )
    }
    anchors.forEach { p -> drawCircle(color = dotColor, radius = 1.6f, center = p) }
}

// ─── Per-variant tuning ──────────────────────────────────────────────────────

private fun AtmosphereVariant.driftSpeed(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 1.0f
    AtmosphereVariant.WarmInterior -> 0.7f
    AtmosphereVariant.StillnightMinimal -> 0.45f
}

private fun AtmosphereVariant.ringYFraction(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 0.28f
    AtmosphereVariant.WarmInterior -> 0.22f
    AtmosphereVariant.StillnightMinimal -> 0.20f
}

private fun AtmosphereVariant.ringSizeFraction(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 0.30f
    AtmosphereVariant.WarmInterior -> 0.26f
    AtmosphereVariant.StillnightMinimal -> 0.22f
}

private fun AtmosphereVariant.ringAlpha(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 1.0f
    AtmosphereVariant.WarmInterior -> 0.7f
    AtmosphereVariant.StillnightMinimal -> 0.45f
}

private fun AtmosphereVariant.focalAlpha(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 1.0f
    AtmosphereVariant.WarmInterior -> 1.4f      // warmer / closer feeling
    AtmosphereVariant.StillnightMinimal -> 0.5f
}

private fun AtmosphereVariant.focalCenter(w: Float, h: Float): Offset = when (this) {
    AtmosphereVariant.HorizonDawn -> Offset(w * 0.5f, h * 0.72f)
    AtmosphereVariant.WarmInterior -> Offset(w * 0.78f, h * 0.55f)  // warm window
    AtmosphereVariant.StillnightMinimal -> Offset(w * 0.5f, h * 0.78f)
}

private fun AtmosphereVariant.fogIntensity(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 1.0f
    AtmosphereVariant.WarmInterior -> 1.2f
    AtmosphereVariant.StillnightMinimal -> 0.5f
}

private fun AtmosphereVariant.backboneBrush(): Brush = when (this) {
    AtmosphereVariant.HorizonDawn -> Brush.verticalGradient(
        0f to NyraNocturnePalette.VoidNavy.toComposeColor(),
        0.55f to NyraNocturnePalette.DeepSpaceIndigo.toComposeColor(),
        1f to NyraNocturnePalette.AtmosphericViolet.toComposeColor(alphaMultiplier = 0.85f)
    )
    AtmosphereVariant.WarmInterior -> Brush.verticalGradient(
        0f to NyraNocturnePalette.VoidNavy.toComposeColor(),
        0.65f to NyraNocturnePalette.AtmosphericViolet.toComposeColor(alphaMultiplier = 0.6f),
        1f to NyraNocturnePalette.AuroraRose.toComposeColor(alphaMultiplier = 0.15f)
    )
    AtmosphereVariant.StillnightMinimal -> Brush.verticalGradient(
        0f to NyraNocturnePalette.VoidNavy.toComposeColor(),
        1f to NyraNocturnePalette.DeepSpaceIndigo.toComposeColor()
    )
}

private fun Long.toComposeColor(alphaMultiplier: Float = 1f): Color {
    val alpha = (((this ushr 24) and 0xFF).toFloat() / 255f * alphaMultiplier).coerceIn(0f, 1f)
    val red = ((this ushr 16) and 0xFF).toFloat() / 255f
    val green = ((this ushr 8) and 0xFF).toFloat() / 255f
    val blue = (this and 0xFF).toFloat() / 255f
    return Color(red = red, green = green, blue = blue, alpha = alpha)
}
