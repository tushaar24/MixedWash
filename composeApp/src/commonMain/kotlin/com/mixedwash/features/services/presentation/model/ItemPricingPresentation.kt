package com.mixedwash.features.services.presentation.model

sealed class ItemPricingPresentation {
    data class SubItemRangedPricingPresentation(
        val minPrice: Int,
        val maxPrice: Int
    ) : ItemPricingPresentation()

    data class SubItemFixedPricingPresentation(
        val fixedPrice: Int
    ) : ItemPricingPresentation()

    data class ServiceItemPricingPresentation(
        val pricePerUnit: Int,
        val unit: String,
        val minimumUnits: Int,
        val minimumPrice: Int
    ) : ItemPricingPresentation()
}
