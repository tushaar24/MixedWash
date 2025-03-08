package com.mixedwash.features.common.data.entities

import com.mixedwash.features.common.presentation.slot_selection.TimeSlot

data class SlotAndOfferSelectionRequestEntity(
    val pickupSlot: TimeSlot,
    val dropSlot: TimeSlot,
    val offer: String?=null,
    val deliveryNotes: String,
)