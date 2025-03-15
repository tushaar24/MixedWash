package com.mixedwash.core.presentation.components.dump

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun GradientBox(
    gradientDark: Color,
    gradientLight: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth().fillMaxHeight(0.9f).background(
            brush = Brush.linearGradient(
                colors = listOf(gradientLight, gradientDark)
            )
        )
    )
}
