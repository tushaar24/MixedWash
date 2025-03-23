package com.mixedwash.core.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ElevatedBox(
    modifier: Modifier = Modifier,
    shadowColor: Color = Color.Red,
    alpha: Float = 0.02f,
    shape: Shape = RectangleShape,
    verticalDirection: ShadowDirection.Vertical = ShadowDirection.Vertical.Top,
    spread: Int = 1,
    backgroundColor: Color = BrandTheme.colors.background,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 0.dp),
    elevation: Dp,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier.dropShadow(
            offsetY = elevation * (if (verticalDirection == ShadowDirection.Vertical.Bottom) 1 else -1) * spread,
            color = shadowColor.copy(alpha = alpha),
            blur = elevation,
            shape = shape
        ).background(backgroundColor).padding(contentPadding)
    ) {
        content()
    }
}

sealed class ShadowDirection {
    enum class Vertical { Bottom, Top }
//    enum class Horizontal { Left, Right }
}