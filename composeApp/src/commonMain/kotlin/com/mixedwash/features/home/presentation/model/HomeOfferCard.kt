package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.HomeOfferCardDto

data class HomeOfferCard(
    val text: String,
    val description: String,
    val offerId: String,
    val imageUrl: String,
    val link: String,
    val contentTextColor: String,
    val gradient: Gradient
)

// Converter from HomeOfferCardDto to HomeOfferCard
fun HomeOfferCardDto.toPresentation(): HomeOfferCard = HomeOfferCard(
    text = text,
    offerId = offerId,
    imageUrl = imageUrl,
    link = link,
    gradient = gradient.toPresentation(),
    description = description,
    contentTextColor = contentTextColor,
)
