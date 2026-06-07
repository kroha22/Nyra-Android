package com.nyra.app.android.core.ui_state.runtime.modules

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.nyra.app.android.core.profile.model.NyraUserProfile
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette

/**
 * All Home Screen module Composables.
 *
 * Each module is small, self-contained, and stateless beyond the data it accepts.
 * Composition / selection lives in `AdaptiveHomeScreen.kt` via the
 * `NyraHomeStateLevel` ladder.
 *
 * Visual rules (per spec):
 *  - 4–8% opacity surfaces ("like light in atmosphere, not glass rectangles")
 *  - Soft glow edges via 1px border at low alpha
 *  - Ultra-light typography — `FontWeight.Light` everywhere; never bold
 *  - Generous vertical breathing room
 *  - One major glow focal per screen — handled by the Hero card; other cards
 *    stay quiet
 */

// ─────────────────────────────────────────────────────────────────────────────
//  Shared atomic — translucent atmospheric card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
internal fun AtmosphericCard(
    modifier: Modifier = Modifier,
    isHero: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(if (isHero) 28.dp else 22.dp)
    val surface = if (isHero) NyraNocturnePalette.WarmSurface else NyraNocturnePalette.RestingSurface
    val borderColor = if (isHero) NyraNocturnePalette.RingPrimary else NyraNocturnePalette.RingSecondary

    val base = modifier
        .fillMaxWidth()
        .background(color = surface.argbToColor(), shape = shape)
        .border(width = 0.6.dp, color = borderColor.argbToColor(), shape = shape)
        .padding(horizontal = 22.dp, vertical = 20.dp)
    val final = if (onClick != null) base.clickable(onClick = onClick) else base

    Box(modifier = final) { content() }
}

// ─────────────────────────────────────────────────────────────────────────────
//  STATE 0 — Empty Home
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HeroPersonalizationCard(
    onBegin: () -> Unit,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier, isHero = true, onClick = onBegin) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text(
                text = "Nyra becomes more personal as you add emotional signals.",
                color = NyraNocturnePalette.TextPrimary.argbToColor(),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 26.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = NyraNocturnePalette.AuroraRose.argbToColor(),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Begin personalization",
                    color = NyraNocturnePalette.HorizonLavender.argbToColor(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.4.sp
                )
            }
        }
    }
}

@Composable
fun EmotionalCheckInOrbs(
    onSelect: (EmotionalCheckIn) -> Unit,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "How does it feel right now",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 0.5.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EmotionalCheckIn.values().forEach { mood ->
                    CheckInOrb(mood = mood, onClick = { onSelect(mood) })
                }
            }
        }
    }
}

enum class EmotionalCheckIn(val label: String, val tintLong: Long) {
    Calm("calm", NyraNocturnePalette.MistBlue),
    Reflective("reflective", NyraNocturnePalette.HorizonLavender),
    Overwhelmed("overwhelmed", NyraNocturnePalette.AuroraRose),
    Disconnected("disconnected", NyraNocturnePalette.CosmicDust),
    Hopeful("hopeful", NyraNocturnePalette.PeachDawn)
}

@Composable
private fun CheckInOrb(mood: EmotionalCheckIn, onClick: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "orb_${mood.name}")
    val pulse = transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_pulse_${mood.name}"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size((36 * pulse.value).dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                mood.tintLong.argbToColor(alphaMultiplier = 0.7f),
                                mood.tintLong.argbToColor(alphaMultiplier = 0.15f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = mood.label,
            color = NyraNocturnePalette.TextSecondary.argbToColor(),
            fontSize = 11.sp,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun ReflectionPromptCard(
    prompt: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier, onClick = onTap) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Reflection",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            Text(
                text = prompt,
                color = NyraNocturnePalette.TextPrimary.argbToColor(),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 26.sp
            )
        }
    }
}

@Composable
fun MoodTimelinePlaceholder(modifier: Modifier = Modifier) {
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = "Your emotional rhythm will appear here over time.",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 22.sp
            )
            // Thin minimal horizon line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                NyraNocturnePalette.RingPrimary.argbToColor(),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  STATE 1 — Trackers enabled
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun EmotionalRhythmCard(
    headline: String,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Rhythm",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            Text(
                text = headline,
                color = NyraNocturnePalette.TextPrimary.argbToColor(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun PatternHintCard(
    hint: String,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier) {
        Text(
            text = hint,
            color = NyraNocturnePalette.TextPrimary.argbToColor(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun TrackerQuickAdd(
    trackers: List<String>,
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(
                text = "Add a signal",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                trackers.forEach { tracker ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAdd(tracker) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = NyraNocturnePalette.MistBlue.argbToColor(),
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = tracker,
                            color = NyraNocturnePalette.TextPrimary.argbToColor(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  STATE 2 — Natal profile enabled
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DailyEmotionalAtmosphereHero(
    headline: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier, isHero = true) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Today",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            Text(
                text = headline,
                color = NyraNocturnePalette.TextPrimary.argbToColor(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 30.sp
            )
            Text(
                text = subtitle,
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun IdentityPreviewCard(
    profile: NyraUserProfile,
    modifier: Modifier = Modifier
) {
    val previewTraits = remember(profile) { profile.topTraits.take(3).map { it.traitId } }
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Identity",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            previewTraits.forEach { traitId ->
                Text(
                    text = traitId.humanise(),
                    color = NyraNocturnePalette.TextPrimary.argbToColor(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Light
                )
            }
            if (previewTraits.isEmpty()) {
                Text(
                    text = "Patterns are forming.",
                    color = NyraNocturnePalette.TextTertiary.argbToColor(),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun ArchetypePreviewCard(
    profile: NyraUserProfile,
    modifier: Modifier = Modifier
) {
    val title = remember(profile) {
        profile.activeArchetypes.firstOrNull()?.archetypeId?.humanise() ?: "Quiet Orbit"
    }
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Archetype",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            Text(
                text = title,
                color = NyraNocturnePalette.TextPrimary.argbToColor(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 28.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  STATE 3+ — placeholders (data plumbing pending)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun EmotionalClimateCard(
    headline: String,
    secondary: String,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Climate",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            Text(
                text = headline,
                color = NyraNocturnePalette.TextPrimary.argbToColor(),
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 25.sp
            )
            Text(
                text = secondary,
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 13.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun MemoryEchoCard(
    body: String,
    modifier: Modifier = Modifier
) {
    AtmosphericCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Echo",
                color = NyraNocturnePalette.TextSecondary.argbToColor(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 1.5.sp
            )
            Text(
                text = body,
                color = NyraNocturnePalette.TextPrimary.argbToColor(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 24.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Helpers
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Convert snake_case trait/archetype ids into Title Case display strings.
 * Stays as a UI-side concern until the localisation layer ships.
 */
internal fun String.humanise(): String =
    split("_").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }

internal fun Long.argbToColor(alphaMultiplier: Float = 1f): Color {
    val alpha = (((this ushr 24) and 0xFF).toFloat() / 255f * alphaMultiplier).coerceIn(0f, 1f)
    val red = ((this ushr 16) and 0xFF).toFloat() / 255f
    val green = ((this ushr 8) and 0xFF).toFloat() / 255f
    val blue = (this and 0xFF).toFloat() / 255f
    return Color(red = red, green = green, blue = blue, alpha = alpha)
}

@Composable
private fun <T> remember(key: Any?, calculation: () -> T): T =
    androidx.compose.runtime.remember(key) { calculation() }
