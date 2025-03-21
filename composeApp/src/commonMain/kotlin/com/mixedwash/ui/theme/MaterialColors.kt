package com.mixedwash.ui.theme

import androidx.compose.ui.graphics.Color

data class MaterialColors(
    val c50: Color,
    val c100: Color,
    val c200: Color,
    val c300: Color,
    val c400: Color,
    val c500: Color,
    val c600: Color,
    val c700: Color,
    val c800: Color,
    val c900: Color
) {
    val lighter = c50
    val light = c100
    val normal = c400
    val normalDark = c500
    val dark = c800
    val darker = c900
}
