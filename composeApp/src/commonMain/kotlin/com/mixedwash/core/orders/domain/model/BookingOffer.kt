package com.mixedwash.core.orders.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingOffer(
    @SerialName("code") val code: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String,
)