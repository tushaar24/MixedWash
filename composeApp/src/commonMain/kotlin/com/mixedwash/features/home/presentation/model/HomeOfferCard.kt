package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.HomeOfferCardDto

data class HomeOfferCard(
    val text: String,
    val bigText: List<String>,
    val offerId: String,
    val imageUrl: String,
    val buttonLabel: String,
    val link: String,
    val contentTextColor: String,
    val buttonTextColor: String,
    val gradient: Gradient
)

// Converter from HomeOfferCardDto to HomeOfferCard
fun HomeOfferCardDto.toPresentation(): HomeOfferCard = HomeOfferCard(
    text = text,
    bigText= bigText,
    offerId = offerId,
    imageUrl = imageUrl,
    buttonLabel = buttonLabel,
    link = link,
    gradient = gradient.toPresentation(),
    contentTextColor = contentTextColor,
    buttonTextColor = buttonTextColor
)
