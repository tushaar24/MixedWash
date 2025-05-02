package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.IntroSectionDto

// IntroSection.kt
data class IntroSection(
    val heading: String,
    val imageUrl: String,
    val description: String,
    val buttonText: String,
    val contentTextColor: String,
    val buttonTextColor: String,
    val gradient: Gradient
)

// Converter from IntroSectionDto to IntroSection
fun IntroSectionDto.toPresentation(): IntroSection = IntroSection(
    heading = heading,
    imageUrl = imageUrl,
    description = description,
    buttonText = buttonText,
    gradient = gradient.toPresentation(),
    contentTextColor = contentTextColor,
    buttonTextColor = buttonTextColor
)
