package com.mixedwash.features.common.presentation.home.model

import androidx.compose.ui.graphics.Color

data class HomeHeaderData(
    val heading: String,
    val imageUrl: String,
    val description: String,
    val buttonText: String,
    val textColor: Color,
    val gradientLight: Color,
    val gradientDark: Color,
    val gradientOrder: Int = 1, /* 1 for light-dark, 2 for dark-light */
)
