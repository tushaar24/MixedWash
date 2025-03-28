package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.home.data.models.HomeScreenDataDto

data class HomeScreenData(
    val banners: List<HomeBanner>,
    val offerCards: List<HomeOfferCard>?,
    val services: List<HomeService>,
    val activeOrders: List<OrderStatus>?,
    val preferredBannerId: String,
    val introSection: IntroSection?,
    val schemaVersion: String
)

// Converter from HomeScreenDataDto to HomeScreenState
fun HomeScreenDataDto.toPresentation(): HomeScreenData = HomeScreenData(
    banners = this.homeBanners.map { it.toPresentation() },
    offerCards = offerCards?.map { it.toPresentation() },
    services = homeServices.map { it.toPresentation() },
    activeOrders = activeOrders?.map { it.toPresentation() },
    introSection = introSection?.toPresentation(),
    schemaVersion = schemaVersion,
    preferredBannerId = preferredBannerId
)



