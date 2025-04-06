package com.mixedwash.core.orders.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    @SerialName("id")
    val id: String,
    @SerialName("pickup_slot")
    val pickupSlotSelected: BookingTimeSlot,
    @SerialName("drop_slot")
    val dropSlotSelected: BookingTimeSlot,
    @SerialName("booking_items")
    val bookingItems: List<BookingItem>,
)




