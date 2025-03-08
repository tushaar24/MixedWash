package com.mixedwash.features.common.data.entities

import com.mixedwash.features.common.presentation.slot_selection.DateSlot
import com.mixedwash.features.common.presentation.slot_selection.Offer

data class SlotsAndOffersEntity(
    val pickupSlots: List<DateSlot>,
    val dropSlots: List<DateSlot>,
    val commonOffers: List<Offer>
)
