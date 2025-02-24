package com.mixedwash.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin


fun Modifier.gradient(
    colorStops: Array<Pair<Float, Color>>,
    angle: Float = 0f, // Angle in degrees
): Modifier = this.then(
    drawBehind {
        // Convert the angle to radians
        val angleRad = toRadians(angle.toDouble())
        // Find the center of the drawing area
        val center = Offset(size.width / 2, size.height / 2)
        // Calculate half the diagonal length to ensure the gradient covers the full area
        val halfDiagonal = hypot(size.width, size.height) / 2f
        // Determine the offset vector based on the angle
        val offset = Offset(
            x = (cos(angleRad) * halfDiagonal).toFloat(),
            y = (sin(angleRad) * halfDiagonal).toFloat()
        )
        // Define the start and end points for the gradient line
        val start = center - offset
        val end = center + offset

        drawRect(
            brush = Brush.linearGradient(
                colorStops = colorStops,
                start = start,
                end = end
            ),
            size = size
        )
    }
)

private fun toRadians(deg: Double): Double = deg / 180.0 * PI