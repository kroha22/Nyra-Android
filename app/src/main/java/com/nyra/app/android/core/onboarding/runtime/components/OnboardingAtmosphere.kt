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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.nyra.app.android.core.onboarding.model.AtmosphereVariant
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Calibration backdrop — **V1.0** cinematic atmospheric clarity rebuild.
 *
 * Key V1.0 changes vs the previous version:
 *  - **Lavender / purple dominance reduced ~55%.** Backbone is deep navy →
 *    midnight indigo → deep atmosphere blue. Smoky violet appears only in the
 *    upper-mid stop, never as a full-frame tint.
 *  - **Single focal emotional light.** The warm peach glow is now a small,
 *    contained ring + halo (~28% of `min(w,h)`) rather than a 95% width
 *    haze. It reads as *focal*, not *ambient*.
 *  - **Fog moves to edges and corners.** Three soft radial gradients live at
 *    top-left, top-right, and bottom corners; the centre stays clean so UI
 *    typography reads sharply.
 *  - **Topology replaced by soft resonance arcs.** Smooth cubic-bezier curves
 *    drift across the lower-middle frame at ultra-low alpha — emotional
 *    resonance, not constellation / tech graph.
 *  - **Ribbons clamped to 5–12% alpha.** They sit behind everything and stop
 *    competing with UI.
 *  - **Reflective depth plane.** A subtle sheen line + downward fade in the
 *    lower third gives the spec's "infinite emotional depth" without water
 *    realism.
 *
 * The atmosphere now sits on a plane *behind* the UI layer, not on top of it.
 *
 * @param calibrationIntensity 0..3. 0 = base atmosphere only; 1 = +ribbons
 *   emerging; 2 = +atmospheric reaction (focal warms, ribbons slightly more
 *   alpha); 3 = +emotional resonance arcs. `null` means "not calibrating".
 */
@Composable
fun OnboardingAtmosphere(
    variant: AtmosphereVariant,
    calibrationIntensity: Int? = null,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val transition = rememberInfiniteTransition(label = "calibration_atmosphere_v1")
    val driftSpeed = variant.driftSpeed()
    val intensity = (calibrationIntensity ?: 0).coerceIn(0, 3)

    // 28s base loop, divided by drift speed so calmer variants slow further.
    // Per V1.0 motion spec: 20–40s loops, very low amplitude.
    val ringPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (28_000 / max(0.4f, driftSpeed)).toInt()),
            repeatMode = RepeatMode.Restart
        ),
        label = "calib_ring_phase"
    )
    // Slow breath of the focal ring — 14s reverse, 1.5% amplitude.
    val ringBreath by transition.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14_000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "calib_ring_breath"
    )
    val ribbonPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = (38_000 / max(0.4f, driftSpeed)).toInt()),
            repeatMode = RepeatMode.Restart
        ),
        label = "calib_ribbon_phase"
    )
    val arcPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 32_000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "calib_arc_phase"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // ─── Layer 01 — Base gradient (clean, deep navy, no texture) ────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = variant.backboneBrush())
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // ─── Layer 02 — Reflective depth plane (lower third) ────────────
            drawReflectiveDepthPlane(w, h, variant)

            // ─── Layer 03 — Single focal emotional light ────────────────────
            val reactionBoost = if (intensity >= 2) 1.18f else 1.0f
            drawFocalRing(
                w = w,
                h = h,
                variant = variant,
                driftPhase = ringPhase,
                breath = ringBreath,
                reactionBoost = reactionBoost
            )

            // ─── Layer 04 — Corner fog only (centre stays clean) ────────────
            drawCornerFog(w, h, variant)

            // ─── Layer 05 — Soft ribbons (5–12% alpha) ──────────────────────
            if (intensity >= 1) {
                drawSoftRibbons(w, h, ribbonPhase, intensity)
            }

            // ─── Layer 06 — Emotional resonance arcs (replaces topology) ────
            if (intensity >= 3) {
                drawResonanceArcs(w, h, arcPhase)
            }
        }

        content()
    }
}

// ─── Layer 02 — Reflective depth plane ──────────────────────────────────────

private fun DrawScope.drawReflectiveDepthPlane(w: Float, h: Float, variant: AtmosphereVariant) {
    val sheenY = h * 0.68f

    // Thin horizontal sheen line — abstract reflective surface edge.
    drawRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                NyraCalibrationPaletteV1.ReflectiveSheen.toColor(),
                NyraCalibrationPaletteV1.ReflectiveSheen.toColor(alphaMultiplier = 0.6f),
                Color.Transparent
            )
        ),
        topLeft = Offset(0f, sheenY - 0.5f),
        size = Size(w, 1.5f)
    )

    // Downward fade — pulls the eye into emotional depth.
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                NyraCalibrationPaletteV1.VoidNavy.toColor(alphaMultiplier = 0.35f),
                NyraCalibrationPaletteV1.VoidNavy.toColor(alphaMultiplier = 0.7f)
            ),
            startY = sheenY,
            endY = h
        ),
        topLeft = Offset(0f, sheenY)
    )

    // Soft warm reflection bleed near centre — emotional warmth, not water.
    if (variant != AtmosphereVariant.StillnightMinimal) {
        val bleedCenter = Offset(w * 0.5f, sheenY + (h - sheenY) * 0.22f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NyraCalibrationPaletteV1.WarmPeachGlow.toColor(alphaMultiplier = 0.08f),
                    Color.Transparent
                ),
                center = bleedCenter,
                radius = w * 0.42f
            ),
            radius = w * 0.42f,
            center = bleedCenter
        )
    }
}

// ─── Layer 03 — Single focal emotional light ────────────────────────────────

private fun DrawScope.drawFocalRing(
    w: Float,
    h: Float,
    variant: AtmosphereVariant,
    driftPhase: Float,
    breath: Float,
    reactionBoost: Float
) {
    val center = Offset(
        x = w * (0.50f + 0.03f * sin(driftPhase * PI.toFloat() * 2f)),
        y = h * variant.focalYFraction()
    )
    val baseRadius = min(w, h) * 0.28f * breath

    // Wide low-opacity bloom — separates ring from atmosphere.
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                NyraCalibrationPaletteV1.FocalRingHalo.toColor(
                    alphaMultiplier = reactionBoost
                ),
                Color.Transparent
            ),
            center = center,
            radius = baseRadius * 1.9f
        ),
        radius = baseRadius * 1.9f,
        center = center
    )

    // Primary ring stroke.
    drawCircle(
        color = NyraCalibrationPaletteV1.FocalRingStroke.toColor(
            alphaMultiplier = reactionBoost
        ),
        radius = baseRadius,
        center = center,
        style = Stroke(width = 1.3f)
    )

    // Inner brighter edge — the single brightest accent on the screen.
    drawCircle(
        color = NyraCalibrationPaletteV1.WarmPeachGlow.toColor(
            alphaMultiplier = 0.65f * reactionBoost
        ),
        radius = baseRadius * 0.985f,
        center = center,
        style = Stroke(width = 0.8f)
    )
}

// ─── Layer 04 — Corner fog (NOT centre) ─────────────────────────────────────

private fun DrawScope.drawCornerFog(w: Float, h: Float, variant: AtmosphereVariant) {
    val cool = NyraCalibrationPaletteV1.FogCool.toColor(
        alphaMultiplier = variant.fogIntensity()
    )
    val warm = NyraCalibrationPaletteV1.FogWarm.toColor(
        alphaMultiplier = variant.fogIntensity()
    )

    // Top-left corner.
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(cool, Color.Transparent),
            center = Offset(0f, 0f),
            radius = w * 0.45f
        ),
        radius = w * 0.45f,
        center = Offset(0f, 0f)
    )
    // Top-right corner.
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(cool, Color.Transparent),
            center = Offset(w, 0f),
            radius = w * 0.45f
        ),
        radius = w * 0.45f,
        center = Offset(w, 0f)
    )
    // Bottom-left.
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(warm, Color.Transparent),
            center = Offset(0f, h),
            radius = w * 0.40f
        ),
        radius = w * 0.40f,
        center = Offset(0f, h)
    )
    // Bottom-right.
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(warm, Color.Transparent),
            center = Offset(w, h),
            radius = w * 0.40f
        ),
        radius = w * 0.40f,
        center = Offset(w, h)
    )
}

// ─── Layer 05 — Soft ribbons (5–12% alpha) ──────────────────────────────────

private fun DrawScope.drawSoftRibbons(
    w: Float,
    h: Float,
    phase: Float,
    intensity: Int
) {
    val alphaBoost = when (intensity) {
        1 -> 0.6f       // ~5% effective alpha — barely there
        2 -> 1.0f       // ~9%
        else -> 1.4f    // ~12% — top of the spec range
    }
    val color = NyraCalibrationPaletteV1.RibbonStroke.toColor(alphaMultiplier = alphaBoost)
    val ribbons = 3
    val segments = 56
    val amplitude = h * 0.045f

    for (ribbon in 0 until ribbons) {
        val baseY = h * (0.40f + ribbon * 0.06f)
        var prev = Offset(0f, baseY)
        repeat(segments) { i ->
            val t = (i + 1).toFloat() / segments
            val x = w * t
            val y = baseY + amplitude * sin(
                (t * 2.6f + phase + ribbon * 0.5f) * PI.toFloat() * 2f
            )
            val next = Offset(x, y)
            drawLine(color = color, start = prev, end = next, strokeWidth = 1.2f)
            prev = next
        }
    }
}

// ─── Layer 06 — Emotional resonance arcs (replaces topology) ────────────────

private fun DrawScope.drawResonanceArcs(w: Float, h: Float, phase: Float) {
    // Four large translucent arcs drifting horizontally on a slow phase.
    // No straight lines, no anchor dots — pure emotional resonance.
    val color = NyraCalibrationPaletteV1.ResonanceArc.toColor()
    val arcs = listOf(
        ArcSpec(yFraction = 0.46f, widthFraction = 1.30f, curveDepth = 0.14f, phaseOffset = 0f),
        ArcSpec(yFraction = 0.52f, widthFraction = 1.15f, curveDepth = 0.10f, phaseOffset = 0.25f),
        ArcSpec(yFraction = 0.58f, widthFraction = 1.40f, curveDepth = 0.16f, phaseOffset = 0.5f),
        ArcSpec(yFraction = 0.64f, widthFraction = 1.20f, curveDepth = 0.12f, phaseOffset = 0.75f)
    )
    arcs.forEach { spec ->
        val drift = (phase + spec.phaseOffset) % 1f
        val arcWidth = w * spec.widthFraction
        val startX = -arcWidth * 0.5f * drift
        val centerX = startX + arcWidth * 0.5f
        val endX = startX + arcWidth
        val baseY = h * spec.yFraction
        val controlY = baseY - h * spec.curveDepth
        val path = Path().apply {
            moveTo(startX, baseY)
            quadraticBezierTo(centerX, controlY, endX, baseY)
        }
        drawPath(path = path, color = color, style = Stroke(width = 0.9f))
    }
}

private data class ArcSpec(
    val yFraction: Float,
    val widthFraction: Float,
    val curveDepth: Float,
    val phaseOffset: Float
)

// ─── Per-variant tuning ──────────────────────────────────────────────────────

private fun AtmosphereVariant.driftSpeed(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 1.0f
    AtmosphereVariant.WarmInterior -> 0.7f
    AtmosphereVariant.StillnightMinimal -> 0.45f
}

private fun AtmosphereVariant.focalYFraction(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 0.30f
    AtmosphereVariant.WarmInterior -> 0.26f
    AtmosphereVariant.StillnightMinimal -> 0.22f
}

private fun AtmosphereVariant.fogIntensity(): Float = when (this) {
    AtmosphereVariant.HorizonDawn -> 1.0f
    AtmosphereVariant.WarmInterior -> 1.2f
    AtmosphereVariant.StillnightMinimal -> 0.55f
}

private fun AtmosphereVariant.backboneBrush(): Brush = when (this) {
    AtmosphereVariant.HorizonDawn -> Brush.verticalGradient(
        0f to NyraCalibrationPaletteV1.VoidNavy.toColor(),
        0.50f to NyraCalibrationPaletteV1.MidnightIndigo.toColor(),
        0.85f to NyraCalibrationPaletteV1.DeepAtmosphereBlue.toColor(),
        1f to NyraCalibrationPaletteV1.VoidNavy.toColor()
    )
    AtmosphereVariant.WarmInterior -> Brush.verticalGradient(
        0f to NyraCalibrationPaletteV1.VoidNavy.toColor(),
        0.45f to NyraCalibrationPaletteV1.MidnightIndigo.toColor(),
        0.75f to NyraCalibrationPaletteV1.SmokyViolet.toColor(alphaMultiplier = 0.85f),
        1f to NyraCalibrationPaletteV1.DeepAtmosphereBlue.toColor()
    )
    AtmosphereVariant.StillnightMinimal -> Brush.verticalGradient(
        0f to NyraCalibrationPaletteV1.VoidNavy.toColor(),
        1f to NyraCalibrationPaletteV1.MidnightIndigo.toColor()
    )
}

// Long.toColor() is provided by AtmosphericComponents.kt (internal extension).
