package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.HomeServicesDto

// HomeService.kt
data class HomeService(
    val serviceID: String,
    val title: String,
    val description: String,
    val imageUrl: String
)

// Converter from HomeServicesDto to HomeService
fun HomeServicesDto.toPresentation(): HomeService = HomeService(
    serviceID = serviceID,
    title = title,
    imageUrl = imageUrl,
    description = description
)
