package com.mixedwash.core.feature.orders.data.model

import com.mixedwash.core.feature.orders.domain.model.BookingItem
import com.mixedwash.core.feature.orders.domain.model.BookingTimeSlot
import com.mixedwash.features.address.domain.model.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("created_at_seconds")
    val createdAtSeconds: Long,
    @SerialName("delivery_address")
    val deliveryAddress: Address,
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
    val cancellationReason: String? = null,
)