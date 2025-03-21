package com.mixedwash.features.services.presentation.model

sealed class ItemPricing {
    data class SubItemRangedPricingPresentation(
        val minPrice: Int,
        val maxPrice: Int
    ) : ItemPricing()

    data class SubItemFixedPricingPresentation(
        val fixedPrice: Int
    ) : ItemPricing()

    data class ServiceItemPricingPresentation(
        val pricePerUnit: Int,
        val unit: String,
        val minimumUnits: Int,
        val minimumPrice: Int
    ) : ItemPricing()
}
