package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.GradientColorDto

// GradientColor.kt
data class GradientColor(
    val colorHex: String,
    val colorStop: Float
)

// Converter from GradientColorDto to GradientColor
fun GradientColorDto.toPresentation(): GradientColor = GradientColor(
    colorHex = colorHex,
    colorStop = colorStop
)
