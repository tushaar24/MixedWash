package com.mixedwash.features.services.presentation.model

data class ServiceItemPresentation(
    val itemId: String,
    val serviceId: String,
    val name: String,
    val metadata: ServiceItemMetadataPresentation? = null,
    val itemPricing: ItemPricingPresentation
)
