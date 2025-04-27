package com.mixedwash.core.feature.orders.domain.model

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
    @SerialName("out_for_delivery_seconds")
    val outForDeliverySeconds: Long? = null,
    @SerialName("delivered_seconds")
    val deliveredSeconds: Long? = null,
    @SerialName("payment_id")
    val paymentId: String? = null,
    @SerialName("is_cancelled")
    val isCancelled: Boolean = false,
    @SerialName("active_cancellation_request_placed_seconds")
    val activeCancellationRequestPlacedSeconds: Long? = null,
    @SerialName("cancellation_reason")
    val cancellationReason: String? = null
)

