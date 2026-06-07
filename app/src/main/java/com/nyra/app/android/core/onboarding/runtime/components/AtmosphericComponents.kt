package com.nyra.app.android.core.onboarding.runtime.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette

/**
 * Reusable atmospheric UI primitives for onboarding screens.
 *
 * All components share the Warm Nocturne palette + ultra-light typography +
 * 4–8% surface translucency rules from the design spec.
 */

// ─── Title / Subtitle blocks ─────────────────────────────────────────────────

@Composable
fun OnboardingTitle(text: String) {
    Text(
        text = text,
        color = NyraNocturnePalette.TextPrimary.toColor(),
        fontSize = 28.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 38.sp,
        letterSpacing = 0.2.sp
    )
}

@Composable
fun OnboardingSubtitle(text: String) {
    Text(
        text = text,
        color = NyraNocturnePalette.TextSecondary.toColor(),
        fontSize = 15.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 22.sp
    )
}

@Composable
fun OnboardingCaption(text: String) {
    Text(
        text = text,
        color = NyraNocturnePalette.TextTertiary.toColor(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 18.sp
    )
}

@Composable
fun OnboardingEyebrow(text: String) {
    Text(
        text = text.uppercase(),
        color = NyraNocturnePalette.TextSecondary.toColor(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 3.sp
    )
}

// ─── Primary CTA ─────────────────────────────────────────────────────────────

@Composable
fun AtmosphericPrimaryButton(
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = NyraNocturnePalette.AuroraRose
                    .toColor(alphaMultiplier = if (enabled) 0.20f else 0.08f),
                shape = RoundedCornerShape(28.dp)
            )
            .border(
                width = 0.6.dp,
                color = NyraNocturnePalette.HorizonLavender
                    .toColor(alphaMultiplier = if (enabled) 0.45f else 0.15f),
                shape = RoundedCornerShape(28.dp)
            )
            .alpha(if (enabled) 1f else 0.55f)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = NyraNocturnePalette.TextPrimary.toColor(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.2.sp
        )
    }
}

@Composable
fun AtmosphericGhostButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = NyraNocturnePalette.TextSecondary.toColor(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Light,
            letterSpacing = 1.sp
        )
    }
}

// ─── Glassmorphism text field ────────────────────────────────────────────────

@Composable
fun AtmosphericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = NyraNocturnePalette.RestingSurface.toColor(),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 0.6.dp,
                color = NyraNocturnePalette.RingPrimary.toColor(),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 22.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(NyraNocturnePalette.HorizonLavender.toColor()),
            textStyle = TextStyle(
                color = NyraNocturnePalette.TextPrimary.toColor(),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light
            ),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { inner ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = NyraNocturnePalette.TextTertiary.toColor(),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                    inner()
                }
            }
        )
    }
}

// ─── Translucent atmospheric card ────────────────────────────────────────────

@Composable
fun AtmosphericSurface(
    modifier: Modifier = Modifier,
    isStrong: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(24.dp)
    val base = modifier
        .background(
            color = if (isStrong) NyraNocturnePalette.WarmSurface.toColor() else NyraNocturnePalette.RestingSurface.toColor(),
            shape = shape
        )
        .border(
            BorderStroke(
                width = 0.6.dp,
                color = if (isStrong) NyraNocturnePalette.RingPrimary.toColor() else NyraNocturnePalette.RingSecondary.toColor()
            ),
            shape = shape
        )
        .padding(horizontal = 22.dp, vertical = 20.dp)
    val final = if (onClick != null) base.clickable(onClick = onClick) else base

    Box(modifier = final) { content() }
}

// ─── Toggle + choice chip ────────────────────────────────────────────────────

@Composable
fun AtmosphericToggle(
    label: String,
    selected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(
                    color = if (selected) NyraNocturnePalette.AuroraRose.toColor(0.6f)
                    else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    0.8.dp,
                    NyraNocturnePalette.HorizonLavender.toColor(0.6f),
                    CircleShape
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = NyraNocturnePalette.TextPrimary.toColor(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun AtmosphericChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = if (selected) NyraNocturnePalette.WarmSurface.toColor()
                else NyraNocturnePalette.RestingSurface.toColor(),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 0.6.dp,
                color = if (selected) NyraNocturnePalette.HorizonLavender.toColor(0.5f)
                else NyraNocturnePalette.RingSecondary.toColor(),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = NyraNocturnePalette.TextPrimary.toColor(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Light
        )
    }
}

// ─── Step progress dots ──────────────────────────────────────────────────────

@Composable
fun StepProgressDots(currentIndex: Int, total: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { idx ->
            val active = idx == currentIndex
            Box(
                modifier = Modifier
                    .size(if (active) 6.dp else 4.dp)
                    .background(
                        color = NyraNocturnePalette.HorizonLavender
                            .toColor(if (active) 0.85f else 0.25f),
                        shape = CircleShape
                    )
            )
        }
    }
}

// ─── Scaffold shared across screens ──────────────────────────────────────────

@Composable
fun OnboardingScaffold(
    onBack: (() -> Unit)? = null,
    progress: Pair<Int, Int>? = null,
    bottom: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp, vertical = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                Text(
                    text = "Back",
                    color = NyraNocturnePalette.TextSecondary.toColor(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.clickable(onClick = onBack)
                )
            } else {
                Text(
                    text = "Nyra",
                    color = NyraNocturnePalette.TextSecondary.toColor(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 4.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            progress?.let { (current, total) ->
                StepProgressDots(currentIndex = current, total = total)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Box(modifier = Modifier.weight(1f)) {
            content()
        }

        bottom()
    }
}

// ─── Helpers ────────────────────────────────────────────────────────────────

internal fun Long.toColor(alphaMultiplier: Float = 1f): Color {
    val alpha = (((this ushr 24) and 0xFF).toFloat() / 255f * alphaMultiplier).coerceIn(0f, 1f)
    val red = ((this ushr 16) and 0xFF).toFloat() / 255f
    val green = ((this ushr 8) and 0xFF).toFloat() / 255f
    val blue = (this and 0xFF).toFloat() / 255f
    return Color(red = red, green = green, blue = blue, alpha = alpha)
}
