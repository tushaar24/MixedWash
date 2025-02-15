package com.mixedwash.features.createOrder.domain.models

import com.mixedwash.features.createOrder.presentation.screens.DateSlot
import com.mixedwash.features.createOrder.presentation.screens.Offer

data class SlotsAndOffersEntity(
    val pickupSlots: List<DateSlot>,
    val dropSlots: List<DateSlot>,
    val commonOffers: List<Offer>
)
