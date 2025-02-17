package com.mixedwash.features.createOrder.data.local.model

import com.mixedwash.features.createOrder.presentation.slot_selection.DateSlot
import com.mixedwash.features.createOrder.presentation.slot_selection.Offer

data class SlotsAndOffersEntity(
    val pickupSlots: List<DateSlot>,
    val dropSlots: List<DateSlot>,
    val commonOffers: List<Offer>
)
