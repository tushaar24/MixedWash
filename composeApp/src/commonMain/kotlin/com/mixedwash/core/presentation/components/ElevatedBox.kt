package com.mixedwash.core.presentation.components

import BrandTheme
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DropShadowConfig(
    val shadowColor: Color = Color.Black,
    val alpha: Float = 0.02f,
    val shape: Shape = RectangleShape,
    val verticalDirection: ShadowDirection.Vertical = ShadowDirection.Vertical.Top,
    val spread: Int = 1,
)

@Composable
fun ElevatedBox(
    elevation: Dp,
    modifier: Modifier = Modifier,
    dropShadowConfig: DropShadowConfig = DropShadowConfig(),
    backgroundColor: Color = BrandTheme.colors.background,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp, horizontal = 0.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val dir = if (dropShadowConfig.verticalDirection == ShadowDirection.Vertical.Bottom) 1 else -1
    Box(
        modifier.dropShadow(
            offsetY = elevation * dir * dropShadowConfig.spread,
            color = dropShadowConfig.shadowColor.copy(alpha = dropShadowConfig.alpha),
            blur = elevation,
            shape = dropShadowConfig.shape
        ).background(backgroundColor).padding(contentPadding)
    ) {
        content()
    }
}

@Composable
fun ElevatedShape(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    dropShadowConfig: DropShadowConfig = DropShadowConfig(),
    content: @Composable BoxScope.() -> Unit,
) {
    val isScrolledDown by remember {
        derivedStateOf { scrollState.value == scrollState.maxValue }
    }

    val currentElevation by animateDpAsState(if (isScrolledDown) 0.dp else elevation)

    ElevatedBox(
        modifier = modifier,
        elevation = currentElevation,
        dropShadowConfig = dropShadowConfig,
        content = content
    )
}

sealed class ShadowDirection {
    enum class Vertical { Bottom, Top }
//    enum class Horizontal { Left, Right }
}