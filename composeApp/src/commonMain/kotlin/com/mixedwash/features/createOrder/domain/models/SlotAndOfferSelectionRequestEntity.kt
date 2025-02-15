package com.mixedwash.features.createOrder.domain.models

import com.mixedwash.features.createOrder.presentation.screens.TimeSlot

data class SlotAndOfferSelectionRequestEntity(
    val pickupSlot: TimeSlot,
    val dropSlot: TimeSlot,
    val offer: String?=null,
    val deliveryNotes: String,
)