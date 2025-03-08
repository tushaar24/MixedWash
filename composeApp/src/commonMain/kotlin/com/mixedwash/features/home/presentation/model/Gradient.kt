package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.GradientDto

// Gradient.kt
data class Gradient(
    val gradientColors: List<GradientColor>,
    val angle: Float
)

// Converter from GradientDto to Gradient
fun GradientDto.toPresentation(): Gradient = Gradient(
    gradientColors = gradientColors.map { it.toPresentation() },
    angle = angle
)
