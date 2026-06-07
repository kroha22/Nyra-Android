package com.nyra.app.android.core.atmosphere.layers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.nyra.app.android.core.atmosphere.palette.TimeOfDayPalette

/**
 * LAYER 7 — Atmospheric interior overlay (optional silhouette / reflective profile /
 * abstract emotional room-light).
 *
 * Asset slot: the actual PNG/WebP lives under `res/drawable/` and is loaded by id.
 * The slot is **tintable** — the layer applies the current focal hue as a
 * SRC_IN colour filter so the same monochrome asset adapts to every time-of-day
 * palette and emotional state without us shipping per-state PNGs.
 *
 * Per spec: human presence allowed only as distant silhouette, reflective profile,
 * blurred seated figure, atmospheric emotional witness — never full character
 * focus, never mascot energy, never assistant character.
 *
 * If [drawableRes] is null the layer no-ops cleanly.
 */
@Composable
fun AtmosphericInteriorOverlay(
    palette: TimeOfDayPalette,
    @DrawableRes drawableRes: Int?,
    alpha: Float,
    modifier: Modifier = Modifier
) {
    val safeAlpha = alpha.coerceIn(0f, 1f)
    if (drawableRes == null || safeAlpha <= 0.01f) return

    // Tint the monochrome asset toward the current focal hue so it integrates
    // with the rest of the atmosphere across time-of-day and emotional states.
    val tint = ColorFilter.tint(palette.focalInner)

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = tint,
            modifier = Modifier
                .fillMaxSize()
                .alpha(safeAlpha)
        )
    }
}
