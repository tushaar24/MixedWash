package com.mixedwash.features.services.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ItemPricingDto {
    @Serializable
    @SerialName("sub_item_ranged_pricing")
    data class SubItemRangedPricingDto(
        @SerialName("min_price") val minPrice: Int,
        @SerialName("max_price") val maxPrice: Int
    ) : ItemPricingDto()

    @Serializable
    @SerialName("sub_item_fixed_pricing")
    data class SubItemFixedPricingDto(
        @SerialName("fixed_price") val fixedPrice: Int
    ) : ItemPricingDto()

    @Serializable
    @SerialName("service_item_pricing")
    data class ServiceItemPricingDto(
        @SerialName("price_per_unit") val pricePerUnit: Int,
        @SerialName("unit") val unit: String,
        @SerialName("minimum_units") val minimumUnits: Int,
        @SerialName("minimum_price") val minimumPrice: Int
    ) : ItemPricingDto()
}
