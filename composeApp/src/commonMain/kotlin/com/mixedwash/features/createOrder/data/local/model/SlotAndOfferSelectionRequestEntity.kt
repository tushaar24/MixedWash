package com.mixedwash.features.createOrder.data.local.model

import com.mixedwash.features.createOrder.presentation.slot_selection.TimeSlot

data class SlotAndOfferSelectionRequestEntity(
    val pickupSlot: TimeSlot,
    val dropSlot: TimeSlot,
    val offer: String?=null,
    val deliveryNotes: String,
)