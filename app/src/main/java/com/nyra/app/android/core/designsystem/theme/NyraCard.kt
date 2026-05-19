package com.nyra.app.android.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NyraCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(NyraSpacing.md),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = NyraSurface,
                shape = RoundedCornerShape(28.dp)
            )
            .padding(contentPadding)
    ) {
        content()
    }
}