package com.mixedwash.core.orders.data.model

import com.mixedwash.core.orders.domain.model.BookingOffer
import com.mixedwash.features.address.domain.model.Address
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * OrderDoc represents the Firebase Firestore document structure for an order.
 * This class is specifically designed for data transfer and storage purposes,
 * with field names matching the Firestore document fields via @SerialName annotations.
 *
 * Note that this model differs slightly from the domain layer Order model:
 *
 * The transformation between OrderDoc and Order happens in the repository layer.
 */
@Serializable
data class OrderDoc(
    @SerialName("id")
    val id: String,
    @SerialName("customer_id")
    val customerId: String,
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