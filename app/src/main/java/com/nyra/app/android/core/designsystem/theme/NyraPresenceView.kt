package com.nyra.app.android.core.designsystem.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun NyraPresenceView(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.size(180.dp)
    ) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NyraWarmGlow,
                    NyraSoftMist.copy(alpha = 0.75f),
                    NyraSoftMist.copy(alpha = 0.12f)
                ),
                center = Offset(size.width * 0.5f, size.height * 0.45f),
                radius = size.minDimension * 0.48f
            )
        )
    }
}