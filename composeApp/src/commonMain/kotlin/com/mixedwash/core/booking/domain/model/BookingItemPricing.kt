package com.mixedwash.core.booking.domain.model

sealed class BookingItemPricing {
    data class SubItemRangedPricing(
        val minPrice: Int,
        val maxPrice: Int
    ) : BookingItemPricing()

    data class SubItemFixedPricing(
        val fixedPrice: Int
    ) : BookingItemPricing()

    data class ServiceItemPricing(
        val pricePerUnit: Int,
        val unit: String,
        val minimumUnits: Int,
        val minimumPrice: Int
    ) : BookingItemPricing()
}
