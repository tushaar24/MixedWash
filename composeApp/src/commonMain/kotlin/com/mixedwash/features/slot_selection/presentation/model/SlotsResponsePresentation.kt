package com.mixedwash.features.slot_selection.presentation.model

data class SlotsResponsePresentation(
    val pickupSlots: List<DateSlotPresentation>,
    val dropSlots: List<DateSlotPresentation>,
    val commonOffers: List<OfferPresentation>?=null
) 