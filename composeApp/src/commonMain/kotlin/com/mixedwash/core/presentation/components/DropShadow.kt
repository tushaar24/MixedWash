package com.mixedwash.core.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Platform-specific function declaration
expect fun NativePaint.setMaskFilter(blurRadius: Float)

// The modified dropShadow modifier
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = this.drawBehind {
    val shadowSize = Size(size.width + spread.toPx() * 2, size.height + spread.toPx() * 2)
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint()
    paint.color = color

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().setMaskFilter(blur.toPx())
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(
            offsetX.toPx() - spread.toPx(),
            offsetY.toPx() - spread.toPx()
        )
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}
