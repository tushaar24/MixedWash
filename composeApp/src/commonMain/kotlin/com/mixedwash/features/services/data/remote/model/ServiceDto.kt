package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceDto(
    @SerialName("service_id") val serviceId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("items") val items: List<ServiceItemDto>? = null,
    @SerialName("pricing_metadata") val pricingMetadata: PricingMetadataDTO? = null,
    @SerialName("inclusions") val inclusions: String? = null,
    @SerialName("exclusions") val exclusions: String,
    @SerialName("delivery_time_min_in_hrs") val deliveryTimeMinInHrs: Int,
    @SerialName("delivery_time_max_in_hrs") val deliveryTimeMaxInHrs: Int,
    @SerialName("details") val details: List<ServiceDetailDto>
)







