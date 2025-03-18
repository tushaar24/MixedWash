package com.mixedwash.features.booking_details.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.ui.theme.Gray800


@Composable
fun ActionText(modifier: Modifier = Modifier, text: String, action: () -> Unit) {
    Box(
        modifier = modifier
            .noRippleClickable(onClick = action)
            .drawBehind {

                val strokeWidth = 0.5.dp.value * density
                val y = size.height - strokeWidth / 2

                drawLine(
                    color = Gray800, Offset(0f, y), Offset(size.width, y), strokeWidth
                )
            },
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 1.em,
        )
    }

}