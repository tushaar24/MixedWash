package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.HomeScreenDataDto

data class HomeScreenData(
    val banner: HomeBanner,
    val offerCards: List<HomeOfferCard>?,
    val services: List<HomeService>,
    val activeOrders: List<OrderStatus>?,
    val introSection: IntroSection?,
    val schemaVersion: String
)

// Converter from HomeScreenDataDto to HomeScreenState
fun HomeScreenDataDto.toPresentation(): HomeScreenData = HomeScreenData(
    banner = homeBanner.toPresentation(),
    offerCards = offerCards?.map { it.toPresentation() },
    services = homeServices.map { it.toPresentation() },
    activeOrders = activeOrders?.map { it.toPresentation() },
    introSection = introSection?.toPresentation(),
    schemaVersion = schemaVersion
)



