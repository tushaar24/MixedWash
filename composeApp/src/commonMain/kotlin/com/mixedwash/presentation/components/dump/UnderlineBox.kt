package com.mixedwash.presentation.components.dump

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UnderlineBox(lineColor: Color = Color.Unspecified, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = Modifier.drawBehind {
        val strokeWidth = 1.dp.toPx()  // Line thickness
        val y = size.height - strokeWidth / 2  // Position at the bottom edge

        drawLine(
            color = lineColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth
        )
    }) {
        content()
    }
}