package com.mixedwash.features.slot_selection.data.model.response

import com.mixedwash.features.slot_selection.data.model.DateSlotDto
import com.mixedwash.features.slot_selection.data.model.OfferDto

data class AvailableSlotsResponseDto(
    val pickupSlots: List<DateSlotDto>,
    val dropSlots: List<DateSlotDto>,
    val commonOffers: List<OfferDto>?=null
)
