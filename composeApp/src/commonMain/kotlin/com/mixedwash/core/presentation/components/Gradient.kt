package com.mixedwash.core.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.mixedwash.features.common.util.parse
import com.mixedwash.features.home.presentation.model.Gradient
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin


fun Modifier.gradient(
    gradient: Gradient,
): Modifier = this.then(
    drawBehind {
        // Convert the angle to radians
        val angleRad = toRadians(gradient.angle.toDouble()+180f)
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

        var colorStopsArray = gradient.gradientColors.map {
            Pair(it.colorStop, Color.parse(it.colorHex))
        }.toTypedArray()

        if (colorStopsArray.isEmpty()) {
            colorStopsArray = arrayOf(Pair(0f, Color.Transparent), Pair(1f, Color.Transparent))
        }

        if(colorStopsArray.size == 1) {
            val color = colorStopsArray[0].second
            colorStopsArray = arrayOf(Pair(0f,color), Pair(1f,color))
        }

        drawRect(
            brush = Brush.linearGradient(
                colorStops = colorStopsArray,
                start = start,
                end = end
            ),
            size = size
        )
    }
)

private fun toRadians(deg: Double): Double = deg / 180.0 * PI