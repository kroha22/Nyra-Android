package com.nyra.app.android.feature.home.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.nyra.app.android.core.model.CardAction
import com.nyra.app.android.core.model.CardType

data class HomeCardUiModel(
    val id: String,
    val type: CardType,
    val title: String,
    val subtitle: String?,
    val icon: ImageVector?,
    val visualToken: String?,
    val action: CardAction,
    val priority: Int,
    val visualState: HomeCardVisualState
)

enum class HomeCardVisualState {
    Default,
    Highlighted,
    Disabled
}
