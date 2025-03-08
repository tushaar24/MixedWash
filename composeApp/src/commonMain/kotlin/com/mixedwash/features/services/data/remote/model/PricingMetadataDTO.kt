package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PricingMetadataDTO {
    @Serializable
    @SerialName("sub_items_pricing")
    data class SubItemsPricingDTO(
        @SerialName("starting_price") val startingPrice: Int
    ) : PricingMetadataDTO()
    @Serializable
    @SerialName("service_pricing")
    data class ServicePricingDTO(
        @SerialName("price_per_unit") val pricePerUnit: Int,
        @SerialName("unit") val unit: String,
        @SerialName("minimum_units") val minimumUnits: Int,
        @SerialName("minimum_price") val minimumPrice: Int
    ) : PricingMetadataDTO()
}