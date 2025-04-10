package com.mixedwash.core.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha

fun Modifier.fadeIn(
    durationMillis: Int = 300,
    delayMillis: Int = 0,
): Modifier = composed {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = durationMillis, delayMillis = delayMillis)
    )
    LaunchedEffect(Unit) { visible = true }
    this.alpha(alpha)
}

