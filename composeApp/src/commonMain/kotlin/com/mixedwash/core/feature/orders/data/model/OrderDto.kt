package com.mixedwash.core.feature.orders.data.model

import com.mixedwash.core.feature.orders.domain.model.BookingOffer
import com.mixedwash.features.address.domain.model.Address
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("created_at_seconds")
    val createdAtSeconds: Long = Clock.System.now().epochSeconds,
    @SerialName("out_for_pickup_seconds")
    val outForPickupSeconds: Long? = null,
    @SerialName("picked_up_seconds")
    val pickedUpSeconds: Long? = null,
    @SerialName("offers")
    val offers: List<BookingOffer>? = null,
    @SerialName("delivery_notes")
    val deliveryNotes: String,
    @SerialName("address")
    val address: Address,
)