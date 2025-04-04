package com.mixedwash.core.orders.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingData(
    @SerialName("pickup_slot")
    val pickupSlotSelected: BookingTimeSlot,
    @SerialName("drop_slot")
    val dropSlotSelected: BookingTimeSlot,
    @SerialName("booking_items")
    val bookingItems: List<BookingItem>,
)
