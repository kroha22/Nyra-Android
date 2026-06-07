package com.nyra.app.android.core.splash.runtime

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyra.app.android.core.atmosphere.palette.NyraHorizonPalette
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

/**
 * HORIZON splash scene — **V1.0** cinematic refinement.
 *
 * Refinements vs the original spec:
 *  - **Eclipse ring** now has 5 stroke / halo tiers (brightest inner edge,
 *    primary, soft inner echo, low-opacity bloom, dusty violet diffusion) so
 *    it sits *forward* of the sky rather than dissolving into it.
 *  - **Mountains** is now a 3-tier system (back / mid / foreground). The new
 *    foreground tier carries the darkest values in the scene and uses sharper
 *    angular shapes for cinematic graphic minimalism.
 *  - **Reflection** replaces the single vertical beam with a wider horizon
 *    bloom + narrow streak + 12 shimmer dots that break up irregularly and
 *    fade naturally into the dark water.
 *  - **Sky** adds vertical haze diffusion + tiny far-distance particles +
 *    deterministic cinematic grain.
 *  - **Character silhouette** gains a subtle rim light on the right edge and
 *    its feet now fade into the foreground haze instead of cutting hard.
 *  - **Typography** moves to lowercase "nyra" per the V1.0 elegance brief.
 *
 * Tonal hierarchy (V1.0):
 *  1. Brightest — ring inner edge + horizon glow
 *  2. Secondary — reflection bloom + shimmer
 *  3. Third     — typography
 *  4. Fourth    — sky particles + fog + grain
 *  5. Darkest   — foreground mountains + bottom of frame
 */
@Composable
fun NyraSplashScene(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "splash_parallax")
    val slowPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30_000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_slow"
    )
    val mediumPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20_000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_medium"
    )
    val fastPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12_000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_fast"
    )
    val ringPulse by transition.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.015f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6_400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_ring_pulse"
    )
    val shimmerPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9_000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_shimmer"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Layer 0 — Base color (vertical gradient backbone).
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to NyraHorizonPalette.IndigoBlack.toColor(),
                        0.55f to NyraHorizonPalette.MidnightViolet.toColor(),
                        1f to NyraHorizonPalette.DarkMauve.toColor()
                    )
                )
        )

        // Layers 1–10 share one Canvas to minimise overdraw.
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Sky depth additions before the focal so the focal sits forward.
            drawSkyVerticalDiffusion(w, h)
            drawSkyDistantParticles(w, h, parallaxX = parallaxOffset(slowPhase, 2f))
            drawCinematicGrain(w, h)

            // 1 — Horizon glow.
            drawHorizonGlow(w, h)

            // 2 — Starfield (subtle deterministic dots, upper sky only).
            drawStarfield(w, h, parallaxX = parallaxOffset(slowPhase, 2f))

            // 3 — Mountains back (very washed out, low contrast).
            drawMountainsBack(w, h, parallaxX = parallaxOffset(mediumPhase, 3f))

            // 4 — Mountains mid (slightly sharper, varied heights).
            drawMountainsMid(w, h, parallaxX = parallaxOffset(mediumPhase, 4f))

            // 4b (NEW V1.0) — Mountains foreground (darkest, angular).
            drawMountainsForeground(w, h, parallaxX = parallaxOffset(fastPhase, 6f))

            // 5 — Water surface (gradient + striations).
            drawWaterSurface(w, h, parallaxX = parallaxOffset(mediumPhase, 4f))

            // 6 (V1.0) — Reflection: wide bloom + narrow streak + shimmer.
            drawReflectionHorizonBloom(w, h)
            drawReflectionStreak(w, h)
            drawShimmerHighlights(w, h, phase = shimmerPhase)

            // 7 (V1.0) — Eclipse ring with brighter inner edge + bloom + halo.
            drawEclipseRing(w, h, pulse = ringPulse)

            // 8 — Atmospheric fog.
            drawAtmosphericFog(w, h, parallaxX = parallaxOffset(slowPhase, 2f))

            // 9 (V1.0) — Character silhouette with rim light + feet fade.
            drawCharacterSilhouette(w, h, parallaxX = parallaxOffset(fastPhase, 8f))

            // 10 — Flow particles (subtle ambient drift in mid-frame).
            drawFlowParticles(w, h, phase = fastPhase)
        }

        // 11 — UI overlay.
        SplashUiOverlay()
    }
}

// ─── Parallax helper ─────────────────────────────────────────────────────────

private fun parallaxOffset(phase: Float, amplitudePx: Float): Float =
    amplitudePx * sin(phase * PI.toFloat() * 2f)

// ─── Sky atmosphere depth (V1.0 additions) ──────────────────────────────────

private fun DrawScope.drawSkyVerticalDiffusion(w: Float, h: Float) {
    // A vertical haze occupying the upper 60% of the frame — gives the sky
    // breathing depth without introducing colour.
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                NyraHorizonPalette.SkyDiffusion.toColor(),
                NyraHorizonPalette.SkyDiffusion.toColor(alphaMultiplier = 0.5f),
                Color.Transparent
            ),
            startY = 0f,
            endY = h * 0.62f
        )
    )
}

private fun DrawScope.drawSkyDistantParticles(w: Float, h: Float, parallaxX: Float) {
    // Many tiny particles, *smaller and dimmer* than the starfield, scattered
    // through the full sky. Reads as atmospheric depth, not stars.
    val color = NyraHorizonPalette.CinematicGrain.toColor(alphaMultiplier = 5.5f)
    val brighterColor = NyraHorizonPalette.CinematicGrain.toColor(alphaMultiplier = 9f)
    repeat(110) { index ->
        val nx = ((index * 191) % 1000) / 1000f
        val ny = ((index * 113) % 1000) / 1000f
        val x = (w * nx + parallaxX).coerceIn(0f, w)
        val y = h * 0.60f * ny
        val radius = if (index % 13 == 0) 0.9f else 0.45f
        drawCircle(
            color = if (index % 13 == 0) brighterColor else color,
            radius = radius,
            center = Offset(x, y)
        )
    }
}

private fun DrawScope.drawCinematicGrain(w: Float, h: Float) {
    // Deterministic ultra-low alpha "film grain" dotted across the whole frame.
    val color = NyraHorizonPalette.CinematicGrain.toColor()
    repeat(180) { index ->
        val nx = ((index * 631) % 1000) / 1000f
        val ny = ((index * 397) % 1000) / 1000f
        drawCircle(color = color, radius = 0.5f, center = Offset(w * nx, h * ny))
    }
}

// ─── Layer 1 — Horizon glow ──────────────────────────────────────────────────

private fun DrawScope.drawHorizonGlow(w: Float, h: Float) {
    val horizonY = h * 0.62f
    val center = Offset(w * 0.5f, horizonY)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                NyraHorizonPalette.PeachAtmospheric.toColor(alphaMultiplier = 0.42f),
                NyraHorizonPalette.PeachAtmospheric.toColor(alphaMultiplier = 0.15f),
                Color.Transparent
            ),
            center = center,
            radius = w * 0.95f
        ),
        radius = w * 0.95f,
        center = center
    )
}

// ─── Layer 2 — Starfield ─────────────────────────────────────────────────────

private fun DrawScope.drawStarfield(w: Float, h: Float, parallaxX: Float) {
    val dotColor = NyraHorizonPalette.StarfieldDot.toColor()
    val ivory = NyraHorizonPalette.IvoryHighlight.toColor(alphaMultiplier = 0.4f)
    val ceiling = h * 0.55f
    repeat(54) { index ->
        val nx = ((index * 73) % 1000) / 1000f
        val ny = ((index * 41) % 1000) / 1000f
        val x = (w * nx + parallaxX).coerceIn(0f, w)
        val y = ceiling * ny
        val radius = when (index % 7) {
            0 -> 1.7f
            1 -> 1.4f
            in 2..4 -> 1.0f
            else -> 0.7f
        }
        val color = if (index % 7 == 0) ivory else dotColor
        drawCircle(color = color, radius = radius, center = Offset(x, y))
    }
}

// ─── Layer 3 — Mountains back ────────────────────────────────────────────────

private fun DrawScope.drawMountainsBack(w: Float, h: Float, parallaxX: Float) {
    // Wide smooth silhouettes that barely separate from the backbone.
    val path = Path().apply {
        moveTo(parallaxX + -w * 0.10f, h * 0.66f)
        lineTo(parallaxX + w * 0.10f, h * 0.62f)
        lineTo(parallaxX + w * 0.22f, h * 0.59f)
        lineTo(parallaxX + w * 0.34f, h * 0.63f)
        lineTo(parallaxX + w * 0.48f, h * 0.57f)
        lineTo(parallaxX + w * 0.60f, h * 0.62f)
        lineTo(parallaxX + w * 0.74f, h * 0.56f)
        lineTo(parallaxX + w * 0.88f, h * 0.61f)
        lineTo(parallaxX + w * 1.10f, h * 0.64f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(path = path, color = NyraHorizonPalette.MountainsBack.toColor())
}

// ─── Layer 4 — Mountains mid ─────────────────────────────────────────────────

private fun DrawScope.drawMountainsMid(w: Float, h: Float, parallaxX: Float) {
    // Slightly sharper, varied heights, still desaturated.
    val path = Path().apply {
        moveTo(parallaxX + -w * 0.05f, h * 0.71f)
        lineTo(parallaxX + w * 0.08f, h * 0.66f)
        lineTo(parallaxX + w * 0.18f, h * 0.69f)
        lineTo(parallaxX + w * 0.27f, h * 0.62f)
        lineTo(parallaxX + w * 0.36f, h * 0.67f)
        lineTo(parallaxX + w * 0.46f, h * 0.61f)
        lineTo(parallaxX + w * 0.55f, h * 0.66f)
        lineTo(parallaxX + w * 0.66f, h * 0.63f)
        lineTo(parallaxX + w * 0.78f, h * 0.67f)
        lineTo(parallaxX + w * 0.90f, h * 0.62f)
        lineTo(parallaxX + w * 1.05f, h * 0.66f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(path = path, color = NyraHorizonPalette.MountainsMid.toColor())
}

// ─── Layer 4b (NEW V1.0) — Mountains foreground ──────────────────────────────

private fun DrawScope.drawMountainsForeground(w: Float, h: Float, parallaxX: Float) {
    // Darkest tier. Angular, asymmetric, cinematic graphic minimalism.
    // Lower silhouette, sharper jagged peaks so the eye reads scene depth.
    val path = Path().apply {
        moveTo(parallaxX + -w * 0.04f, h * 0.79f)
        lineTo(parallaxX + w * 0.06f, h * 0.74f)
        lineTo(parallaxX + w * 0.11f, h * 0.78f)
        lineTo(parallaxX + w * 0.19f, h * 0.71f)        // sharp left-of-centre peak
        lineTo(parallaxX + w * 0.25f, h * 0.76f)
        lineTo(parallaxX + w * 0.32f, h * 0.73f)
        lineTo(parallaxX + w * 0.41f, h * 0.78f)
        lineTo(parallaxX + w * 0.49f, h * 0.69f)        // tallest peak, dominant
        lineTo(parallaxX + w * 0.56f, h * 0.75f)
        lineTo(parallaxX + w * 0.64f, h * 0.71f)
        lineTo(parallaxX + w * 0.74f, h * 0.77f)
        lineTo(parallaxX + w * 0.82f, h * 0.73f)        // right-side jagged
        lineTo(parallaxX + w * 0.93f, h * 0.76f)
        lineTo(parallaxX + w * 1.05f, h * 0.79f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(path = path, color = NyraHorizonPalette.MountainsForeground.toColor())
}

// ─── Layer 5 — Water surface ─────────────────────────────────────────────────

private fun DrawScope.drawWaterSurface(w: Float, h: Float, parallaxX: Float) {
    val waterTop = h * 0.71f
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                NyraHorizonPalette.WaterSheen.toColor(),
                NyraHorizonPalette.DarkMauve.toColor(alphaMultiplier = 0.85f),
                NyraHorizonPalette.IndigoBlack.toColor()
            ),
            startY = waterTop,
            endY = h
        ),
        topLeft = Offset(0f, waterTop)
    )
    // Subtle horizontal striations.
    val sheenColor = NyraHorizonPalette.IvoryHighlight.toColor(alphaMultiplier = 0.04f)
    val striationCount = 8
    repeat(striationCount) { i ->
        val fraction = (i + 1).toFloat() / (striationCount + 1)
        val y = waterTop + (h - waterTop) * fraction
        drawLine(
            color = sheenColor,
            start = Offset(parallaxX + w * 0.05f, y),
            end = Offset(parallaxX + w * 0.95f, y),
            strokeWidth = 0.8f
        )
    }
}

// ─── Layer 6 (V1.0) — Reflection: bloom + streak + shimmer ───────────────────

private fun DrawScope.drawReflectionHorizonBloom(w: Float, h: Float) {
    // Wide warm bloom centred on the horizon line — feels like light catching
    // the surface of the water, not a beam.
    val center = Offset(w * 0.5f, h * 0.72f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                NyraHorizonPalette.ReflectionBloom.toColor(),
                NyraHorizonPalette.ReflectionBloom.toColor(alphaMultiplier = 0.45f),
                Color.Transparent
            ),
            center = center,
            radius = w * 0.55f
        ),
        radius = w * 0.55f,
        center = center
    )
}

private fun DrawScope.drawReflectionStreak(w: Float, h: Float) {
    // Narrower, softer vertical streak — supporting layer behind the bloom,
    // not the primary reflection any more.
    val waterTop = h * 0.71f
    val centerX = w * 0.5f
    val streakWidth = w * 0.04f
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                NyraHorizonPalette.ReflectionStreak.toColor(),
                NyraHorizonPalette.ReflectionStreak.toColor(alphaMultiplier = 0.5f),
                Color.Transparent
            ),
            startY = waterTop,
            endY = h * 0.93f
        ),
        topLeft = Offset(centerX - streakWidth / 2f, waterTop),
        size = Size(streakWidth, h * 0.22f)
    )
}

private fun DrawScope.drawShimmerHighlights(w: Float, h: Float, phase: Float) {
    // 12 small shimmer dots breaking up the streak irregularly. The deterministic
    // positions look organic; alpha pulses on a long phase so it never feels
    // mechanical.
    val baseColor = NyraHorizonPalette.ShimmerHighlight.toColor()
    val waterTop = h * 0.71f
    repeat(12) { index ->
        val nx = ((index * 211) % 1000) / 1000f       // horizontal position
        val ny = ((index * 137) % 1000) / 1000f       // vertical fraction inside water
        // Concentrate horizontally around the centre but allow some breakup.
        val x = w * (0.30f + nx * 0.40f)
        val y = waterTop + (h - waterTop) * (0.05f + ny * 0.55f)
        // Shimmer alpha breathes with phase; offset so dots don't all flash together.
        val pulse = 0.5f + 0.5f * sin((phase + index * 0.18f) * PI.toFloat() * 2f)
        val radius = 0.9f + (index % 4) * 0.3f
        // Fade toward the bottom — natural dissolution into darkness.
        val depthFade = 1f - ny * 0.7f
        drawCircle(
            color = baseColor.copy(alpha = (baseColor.alpha * pulse * depthFade).coerceIn(0f, 1f)),
            radius = radius,
            center = Offset(x, y)
        )
    }
}

// ─── Layer 7 (V1.0) — Eclipse ring ───────────────────────────────────────────

private fun DrawScope.drawEclipseRing(w: Float, h: Float, pulse: Float) {
    val center = Offset(w * 0.5f, h * 0.42f)
    val baseRadius = min(w, h) * 0.32f * pulse

    // 5-tier construction (back-to-front):

    // 1. Very wide low-opacity bloom — separates ring from sky atmosphere.
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                NyraHorizonPalette.EclipseBloom.toColor(),
                Color.Transparent
            ),
            center = center,
            radius = baseRadius * 2.20f
        ),
        radius = baseRadius * 2.20f,
        center = center
    )

    // 2. Dusty violet diffusion — soft cool ring of haze around the focal.
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                NyraHorizonPalette.EclipseDiffusion.toColor(),
                Color.Transparent
            ),
            center = center,
            radius = baseRadius * 1.70f
        ),
        radius = baseRadius * 1.70f,
        center = center
    )

    // 3. Outer warm halo (existing).
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.Transparent,
                NyraHorizonPalette.EclipseHalo.toColor(),
                Color.Transparent
            ),
            center = center,
            radius = baseRadius * 1.50f
        ),
        radius = baseRadius * 1.50f,
        center = center
    )

    // 4. Primary ring stroke.
    drawCircle(
        color = NyraHorizonPalette.EclipseRingStroke.toColor(alphaMultiplier = 0.78f),
        radius = baseRadius,
        center = center,
        style = Stroke(width = 1.4f)
    )

    // 5. **V1.0 brightest inner edge** — the highest luminance point of the
    // entire scene. Sits just inside the primary stroke, slightly brighter, so
    // the ring reads as a luminous atmospheric edge rather than a flat circle.
    drawCircle(
        color = NyraHorizonPalette.EclipseInnerEdge.toColor(),
        radius = baseRadius * 0.985f,
        center = center,
        style = Stroke(width = 1.0f)
    )

    // 6. Outer thin peach echo.
    drawCircle(
        color = NyraHorizonPalette.PeachAtmospheric.toColor(alphaMultiplier = 0.22f),
        radius = baseRadius * 1.11f,
        center = center,
        style = Stroke(width = 0.8f)
    )
}

// ─── Layer 8 — Atmospheric fog ───────────────────────────────────────────────

private fun DrawScope.drawAtmosphericFog(w: Float, h: Float, parallaxX: Float) {
    val center = Offset(w * 0.5f + parallaxX, h * 0.58f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                NyraHorizonPalette.FogHaze.toColor(),
                Color.Transparent
            ),
            center = center,
            radius = w * 0.85f
        ),
        radius = w * 0.85f,
        center = center
    )
}

// ─── Layer 9 (V1.0) — Character silhouette with rim light ───────────────────

private fun DrawScope.drawCharacterSilhouette(w: Float, h: Float, parallaxX: Float) {
    val baseY = h * 0.74f
    val figureHeight = h * 0.032f
    val figureWidth = figureHeight * 0.42f
    val centerX = w * 0.46f + parallaxX
    val bodyColor = NyraHorizonPalette.FigureShadow.toColor()
    val rimColor = NyraHorizonPalette.FigureRimLight.toColor()

    // Head — filled circle.
    val headRadius = figureWidth * 0.45f
    val headCenter = Offset(centerX, baseY - figureHeight + headRadius)
    drawCircle(color = bodyColor, radius = headRadius, center = headCenter)

    // Rim light on the head — thin arc on the right edge catching distant glow.
    drawArc(
        color = rimColor,
        startAngle = -65f,
        sweepAngle = 80f,
        useCenter = false,
        topLeft = Offset(
            headCenter.x - headRadius + 0.4f,
            headCenter.y - headRadius + 0.4f
        ),
        size = Size(headRadius * 2f - 0.8f, headRadius * 2f - 0.8f),
        style = Stroke(width = 0.6f)
    )

    // Body — slightly tapered trapezoid as a filled path.
    val bodyTopY = headCenter.y + headRadius * 0.6f
    val bodyPath = Path().apply {
        moveTo(centerX - figureWidth * 0.42f, bodyTopY)
        lineTo(centerX + figureWidth * 0.42f, bodyTopY)
        lineTo(centerX + figureWidth * 0.55f, baseY)
        lineTo(centerX - figureWidth * 0.55f, baseY)
        close()
    }
    drawPath(path = bodyPath, color = bodyColor)

    // Right-edge rim light along the body.
    drawLine(
        color = rimColor,
        start = Offset(centerX + figureWidth * 0.42f + 0.3f, bodyTopY + 0.6f),
        end = Offset(centerX + figureWidth * 0.55f + 0.3f, baseY - 0.6f),
        strokeWidth = 0.6f
    )

    // Feet fade — overlay a vertical gradient that dissolves the bottom of the
    // figure into the foreground haze, so it doesn't read as a cut-out.
    val fadeTopY = baseY - figureHeight * 0.30f
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                NyraHorizonPalette.MountainsForeground.toColor(alphaMultiplier = 0.30f),
                NyraHorizonPalette.MountainsForeground.toColor(alphaMultiplier = 0.85f)
            ),
            startY = fadeTopY,
            endY = baseY + 1f
        ),
        topLeft = Offset(centerX - figureWidth * 0.7f, fadeTopY),
        size = Size(figureWidth * 1.4f, baseY - fadeTopY + 1f)
    )
}

// ─── Layer 10 — Flow particles ───────────────────────────────────────────────

private fun DrawScope.drawFlowParticles(w: Float, h: Float, phase: Float) {
    val color = NyraHorizonPalette.DriftParticle.toColor()
    val count = 22
    repeat(count) { index ->
        val nx = ((index * 37) % 1000) / 1000f
        val ny = ((index * 53) % 1000) / 1000f
        val drift = 6f * sin((phase + index * 0.07f) * PI.toFloat() * 2f)
        val x = (w * nx + drift).coerceIn(0f, w)
        val y = (h * 0.35f + h * 0.45f * ny)
        drawCircle(color = color, radius = 0.9f + (index % 4) * 0.4f, center = Offset(x, y))
    }
}

// ─── Layer 11 — UI overlay (V1.0 lowercase elegance) ─────────────────────────

@Composable
private fun SplashUiOverlay() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 96.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "nyra",
                color = NyraHorizonPalette.OverlayPrimaryText.toColor(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 14.sp
            )
            Text(
                text = "an emotional atmosphere",
                color = NyraHorizonPalette.OverlaySecondaryText.toColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 4.sp
            )
        }
    }
}

// ─── Helpers ────────────────────────────────────────────────────────────────

private fun Long.toColor(alphaMultiplier: Float = 1f): Color {
    val alpha = (((this ushr 24) and 0xFF).toFloat() / 255f * alphaMultiplier).coerceIn(0f, 1f)
    val red = ((this ushr 16) and 0xFF).toFloat() / 255f
    val green = ((this ushr 8) and 0xFF).toFloat() / 255f
    val blue = (this and 0xFF).toFloat() / 255f
    return Color(red = red, green = green, blue = blue, alpha = alpha)
}
