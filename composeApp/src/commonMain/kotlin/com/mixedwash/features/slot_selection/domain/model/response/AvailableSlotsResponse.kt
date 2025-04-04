package com.mixedwash.features.slot_selection.domain.model.response

data class AvailableSlotsResponse(
    val pickupSlots: List<DateSlot>,
    val dropSlots: List<DateSlot>,
    val commonOffers: List<Offer>?=null
) 