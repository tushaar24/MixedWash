package com.mixedwash.core.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.mixedwash.ui.theme.components.DefaultCircularProgressIndicator

@Composable
fun ClickableLoadingOverlay(isLoading: Boolean) {
    Crossfade(targetState = isLoading) {
        when (isLoading) {
            true -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .noRippleClickable(enabled = false, onClick = {}),
                contentAlignment = Alignment.Center
            ) {
                DefaultCircularProgressIndicator()
            }

            false -> {}
        }
    }

}