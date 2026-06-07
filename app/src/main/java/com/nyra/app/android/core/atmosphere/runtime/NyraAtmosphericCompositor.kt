package com.nyra.app.android.core.atmosphere.runtime

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nyra.app.android.core.atmosphere.layers.AtmosphericInteriorOverlay
import com.nyra.app.android.core.atmosphere.layers.AtmosphericRingsLayer
import com.nyra.app.android.core.atmosphere.layers.BackboneGradientLayer
import com.nyra.app.android.core.atmosphere.layers.FlowRibbonsLayer
import com.nyra.app.android.core.atmosphere.layers.FogDiffusionLayer
import com.nyra.app.android.core.atmosphere.layers.HorizonGlowLayer
import com.nyra.app.android.core.atmosphere.layers.ParticlesLayer
import com.nyra.app.android.core.atmosphere.layers.ReflectiveDepthSurfaceLayer
import com.nyra.app.android.core.atmosphere.layers.SymbolicTopologyLayer
import com.nyra.app.android.core.atmosphere.model.NyraEmotionalLayerState
import com.nyra.app.android.core.atmosphere.palette.AnimatedAtmospherePalette
import com.nyra.app.android.core.atmosphere.palette.NyraTimeOfDayPaletteFactory
import com.nyra.app.android.core.onboarding.model.AtmosphereVariant
import kotlin.math.PI
import kotlin.math.sin

/**
 * Central runtime compositor for Nyra's atmospheric stack.
 *
 * Stacks the spec's nine layers in canonical order:
 *
 *  1. Procedural atmospheric gradient — [BackboneGradientLayer]
 *  2. Horizon glow                    — [HorizonGlowLayer]
 *  3. Fog diffusion                   — [FogDiffusionLayer]
 *  4. Atmospheric rings               — [AtmosphericRingsLayer]
 *  5. Flow ribbons                    — [FlowRibbonsLayer] (gated)
 *  6. Symbolic topology               — [SymbolicTopologyLayer] (gated by intensity)
 *  7. Emotional overlay asset         — [AtmosphericInteriorOverlay] (optional)
 *  8. Particles                       — [ParticlesLayer]
 *  9. UI content                      — your [content] lambda
 *
 * Everything reads from the single [NyraEmotionalLayerState] passed in. Two
 * shared infinite transitions drive the ring drift / pulse and the ribbon
 * phase — using a small set keeps the recomposition surface tight.
 *
 * @param interiorOverlayRes optional drawable resource for the silhouette /
 *   reflective interior. Tinted at runtime via [AtmosphericInteriorOverlay].
 *   Pass `null` and `state.interiorOverlayAlpha = 0` to hide it.
 */
@Composable
fun NyraAtmosphericCompositor(
    state: NyraEmotionalLayerState,
    modifier: Modifier = Modifier,
    @DrawableRes interiorOverlayRes: Int? = null,
    content: @Composable BoxScope.() -> Unit
) {
    // Use the animated palette so cross-state shifts drift cinematically rather
    // than popping. Drift duration matches the "extremely slow, meditative" motion
    // spec — 1.8s by default; long enough to read as breathing.
    val palette = AnimatedAtmospherePalette.rememberAnimated(
        target = NyraTimeOfDayPaletteFactory.forTimeOfDay(state.timeOfDay),
        warmthBias = state.warmthBias
    )
    val variantFactors = state.variant.factors()

    val transition = rememberInfiniteTransition(label = "nyra_atmosphere")

    // Ring drift + pulse phase. Ribbon phase is a separate, slower oscillator
    // so the two layers don't lock into a single rhythm.
    val ringPhase = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ringPeriodMs(variantFactors.driftSpeed)),
            repeatMode = RepeatMode.Restart
        ),
        label = "nyra_ring_phase"
    )
    val ringPulse = transition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5_200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nyra_ring_pulse"
    )
    val ribbonPhase = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ribbonPeriodMs(variantFactors.driftSpeed)),
            repeatMode = RepeatMode.Restart
        ),
        label = "nyra_ribbon_phase"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // 01. Gradient backbone
        BackboneGradientLayer(palette = palette)

        // 02. Reflective depth surface — only when explicitly enabled.
        if (state.reflectiveDepthVisible) {
            ReflectiveDepthSurfaceLayer(palette = palette)
        }

        // 03. Horizon glow
        HorizonGlowLayer(
            palette = palette,
            centerYFraction = variantFactors.horizonYFraction
        )

        // 3. Fog diffusion — variant determines intensity.
        FogDiffusionLayer(palette = palette, intensity = variantFactors.fogIntensity)

        // 4. Atmospheric rings — emotional state drives geometry; variant nudges drift.
        AtmosphericRingsLayer(
            palette = palette,
            state = state.ringState,
            driftPhase = ringPhase.value + ringDriftJitter(),
            pulse = ringPulse.value
        )

        // 5. Flow ribbons — only when explicitly enabled by the consumer.
        if (state.ribbonsVisible) {
            FlowRibbonsLayer(
                palette = palette,
                state = state.ribbonState,
                driftPhase = ribbonPhase.value
            )
        }

        // 6. Symbolic topology — gated by intensity > 0.
        SymbolicTopologyLayer(palette = palette, intensity = state.topologyIntensity)

        // 7. Optional interior overlay asset (silhouette / reflective profile).
        AtmosphericInteriorOverlay(
            palette = palette,
            drawableRes = interiorOverlayRes,
            alpha = state.interiorOverlayAlpha
        )

        // 8. Particles
        ParticlesLayer(palette = palette, density = state.particleDensity)

        // 9. UI content
        content()
    }
}

// ─── Variant tuning ──────────────────────────────────────────────────────────

private data class VariantFactors(
    val driftSpeed: Float,
    val horizonYFraction: Float,
    val fogIntensity: Float
)

private fun AtmosphereVariant.factors(): VariantFactors = when (this) {
    AtmosphereVariant.HorizonDawn -> VariantFactors(
        driftSpeed = 1.0f,
        horizonYFraction = 0.72f,
        fogIntensity = 1.0f
    )
    AtmosphereVariant.WarmInterior -> VariantFactors(
        driftSpeed = 0.7f,
        horizonYFraction = 0.55f,   // warm window light feels closer
        fogIntensity = 1.2f
    )
    AtmosphereVariant.StillnightMinimal -> VariantFactors(
        driftSpeed = 0.45f,
        horizonYFraction = 0.78f,
        fogIntensity = 0.5f
    )
}

private fun ringPeriodMs(driftSpeed: Float): Int =
    (28_000 / driftSpeed.coerceIn(0.3f, 2.0f)).toInt().coerceIn(12_000, 60_000)

private fun ribbonPeriodMs(driftSpeed: Float): Int =
    (44_000 / driftSpeed.coerceIn(0.3f, 2.0f)).toInt().coerceIn(18_000, 90_000)

// Tiny horizontal jitter on the ring drift so it doesn't feel mechanically circular.
@Composable
private fun ringDriftJitter(): Float {
    // Driven by the same overall infinite transition but at a different phase.
    val transition = rememberInfiniteTransition(label = "ring_jitter")
    val v = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11_000),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring_jitter_phase"
    )
    return 0.04f * sin(v.value * PI.toFloat() * 2f)
}
