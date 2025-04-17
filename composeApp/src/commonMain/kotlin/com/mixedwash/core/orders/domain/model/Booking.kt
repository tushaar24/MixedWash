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
    @SerialName("out_for_pickup_seconds")
    val outForPickupSeconds: Long? = null,
    @SerialName("picked_up_seconds")
    val pickedUpSeconds: Long? = null,
    @SerialName("out_for_delivery_seconds")
    val outForDeliverySeconds: Long? = null,
    @SerialName("delivered_seconds")
    val deliveredSeconds: Long? = null,
    @SerialName("is_paid")
    val isPaid: Boolean = false
)




