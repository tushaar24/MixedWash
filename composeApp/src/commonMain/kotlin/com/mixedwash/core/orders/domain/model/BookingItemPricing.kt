package com.mixedwash.core.orders.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class BookingItemPricing {
    @Serializable
    @SerialName("sub_item_ranged_pricing")
    data class SubItemRangedPricing(
        @SerialName("min_price") val minPrice: Int,
        @SerialName("max_price") val maxPrice: Int
    ) : BookingItemPricing()

    @Serializable
    @SerialName("sub_item_fixed_pricing")
    data class SubItemFixedPricing(
        @SerialName("fixed_price") val fixedPrice: Int
    ) : BookingItemPricing()

    @Serializable
    @SerialName("service_item_pricing")
    data class ServiceItemPricing(
        @SerialName("price_per_unit") val pricePerUnit: Int,
        @SerialName("unit") val unit: String,
        @SerialName("minimum_units") val minimumUnits: Int,
        @SerialName("minimum_price") val minimumPrice: Int
    ) : BookingItemPricing()
}
