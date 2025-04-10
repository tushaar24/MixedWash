package com.mixedwash.features.services.presentation.model

sealed class PricingMetadataPresentation {
    data class SubItemsPricingPresentation(
        val startingPrice: Int
    ) : PricingMetadataPresentation()

    data class ServicePricingPresentation(
        val pricePerUnit: Int,
        val unit: String,
        val minimumUnits: Int,
        val minimumPrice: Int
    ) : PricingMetadataPresentation()

    fun asSubItemsPricing() = this as? SubItemsPricingPresentation
    fun asServicePricing() = this as? ServicePricingPresentation
}


