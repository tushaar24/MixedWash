package com.mixedwash.core.booking.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingItem(
    @SerialName("item_id")
    val itemId: String,
    @SerialName("name")
    val name: String,
    @SerialName("service_name")
    val serviceName: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("item_pricing")
    val itemPricing: BookingItemPricing,
    @SerialName("service_id")
    val serviceId: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("created_millis")
    val createdMillis: Long
)



