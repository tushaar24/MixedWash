package com.mixedwash.core.orders.domain.model

import com.mixedwash.features.address.domain.model.Address
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("id")
    val id: String,
    @SerialName("bookings")
    val bookings: List<Booking> = emptyList(),
    @SerialName("customer_id")
    val customerId: String,
    @SerialName("created_at_millis")
    val createdAtMillis: Long = Clock.System.now().toEpochMilliseconds(),
    @SerialName("notes")
    val notes: String? = null,
    @SerialName("offers")
    val offers: List<BookingOffer>? = null,
    @SerialName("delivery_notes")
    val deliveryNotes: String,
    @SerialName("address")
    val address: Address,
)