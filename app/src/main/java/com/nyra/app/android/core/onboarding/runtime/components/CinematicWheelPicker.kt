package com.nyra.app.android.core.onboarding.runtime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

/**
 * Cinematic date wheel — **NyraCalibrationPaletteV1.0** focal isolation.
 *
 * Changes vs the prior version:
 *  - A radial vignette sits *behind* the picker so the surrounding atmosphere
 *    visually recedes. The picker now reads as an isolated focal object.
 *  - The selected (centre) row is rendered with warm peach typography + a
 *    subtle warm-tinted background underlay, giving it the "emotionally
 *    illuminated" feeling the spec asks for.
 *  - Non-selected rows fade to cool moonstone, not just lowered alpha — the
 *    eye reads the centre row as the only warm element in the field.
 *  - Each column is wrapped in a slightly darker container so the rows have
 *    a clear plane to sit on.
 */
@Composable
fun CinematicDatePicker(
    day: Int,
    month: Int,
    year: Int,
    onDateChange: (day: Int, month: Int, year: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        // ─── Focal isolation vignette ────────────────────────────────────────
        // A radial darken centred on the picker pulls the eye in and pushes
        // atmospheric detail away. Drawn first so it sits behind everything.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NyraCalibrationPaletteV1.PickerVignette.toColor(alphaMultiplier = 0.85f),
                            NyraCalibrationPaletteV1.PickerVignette.toColor(alphaMultiplier = 0.55f),
                            Color.Transparent
                        ),
                        center = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                        radius = 600f
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            WheelColumn(
                value = day,
                range = 1..daysInMonth(month, year),
                onValueChange = { onDateChange(it, month, year) },
                modifier = Modifier.weight(0.9f),
                format = { it.toString().padStart(2, '0') }
            )
            WheelColumn(
                value = month,
                range = 1..12,
                onValueChange = { onDateChange(coerceDay(day, it, year), it, year) },
                modifier = Modifier.weight(1.2f),
                format = { monthShortName(it) }
            )
            WheelColumn(
                value = year,
                range = 1900..2025,
                onValueChange = { onDateChange(coerceDay(day, month, it), month, it) },
                modifier = Modifier.weight(1.1f),
                format = { it.toString() }
            )
        }
    }
}

@Composable
private fun WheelColumn(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    format: (Int) -> String,
    modifier: Modifier = Modifier
) {
    var accumulator by remember { mutableStateOf(0f) }
    val stepPx = 110f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    accumulator += delta
                    while (abs(accumulator) >= stepPx) {
                        if (accumulator > 0) {
                            val next = (value - 1).coerceAtLeast(range.first)
                            if (next != value) onValueChange(next)
                            accumulator -= stepPx
                        } else {
                            val next = (value + 1).coerceAtMost(range.last)
                            if (next != value) onValueChange(next)
                            accumulator += stepPx
                        }
                    }
                    delta
                }
            )
    ) {
        // Column inner panel — sits on a slightly cooler surface so the centre
        // selected row reads as the warmest, brightest element.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = NyraCalibrationPaletteV1.SurfaceWheel.toColor(),
                    shape = RoundedCornerShape(18.dp)
                )
                .border(
                    width = 0.5.dp,
                    color = NyraCalibrationPaletteV1.BorderCard.toColor(alphaMultiplier = 0.7f),
                    shape = RoundedCornerShape(18.dp)
                )
        )

        // Centre selected-row warm underlay — strict focal isolation.
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(46.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            NyraCalibrationPaletteV1.SurfaceWheelSelected.toColor(),
                            NyraCalibrationPaletteV1.SurfaceWheelSelected.toColor(alphaMultiplier = 0.85f),
                            NyraCalibrationPaletteV1.SurfaceWheelSelected.toColor(),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 0.5.dp,
                    color = NyraCalibrationPaletteV1.BorderWheelSelected.toColor(),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        // The visible items — selected row sits dead centre.
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (-2..2).forEach { offset ->
                val candidate = value + offset
                val visible = candidate in range
                val isSelected = offset == 0

                Box(
                    modifier = Modifier
                        .height(if (isSelected) 46.dp else 36.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (visible) format(candidate) else "",
                        color = if (isSelected) {
                            NyraCalibrationPaletteV1.WarmPeachGlow.toColor()   // brightest, warm focal
                        } else {
                            NyraCalibrationPaletteV1.WheelRowFaded.toColor()    // colder / faded
                        },
                        fontSize = if (isSelected) 26.sp else 14.sp,
                        fontWeight = if (isSelected) FontWeight.Normal else FontWeight.Light,
                        modifier = Modifier.alpha(
                            when (abs(offset)) {
                                0 -> 1f
                                1 -> 0.55f
                                else -> 0.22f
                            }
                        )
                    )
                }
            }
        }
    }
}

private fun daysInMonth(month: Int, year: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (isLeapYear(year)) 29 else 28
    else -> 30
}

private fun isLeapYear(year: Int): Boolean =
    (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

private fun coerceDay(day: Int, month: Int, year: Int): Int =
    day.coerceAtMost(daysInMonth(month, year))

private fun monthShortName(month: Int): String = when (month) {
    1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"; 5 -> "May"; 6 -> "Jun"
    7 -> "Jul"; 8 -> "Aug"; 9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
    else -> "—"
}

// Long.toColor() is provided by AtmosphericComponents.kt (internal extension)
// in the same package; we re-use it here rather than redeclare.
