package com.mixedwash.features.laundryServices.data.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceResponseEntity(
    @SerialName("service_name")
    val serviceName: String,
    @SerialName("price")
    val price: Double,
    @SerialName("service_id")
    val serviceId: String,
    @SerialName("unit")
    val unit: String
)

