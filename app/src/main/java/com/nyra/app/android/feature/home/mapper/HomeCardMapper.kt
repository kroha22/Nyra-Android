package com.nyra.app.android.feature.home.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.nyra.app.android.core.model.CardTemplate
import com.nyra.app.android.core.model.IconToken
import com.nyra.app.android.feature.home.model.HomeCardUiModel
import com.nyra.app.android.feature.home.model.HomeCardVisualState

fun CardTemplate.toHomeCardUiModel(
    isHighlighted: Boolean = false
): HomeCardUiModel {
    return HomeCardUiModel(
        id = id,
        type = type,
        title = title,
        subtitle = subtitle,
        icon = iconToken.toImageVector(),
        visualToken = visualToken,
        action = action,
        priority = priority,
        visualState = when {
            !isActive -> HomeCardVisualState.Disabled
            isHighlighted -> HomeCardVisualState.Highlighted
            else -> HomeCardVisualState.Default
        }
    )
}

private fun String?.toImageVector(): ImageVector? = when (this) {
    IconToken.Heart -> heartIcon()
    IconToken.Leaf -> leafIcon()
    IconToken.Wave -> waveIcon()
    IconToken.Journal -> journalIcon()
    IconToken.Presence -> presenceIcon()
    else -> null
}

private fun heartIcon() = ImageVector.Builder(
    name = "heart", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f
).apply {
    path(stroke = SolidColor(Color.White), strokeLineWidth = 1.5f) {
        moveTo(12f, 21.35f)
        lineTo(10.55f, 20.03f)
        curveTo(5.4f, 15.36f, 2f, 12.28f, 2f, 8.5f)
        curveTo(2f, 5.42f, 4.42f, 3f, 7.5f, 3f)
        curveTo(9.24f, 3f, 10.91f, 3.81f, 12f, 5.09f)
        curveTo(13.09f, 3.81f, 14.76f, 3f, 16.5f, 3f)
        curveTo(19.58f, 3f, 22f, 5.42f, 22f, 8.5f)
        curveTo(22f, 12.28f, 18.6f, 15.36f, 13.45f, 20.04f)
        lineTo(12f, 21.35f)
        close()
    }
}.build()

private fun leafIcon() = ImageVector.Builder(
    name = "leaf", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f
).apply {
    path(stroke = SolidColor(Color.White), strokeLineWidth = 1.5f) {
        moveTo(17f, 8f)
        curveTo(14f, 8f, 11f, 11f, 11f, 11f)
        curveTo(11f, 11f, 8f, 14f, 8f, 17f)
        lineTo(8f, 21f)
        lineTo(12f, 21f)
        curveTo(15f, 21f, 18f, 18f, 18f, 18f)
        curveTo(18f, 18f, 21f, 15f, 21f, 12f)
        lineTo(21f, 8f)
        lineTo(17f, 8f)
        close()
        moveTo(8f, 21f)
        lineTo(3f, 21f)
    }
}.build()

private fun waveIcon() = ImageVector.Builder(
    name = "wave", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f
).apply {
    path(stroke = SolidColor(Color.White), strokeLineWidth = 1.5f) {
        moveTo(2f, 12f)
        curveTo(4f, 12f, 5f, 10f, 7f, 10f)
        curveTo(9f, 10f, 10f, 14f, 12f, 14f)
        curveTo(14f, 14f, 15f, 10f, 17f, 10f)
        curveTo(19f, 10f, 20f, 12f, 22f, 12f)
    }
}.build()

private fun journalIcon() = ImageVector.Builder(
    name = "journal", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f
).apply {
    path(stroke = SolidColor(Color.White), strokeLineWidth = 1.5f) {
        moveTo(4f, 4f)
        horizontalLineTo(20f)
        verticalLineTo(20f)
        horizontalLineTo(4f)
        close()
        moveTo(7f, 8f)
        horizontalLineTo(17f)
        moveTo(7f, 12f)
        horizontalLineTo(17f)
        moveTo(7f, 16f)
        horizontalLineTo(13f)
    }
}.build()

private fun presenceIcon() = ImageVector.Builder(
    name = "presence", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f
).apply {
    path(stroke = SolidColor(Color.White), strokeLineWidth = 1.5f) {
        moveTo(12f, 12f)
        moveTo(12f, 2f)
        curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
        curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
        curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
        curveTo(22f, 6.48f, 17.52f, 2f, 12f, 2f)
        close()
        moveTo(12f, 7f)
        curveTo(9.24f, 7f, 7f, 9.24f, 7f, 12f)
        curveTo(7f, 14.76f, 9.24f, 17f, 12f, 17f)
        curveTo(14.76f, 17f, 17f, 14.76f, 17f, 12f)
        curveTo(17f, 9.24f, 14.76f, 7f, 12f, 7f)
        close()
    }
}.build()
