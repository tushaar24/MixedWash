package com.mixedwash.core.booking.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingOffer(
    @SerialName("code") val code: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String,
    @SerialName("is_selected") val isSelected: Boolean = false,
    @SerialName("is_available") val isAvailable: Boolean = true
)