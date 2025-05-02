package com.mixedwash.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@Composable
fun Modifier.noRippleClickable(enabled: Boolean = true, onClick: (() -> Unit)?): Modifier =
    composed {
        this.clickable(
            indication = null,
            enabled = enabled,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick ?: {}
        )
    }
