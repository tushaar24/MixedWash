package com.mixedwash.core.booking.domain.model

import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.slot_selection.data.model.TimeSlotDto

data class BookingData(
    val pickupSlot: TimeSlotDto,
    val dropSlot: TimeSlotDto,
    val bookingItems : List<BookingItem>,
    val offer: String?=null,
    val deliveryNotes: String,
    val address: Address
)