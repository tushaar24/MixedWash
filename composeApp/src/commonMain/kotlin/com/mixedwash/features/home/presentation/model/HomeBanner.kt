package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.HomeBannerDto

data class HomeBanner(
    val id: String,
    val heading: String,
    val imageUrl: String,
    val description: String,
    val button: String? = null,
    val link: String? = null,
    val uiTextColor: String,
    val contentTextColor: String,
    val gradient: Gradient
)

// Converter from HomeBannerDto to HomeBanner
fun HomeBannerDto.toPresentation(): HomeBanner = HomeBanner(
    id = id,
    heading = heading,
    imageUrl = imageUrl,
    description = description,
    button = button,
    link = link,
    uiTextColor = uiTextColor,
    contentTextColor = contentTextColor,
    gradient = gradient.toPresentation()
)
