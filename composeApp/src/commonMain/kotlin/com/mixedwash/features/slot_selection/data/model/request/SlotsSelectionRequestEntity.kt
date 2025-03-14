package com.mixedwash.features.slot_selection.data.model.request

import com.mixedwash.features.slot_selection.data.model.TimeSlotDto

data class SlotsSelectionRequestEntity(
    val pickupSlot: TimeSlotDto,
    val dropSlot: TimeSlotDto,
    val offer: String?=null,
    val deliveryNotes: String,
)