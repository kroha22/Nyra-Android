package com.nyra.app.android.core.atmosphere.layers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.nyra.app.android.core.atmosphere.model.EmotionalRibbonState
import com.nyra.app.android.core.atmosphere.model.EmotionalRingState
import com.nyra.app.android.core.atmosphere.palette.TimeOfDayPalette
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Individual atmospheric layers — one Composable per spec layer.
 *
 * Every layer is independent: it reads only what it needs from the runtime
 * palette + state, draws to the area it's given, and stays out of the way of
 * other layers. The compositor in
 * [com.nyra.app.android.core.atmosphere.runtime.NyraAtmosphericCompositor]
 * stacks them in the canonical order from the spec.
 */

// ─── LAYER 1 — Procedural atmospheric gradient ───────────────────────────────

@Composable
fun BackboneGradientLayer(
    palette: TimeOfDayPalette,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to palette.backboneTop,
                    0.55f to palette.backboneMid,
                    1f to palette.backboneBottom
                )
            )
    )
}

// ─── LAYER 02 — Reflective depth surface ─────────────────────────────────────
//
// The spec's "infinite emotional floor" — a soft dark reflective plane occupying
// the lower portion of the screen with a faint sheen along its upper edge and
// a slow downward fade into the backbone. Reads as abstract emotional depth
// rather than literal water / landscape.

@Composable
fun ReflectiveDepthSurfaceLayer(
    palette: TimeOfDayPalette,
    floorYFraction: Float = 0.62f,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val floorY = h * floorYFraction.coerceIn(0.4f, 0.85f)

        // Thin upper sheen — a single horizontal gradient line that suggests the
        // edge of the reflective plane catching distant emotional light.
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    palette.focalInner.copy(alpha = palette.focalInner.alpha * 0.45f),
                    palette.focalOuter.copy(alpha = palette.focalOuter.alpha * 0.30f),
                    Color.Transparent
                ),
                startX = 0f,
                endX = w
            ),
            topLeft = Offset(0f, floorY - 1f),
            size = androidx.compose.ui.geometry.Size(w, 2f)
        )

        // Downward fade below the sheen — pulls the eye into atmospheric depth.
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    palette.backboneMid.copy(alpha = 0.0f),
                    palette.backboneMid.copy(alpha = palette.backboneMid.alpha * 0.35f),
                    palette.backboneBottom.copy(alpha = palette.backboneBottom.alpha * 0.55f)
                ),
                startY = floorY,
                endY = h
            ),
            topLeft = Offset(0f, floorY)
        )

        // Soft warm-ish reflection bleed near the floor centre — the "infinite
        // emotional floor" feeling without any aquatic literalism.
        val reflectionCenter = Offset(w * 0.5f, floorY + (h - floorY) * 0.18f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    palette.focalInner.copy(alpha = palette.focalInner.alpha * 0.18f),
                    Color.Transparent
                ),
                center = reflectionCenter,
                radius = w * 0.45f
            ),
            radius = w * 0.45f,
            center = reflectionCenter
        )
    }
}

// ─── LAYER 2 — Horizon glow ──────────────────────────────────────────────────

@Composable
fun HorizonGlowLayer(
    palette: TimeOfDayPalette,
    centerYFraction: Float = 0.72f,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val center = Offset(w * 0.5f, h * centerYFraction)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(palette.focalInner, palette.focalOuter, Color.Transparent),
                center = center,
                radius = w * 0.85f
            ),
            radius = w * 0.85f,
            center = center
        )
    }
}

// ─── LAYER 3 — Fog diffusion ─────────────────────────────────────────────────

@Composable
fun FogDiffusionLayer(
    palette: TimeOfDayPalette,
    intensity: Float = 1.0f,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val center = Offset(w * 0.5f, h * 0.45f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    palette.fog.copy(alpha = palette.fog.alpha * intensity.coerceIn(0f, 1.5f)),
                    Color.Transparent
                ),
                center = center,
                radius = w * 0.7f
            ),
            radius = w * 0.7f,
            center = center
        )
    }
}

// ─── LAYER 4 — Atmospheric rings ─────────────────────────────────────────────

@Composable
fun AtmosphericRingsLayer(
    palette: TimeOfDayPalette,
    state: EmotionalRingState,
    driftPhase: Float,
    pulse: Float,
    modifier: Modifier = Modifier
) {
    val params = state.parameters
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val baseRadius = min(w, h) * 0.30f * params.radiusMultiplier
        val center = Offset(
            x = w * (0.45f + params.centerXJitter * sin(driftPhase * PI.toFloat() * 2f)),
            y = h * params.centerYFraction
        )

        repeat(params.strokeCount) { traceIndex ->
            val traceRadius = baseRadius * (1f + traceIndex * 0.08f) * pulse
            val traceY = center.y - h * params.verticalLift * traceIndex
            val traceCenter = Offset(center.x, traceY)
            val alpha = params.strokeAlpha * (1f - traceIndex * 0.18f).coerceAtLeast(0.25f)
            val color = palette.ringPrimary.copy(alpha = palette.ringPrimary.alpha * alpha)

            if (params.asymmetry > 0f) {
                // Overwhelmed: draw arcs not full circles, offset around the ring.
                val sweepDegrees = 360f * (1f - params.asymmetry)
                val startDegrees = traceIndex * 47f
                drawArc(
                    color = color,
                    startAngle = startDegrees,
                    sweepAngle = sweepDegrees,
                    useCenter = false,
                    topLeft = Offset(traceCenter.x - traceRadius, traceCenter.y - traceRadius),
                    size = androidx.compose.ui.geometry.Size(traceRadius * 2, traceRadius * 2),
                    style = Stroke(width = 1.2f + params.blurAmount * 0.25f)
                )
            } else {
                drawCircle(
                    color = color,
                    radius = traceRadius,
                    center = traceCenter,
                    style = Stroke(width = 1.2f + params.blurAmount * 0.25f)
                )
            }
        }

        if (params.outerHalo) {
            drawCircle(
                color = palette.ringSecondary,
                radius = baseRadius * 1.18f * pulse,
                center = center,
                style = Stroke(width = 0.8f)
            )
        }
    }
}

// ─── LAYER 5 — Flow ribbons ──────────────────────────────────────────────────

@Composable
fun FlowRibbonsLayer(
    palette: TimeOfDayPalette,
    state: EmotionalRibbonState,
    driftPhase: Float,
    modifier: Modifier = Modifier
) {
    val p = state.parameters
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val baseAmplitude = h * 0.05f * p.amplitudeMultiplier
        val baseY = h * 0.42f
        val color = palette.ribbon.copy(
            alpha = (palette.ribbon.alpha * p.alphaMultiplier).coerceIn(0f, 1f)
        )
        val segments = 56

        repeat(p.ribbonCount) { ribbon ->
            val verticalOffset = h * 0.06f * ribbon + h * p.verticalDrift * ribbon
            var prev = Offset(0f, baseY + verticalOffset)
            repeat(segments) { i ->
                val t = (i + 1).toFloat() / segments
                val x = w * t
                val wave = baseAmplitude * sin(
                    (t * 3.2f + driftPhase * p.speedMultiplier + ribbon * 0.4f) * PI.toFloat() * 2f
                )
                val jitter = if (p.instability > 0f) {
                    p.instability * h * 0.012f * sin(
                        (t * 17f + ribbon * 3.1f + driftPhase * 4f) * PI.toFloat() * 2f
                    )
                } else 0f
                val next = Offset(x, baseY + verticalOffset + wave + jitter)
                drawLine(color = color, start = prev, end = next, strokeWidth = 1.4f)
                prev = next
            }
        }
    }
}

// ─── LAYER 6 — Symbolic topology ─────────────────────────────────────────────

@Composable
fun SymbolicTopologyLayer(
    palette: TimeOfDayPalette,
    intensity: Int,
    modifier: Modifier = Modifier
) {
    if (intensity <= 0) return
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // Density grows with intensity: 6 anchors at level 1, 8 at level 2, 10 at level 3.
        val anchors = when (intensity) {
            1 -> baseAnchors(w, h).take(6)
            2 -> baseAnchors(w, h).take(8)
            else -> baseAnchors(w, h)
        }
        val edges = topologyEdges(anchors.size)
        val lineAlpha = (palette.topology.alpha * (0.55f + 0.20f * intensity)).coerceIn(0f, 1f)
        val lineColor = palette.topology.copy(alpha = lineAlpha)
        val dotColor = palette.topology.copy(alpha = (lineAlpha * 1.6f).coerceAtMost(1f))

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
}

private fun baseAnchors(w: Float, h: Float): List<Offset> = listOf(
    Offset(w * 0.18f, h * 0.24f),
    Offset(w * 0.42f, h * 0.18f),
    Offset(w * 0.68f, h * 0.26f),
    Offset(w * 0.86f, h * 0.42f),
    Offset(w * 0.32f, h * 0.54f),
    Offset(w * 0.60f, h * 0.50f),
    Offset(w * 0.78f, h * 0.66f),
    Offset(w * 0.22f, h * 0.70f),
    Offset(w * 0.48f, h * 0.76f),
    Offset(w * 0.74f, h * 0.30f)
)

private fun topologyEdges(n: Int): List<Pair<Int, Int>> {
    // Deterministic adjacency — close-by anchors connect, far ones do not.
    val all = listOf(
        0 to 1, 1 to 2, 2 to 3, 0 to 4, 1 to 5, 2 to 5,
        4 to 5, 5 to 6, 3 to 6, 6 to 7, 4 to 7,
        4 to 8, 5 to 8, 7 to 8, 2 to 9, 5 to 9
    )
    return all.filter { (a, b) -> a < n && b < n }
}

// ─── LAYER 8 — Particles ─────────────────────────────────────────────────────

@Composable
fun ParticlesLayer(
    palette: TimeOfDayPalette,
    density: Float,
    modifier: Modifier = Modifier
) {
    val safeDensity = density.coerceIn(0f, 1f)
    if (safeDensity <= 0.01f) return
    Canvas(modifier = modifier.fillMaxSize()) {
        drawParticles(palette = palette, density = safeDensity)
    }
}

private fun DrawScope.drawParticles(palette: TimeOfDayPalette, density: Float) {
    val w = size.width
    val h = size.height
    val count = (10 + density * 36).toInt()
    val color = palette.focalOuter.copy(
        alpha = (0.10f + density * 0.10f).coerceIn(0f, 0.3f)
    )
    repeat(count) { index ->
        val nx = ((index * 37) % 100) / 100f
        val ny = ((index * 53) % 100) / 100f
        val radius = 1.0f + (index % 5) * 0.5f
        // Light depth variation through cos so the field doesn't look striped.
        val depthOffset = 4f * cos(index.toFloat() * 0.7f)
        drawCircle(
            color = color,
            radius = radius,
            center = Offset(w * nx + depthOffset, h * ny + depthOffset)
        )
    }
}
