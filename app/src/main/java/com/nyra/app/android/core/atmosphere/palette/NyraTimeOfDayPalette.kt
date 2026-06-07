package com.nyra.app.android.core.atmosphere.palette

import androidx.compose.ui.graphics.Color
import com.nyra.app.android.core.model.TimeOfDay
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette

/**
 * Per-time-of-day color derivations.
 *
 * The base palette lives in [NyraNocturnePalette]. This file shifts it slightly
 * per circadian phase — Morning leans Mist Blue + pale lavender + dawn peach;
 * Day balances indigo + violet + cleaner gradients; Evening warms toward mauve
 * + dusk rose; Night sinks into deep navy + faint rose highlights.
 *
 * Returned [TimeOfDayPalette] is consumed by the atmospheric compositor; layers
 * read individual channels (backboneTop / backboneBottom / focal / ring / fog).
 */
data class TimeOfDayPalette(
    /** Top of the vertical backbone gradient. */
    val backboneTop: Color,
    /** Middle stop of the backbone gradient (around 55–65% screen height). */
    val backboneMid: Color,
    /** Bottom of the backbone gradient. */
    val backboneBottom: Color,
    /** Inner focal halo tint (the warmest core). */
    val focalInner: Color,
    /** Outer focal halo tint (cools into atmosphere). */
    val focalOuter: Color,
    /** Primary ring stroke colour. */
    val ringPrimary: Color,
    /** Secondary outer-halo ring colour. */
    val ringSecondary: Color,
    /** Fog diffusion tint. */
    val fog: Color,
    /** Ribbon stroke colour. */
    val ribbon: Color,
    /** Topology line / dot tint. */
    val topology: Color
)

object NyraTimeOfDayPaletteFactory {

    fun forTimeOfDay(time: TimeOfDay): TimeOfDayPalette = when (time) {
        TimeOfDay.Morning -> Morning
        TimeOfDay.Afternoon -> Day
        TimeOfDay.Evening -> Evening
        TimeOfDay.Night -> Night
    }

    /** Quiet Awakening — soft clarity, openness, pale dawn warmth. */
    val Morning: TimeOfDayPalette = TimeOfDayPalette(
        backboneTop = NyraNocturnePalette.DeepSpaceIndigo.toComposeColor(alphaMultiplier = 0.85f),
        backboneMid = NyraNocturnePalette.AtmosphericViolet.toComposeColor(alphaMultiplier = 0.6f),
        backboneBottom = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = 0.20f),
        focalInner = NyraNocturnePalette.PeachDawn.toComposeColor(alphaMultiplier = 0.22f),
        focalOuter = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = 0.12f),
        ringPrimary = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = 0.45f),
        ringSecondary = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.30f),
        fog = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.09f),
        ribbon = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = 0.12f),
        topology = NyraNocturnePalette.TopologyLine.toComposeColor()
    )

    /** Present Awareness — grounded calm, cleaner gradients. */
    val Day: TimeOfDayPalette = TimeOfDayPalette(
        backboneTop = NyraNocturnePalette.VoidNavy.toComposeColor(alphaMultiplier = 0.85f),
        backboneMid = NyraNocturnePalette.DeepSpaceIndigo.toComposeColor(),
        backboneBottom = NyraNocturnePalette.AtmosphericViolet.toComposeColor(alphaMultiplier = 0.7f),
        focalInner = NyraNocturnePalette.PeachDawn.toComposeColor(alphaMultiplier = 0.14f),
        focalOuter = NyraNocturnePalette.AuroraRose.toComposeColor(alphaMultiplier = 0.07f),
        ringPrimary = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.40f),
        ringSecondary = NyraNocturnePalette.AuroraRose.toComposeColor(alphaMultiplier = 0.20f),
        fog = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.05f),
        ribbon = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.09f),
        topology = NyraNocturnePalette.TopologyLine.toComposeColor()
    )

    /** Emotional Reflection — canonical Nyra evening, warm mauve, cinematic silence. */
    val Evening: TimeOfDayPalette = TimeOfDayPalette(
        backboneTop = NyraNocturnePalette.VoidNavy.toComposeColor(),
        backboneMid = NyraNocturnePalette.DeepSpaceIndigo.toComposeColor(),
        backboneBottom = NyraNocturnePalette.AtmosphericViolet.toComposeColor(alphaMultiplier = 0.85f),
        focalInner = NyraNocturnePalette.PeachDawn.toComposeColor(alphaMultiplier = 0.20f),
        focalOuter = NyraNocturnePalette.AuroraRose.toComposeColor(alphaMultiplier = 0.12f),
        ringPrimary = NyraNocturnePalette.RingPrimary.toComposeColor(),
        ringSecondary = NyraNocturnePalette.RingSecondary.toComposeColor(),
        fog = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.07f),
        ribbon = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = 0.10f),
        topology = NyraNocturnePalette.TopologyLine.toComposeColor()
    )

    /** Deep Inner Space — symbolic depth, distant isolated glow, large dark spaces. */
    val Night: TimeOfDayPalette = TimeOfDayPalette(
        backboneTop = NyraNocturnePalette.VoidNavy.toComposeColor(),
        backboneMid = NyraNocturnePalette.VoidNavy.toComposeColor(alphaMultiplier = 0.95f),
        backboneBottom = NyraNocturnePalette.AtmosphericViolet.toComposeColor(alphaMultiplier = 0.65f),
        focalInner = NyraNocturnePalette.AuroraRose.toComposeColor(alphaMultiplier = 0.10f),
        focalOuter = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.05f),
        ringPrimary = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.30f),
        ringSecondary = NyraNocturnePalette.AuroraRose.toComposeColor(alphaMultiplier = 0.12f),
        fog = NyraNocturnePalette.HorizonLavender.toComposeColor(alphaMultiplier = 0.04f),
        ribbon = NyraNocturnePalette.MistBlue.toComposeColor(alphaMultiplier = 0.06f),
        topology = NyraNocturnePalette.TopologyLine.toComposeColor(alphaMultiplier = 0.7f)
    )
}

// Shared helper kept here to avoid pulling in the runtime helper from another package.
internal fun Long.toComposeColor(alphaMultiplier: Float = 1f): Color {
    val alpha = (((this ushr 24) and 0xFF).toFloat() / 255f * alphaMultiplier).coerceIn(0f, 1f)
    val red = ((this ushr 16) and 0xFF).toFloat() / 255f
    val green = ((this ushr 8) and 0xFF).toFloat() / 255f
    val blue = (this and 0xFF).toFloat() / 255f
    return Color(red = red, green = green, blue = blue, alpha = alpha)
}
