package com.mixedwash.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

data class Shapes(
    val button: CornerBasedShape = RoundedCornerShape(8.dp),
    val textField: CornerBasedShape = RoundedCornerShape(12.dp),
    val roundCard : CornerBasedShape = RoundedCornerShape(16.dp),
    val card: CornerBasedShape = RoundedCornerShape(12.dp),
    val chip: CornerBasedShape = RoundedCornerShape(8.dp),
    val circle: CornerBasedShape = RoundedCornerShape(50),
    val rectangle: CornerBasedShape = RoundedCornerShape(0.dp),
    val bottomSheet: CornerBasedShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
)