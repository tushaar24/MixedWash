package com.mixedwash.features.slot_selection.domain.model.response

data class SlotsResponse(
    val pickupSlots: List<DateSlot>,
    val dropSlots: List<DateSlot>,
    val commonOffers: List<Offer>?=null
) 