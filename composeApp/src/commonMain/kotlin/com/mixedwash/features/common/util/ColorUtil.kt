package com.mixedwash.features.common.util

import androidx.compose.ui.graphics.Color

fun Color.Companion.parse(colorHex : String) : Color {
    return Color(colorHex.removePrefix("#").toLong(16) or 0x00000000FF000000)
}
