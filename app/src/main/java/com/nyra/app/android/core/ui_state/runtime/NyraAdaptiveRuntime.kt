package com.nyra.app.android.core.ui_state.runtime

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.model.NyraUiStateConfig
import com.nyra.app.android.core.ui_state.resolver.NyraUiStateConfigResolver
import kotlin.math.max

@Composable
fun rememberNyraUiState(
    profile: NyraUserProfile,
    resolver: NyraUiStateConfigResolver
): NyraUiStateConfig =
    remember(profile) { resolver.resolve(profile) }

@Composable
fun NyraBackground(
    uiState: NyraUiStateConfig,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        uiState.palette.backgroundPrimary.toComposeColor(),
                        uiState.palette.backgroundSecondary.toComposeColor()
                    )
                )
            )
    ) {
        NyraParticleLayer(uiState = uiState)
        content()
    }
}

@Composable
fun NyraOrb(
    uiState: NyraUiStateConfig,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "nyra_orb")
    val pulse = transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = max(1200, (4200 / uiState.orb.orbPulseSpeed.coerceAtLeast(0.1f)).toInt())
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "nyra_orb_pulse"
    )
    val size = 156.dp * uiState.orb.orbSizeMultiplier * pulse.value
    Box(
        modifier = modifier
            .size(size)
            .blur((uiState.background.blurStrength * 0.35f).dp)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        uiState.orb.orbGlowColor.toComposeColor(alphaMultiplier = 0.78f),
                        uiState.palette.secondaryGlow.toComposeColor(alphaMultiplier = 0.22f),
                        Color.Transparent
                    )
                ),
                CircleShape
            )
    )
}

@Composable
fun NyraGlassCard(
    uiState: NyraUiStateConfig,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = uiState.palette.surface.toComposeColor(),
                shape = RoundedCornerShape(uiState.cardStyle.cornerRadius.dp)
            )
            .border(
                width = 1.dp,
                color = uiState.palette.textPrimary.toComposeColor(
                    alphaMultiplier = uiState.cardStyle.borderOpacity
                ),
                shape = RoundedCornerShape(uiState.cardStyle.cornerRadius.dp)
            )
    ) {
        content()
    }
}

@Composable
fun NyraParticleLayer(
    uiState: NyraUiStateConfig,
    modifier: Modifier = Modifier
) {
    val density = uiState.motion.particleDensity.coerceIn(0f, 1f)
    Canvas(modifier = modifier.fillMaxSize()) {
        drawParticles(uiState = uiState, density = density)
    }
}

private fun DrawScope.drawParticles(
    uiState: NyraUiStateConfig,
    density: Float
) {
    val count = (10 + density * 28).toInt()
    val color = uiState.palette.secondaryGlow.toComposeColor(alphaMultiplier = 0.12f + density * 0.10f)
    repeat(count) { index ->
        val x = size.width * ((index * 37 % 100) / 100f)
        val y = size.height * ((index * 53 % 100) / 100f)
        val radius = 1.2f + (index % 4) * 0.55f
        drawCircle(color = color, radius = radius, center = Offset(x, y))
    }
}

@Composable
fun NyraAdaptiveLayer(
    uiState: NyraUiStateConfig,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    NyraBackground(uiState = uiState, modifier = modifier) {
        NyraOrb(
            uiState = uiState,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        content()
    }
}

fun Long.toComposeColor(alphaMultiplier: Float = 1f): Color {
    val alpha = (((this ushr 24) and 0xFF).toFloat() / 255f * alphaMultiplier).coerceIn(0f, 1f)
    val red = ((this ushr 16) and 0xFF).toFloat() / 255f
    val green = ((this ushr 8) and 0xFF).toFloat() / 255f
    val blue = (this and 0xFF).toFloat() / 255f
    return Color(red = red, green = green, blue = blue, alpha = alpha)
}
