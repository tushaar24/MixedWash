package com.mixedwash.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomBox(
    modifier: Modifier = Modifier,
    elevation: Dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier.dropShadow(
            offsetY = elevation * -1,
            color = Color.Black.copy(alpha = 0.02f),
            blur = elevation,
            shape = RectangleShape
        ).background(BrandTheme.colors.background).padding(vertical = 16.dp)
    ) {
        content()
    }
}
