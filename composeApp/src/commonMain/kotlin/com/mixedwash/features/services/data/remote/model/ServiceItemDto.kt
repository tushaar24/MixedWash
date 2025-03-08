package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceItemDto(
    @SerialName("item_id") val itemId: String,
    @SerialName("name") val name: String,
    @SerialName("metadata") val metadata: ServiceItemMetadataDto? = null,
    @SerialName("item_pricing") val itemPricing: ItemPricingDto,
)
