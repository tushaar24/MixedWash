package com.mixedwash.ui.theme

import BrandTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
fun MixedWashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    BrandTheme(
        colorScheme = ColorScheme.lightScheme,
        typography = Typography.defaultTypography(),
        content = content,
        shapes = Shapes()
    )
}