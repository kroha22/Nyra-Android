package com.nyra.app.android.core.ui_state.runtime

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
import com.nyra.app.android.core.profile.model.NyraHomeStateLevel
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Multi-layered atmospheric background for the Adaptive Home Screen.
 *
 * Renders procedurally — no PNGs, no Lottie. Layers stack from back to front:
 *
 *  1. **Deep vertical fade** (VoidNavy → DeepSpaceIndigo → AtmosphericViolet)
 *     forming the Warm Nocturne backbone.
 *  2. **Horizon glow** — distant warm halo low in the frame, the "Warm Horizon"
 *     focal anchor.
 *  3. **Atmospheric ring** — single thin volumetric ring drifting slowly. The
 *     spec's "single emotional focal object". Always present from State 0.
 *  4. **Reflective floor sheen** — subtle horizontal gradient at the bottom
 *     simulating the reflective horizon surface.
 *  5. **Soft fog diffusion** — radial soft-light haze in the middle third.
 *  6. **Flow ribbons** — added at [NyraHomeStateLevel.TrackersEnabled] and
 *     above. Slow silk-like sinusoidal lines.
 *  7. **Symbolic topology** — added at [NyraHomeStateLevel.NatalEnabled] and
 *     above. Sparse constellation-like dot lattice.
 *
 *  Motion timings come from the resolved [NyraUiStateConfig]; we never animate
 *  faster than what time-of-day modulation allows.
 */
@Composable
fun EmotionalAtmosphereBackground(
    uiState: NyraUiStateConfig,
    level: NyraHomeStateLevel,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val transition = rememberInfiniteTransition(label = "nyra_atmosphere")
    val driftBase = max(0.05f, uiState.motion.driftSpeed.coerceAtMost(1.5f))
    val ringDriftMs = (28_000 / driftBase).toInt().coerceIn(12_000, 60_000)
    val ribbonDriftMs = (40_000 / driftBase).toInt().coerceIn(18_000, 90_000)

    val ringPhase = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ringDriftMs),
            repeatMode = RepeatMode.Restart
        ),
        label = "nyra_ring_phase"
    )

    val ribbonPhase = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ribbonDriftMs),
            repeatMode = RepeatMode.Restart
        ),
        label = "nyra_ribbon_phase"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Layer 1 — Warm Nocturne backbone
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to NyraNocturnePalette.VoidNavy.toComposeColor(),
                        0.55f to NyraNocturnePalette.DeepSpaceIndigo.toComposeColor(),
                        1f to NyraNocturnePalette.AtmosphericViolet
                            .toComposeColor(alphaMultiplier = 0.85f)
                    )
                )
        )

        // Layers 2–7 share a single Canvas to minimise overdraw.
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // ─── Layer 2 — distant horizon glow ──────────────────────────────
            val horizonCenter = Offset(x = w * 0.5f, y = h * 0.72f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NyraNocturnePalette.PeachDawn
                            .toComposeColor(alphaMultiplier = 0.20f),
                        NyraNocturnePalette.AuroraRose
                            .toComposeColor(alphaMultiplier = 0.10f),
                        Color.Transparent
                    ),
                    center = horizonCenter,
                    radius = w * 0.85f
                ),
                radius = w * 0.85f,
                center = horizonCenter
            )

            // ─── Layer 5 — soft fog diffusion (mid-frame haze) ───────────────
            val fogCenter = Offset(x = w * 0.5f, y = h * 0.48f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NyraNocturnePalette.HorizonLavender
                            .toComposeColor(alphaMultiplier = 0.08f),
                        Color.Transparent
                    ),
                    center = fogCenter,
                    radius = w * 0.7f
                ),
                radius = w * 0.7f,
                center = fogCenter
            )

            // ─── Layer 4 — reflective floor sheen ────────────────────────────
            val sheenTop = h * 0.78f
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        NyraNocturnePalette.HorizonLavender
                            .toComposeColor(alphaMultiplier = 0.06f),
                        NyraNocturnePalette.AuroraRose
                            .toComposeColor(alphaMultiplier = 0.04f)
                    ),
                    startY = sheenTop,
                    endY = h
                ),
                topLeft = Offset(0f, sheenTop)
            )

            // ─── Layer 3 — atmospheric ring ──────────────────────────────────
            // Drifts very slowly across the upper third; size pulses subtly.
            val ringPulse = 1f + 0.04f * sin(ringPhase.value * Math.PI.toFloat() * 2f)
            val ringCenter = Offset(
                x = w * (0.42f + 0.10f * sin(ringPhase.value * Math.PI.toFloat() * 2f)),
                y = h * 0.30f
            )
            val ringRadius = (min(w, h) * 0.30f) * ringPulse
            drawCircle(
                color = NyraNocturnePalette.RingPrimary.toComposeColor(),
                radius = ringRadius,
                center = ringCenter,
                style = Stroke(width = 1.2f)
            )
            drawCircle(
                color = NyraNocturnePalette.RingSecondary.toComposeColor(),
                radius = ringRadius * 1.18f,
                center = ringCenter,
                style = Stroke(width = 0.8f)
            )

            // ─── Layer 6 — flow ribbons (TrackersEnabled+) ───────────────────
            if (level.atLeast(NyraHomeStateLevel.TrackersEnabled)) {
                drawFlowRibbons(
                    width = w,
                    height = h,
                    phase = ribbonPhase.value
                )
            }

            // ─── Layer 7 — symbolic topology (NatalEnabled+) ─────────────────
            if (level.atLeast(NyraHomeStateLevel.NatalEnabled)) {
                drawTopologyLattice(width = w, height = h)
            }
        }

        content()
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawFlowRibbons(
    width: Float,
    height: Float,
    phase: Float
) {
    val baseY = height * 0.42f
    val amplitude = height * 0.04f
    val points = 64
    val color = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = 0.10f)

    for (ribbon in 0..1) {
        val offset = ribbon * height * 0.06f
        var prev = Offset(0f, baseY + offset)
        repeat(points) { i ->
            val t = (i + 1).toFloat() / points
            val x = width * t
            val y = baseY + offset +
                amplitude * sin((t * 4f + phase + ribbon * 0.5f) * Math.PI.toFloat() * 2f)
            val next = Offset(x, y)
            drawLine(
                color = color,
                start = prev,
                end = next,
                strokeWidth = 1.4f
            )
            prev = next
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawTopologyLattice(
    width: Float,
    height: Float
) {
    // Deterministic sparse dot lattice — looks organic, doesn't animate.
    val color = NyraNocturnePalette.TopologyLine.toComposeColor()
    val dotColor = NyraNocturnePalette.CosmicDust.toComposeColor(alphaMultiplier = 0.15f)

    val anchors = listOf(
        Offset(width * 0.18f, height * 0.20f),
        Offset(width * 0.42f, height * 0.14f),
        Offset(width * 0.66f, height * 0.22f),
        Offset(width * 0.82f, height * 0.38f),
        Offset(width * 0.30f, height * 0.50f),
        Offset(width * 0.58f, height * 0.46f),
        Offset(width * 0.74f, height * 0.62f),
        Offset(width * 0.22f, height * 0.66f)
    )
    val edges = listOf(
        0 to 1, 1 to 2, 2 to 3, 0 to 4, 1 to 5, 2 to 5,
        4 to 5, 5 to 6, 3 to 6, 6 to 7, 4 to 7
    )

    edges.forEach { (a, b) ->
        drawLine(
            color = color,
            start = anchors[a],
            end = anchors[b],
            strokeWidth = 0.7f
        )
    }
    anchors.forEach { p ->
        drawCircle(color = dotColor, radius = 1.6f, center = p)
    }
}

// Note: `Long.toComposeColor()` lives in `NyraAdaptiveRuntime.kt` (same package)
// and is shared across all runtime Composables.
