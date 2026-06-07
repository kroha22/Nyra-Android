package com.nyra.app.android.core.onboarding.runtime.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.Orientation
import com.nyra.app.android.core.ui_state.resolver.NyraNocturnePalette
import kotlin.math.abs

/**
 * Slim cinematic date wheel — three columns (day / month / year) with vertical
 * drag-to-scroll. Each column shows a small window of values centred on the
 * current selection; rows fade toward the edges to suggest atmospheric depth.
 *
 * Deliberately minimalist — no snap animation, no haptic ticks. Drag accumulates
 * fractional offset that translates to integer index steps every ~36dp; feels
 * heavy and calm rather than playful. Refine further when motion polish matters.
 */
@Composable
fun CinematicDatePicker(
    day: Int,
    month: Int,
    year: Int,
    onDateChange: (day: Int, month: Int, year: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
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

@Composable
private fun WheelColumn(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    format: (Int) -> String,
    modifier: Modifier = Modifier
) {
    var accumulator by remember { mutableStateOf(0f) }
    val stepPx = 110f // drag distance for one index step

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                color = NyraNocturnePalette.RestingSurface.toColor(),
                shape = RoundedCornerShape(18.dp)
            )
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
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (-2..2).forEach { offset ->
                val candidate = value + offset
                val visible = candidate in range
                Text(
                    text = if (visible) format(candidate) else "",
                    color = NyraNocturnePalette.TextPrimary.toColor(),
                    fontSize = if (offset == 0) 26.sp else 16.sp,
                    fontWeight = if (offset == 0) FontWeight.Light else FontWeight.Light,
                    modifier = Modifier
                        .alpha(
                            when (abs(offset)) {
                                0 -> 1f
                                1 -> 0.45f
                                else -> 0.18f
                            }
                        )
                        .padding(horizontal = 4.dp)
                )
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
