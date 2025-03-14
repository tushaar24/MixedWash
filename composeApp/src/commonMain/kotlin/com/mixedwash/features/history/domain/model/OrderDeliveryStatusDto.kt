package com.mixedwash.features.history.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OrderDeliveryStatusDto {
    @SerialName("processing") PROCESSING,
    @SerialName("delivered") DELIVERED,
    @SerialName("cancelled") Cancelled
}
