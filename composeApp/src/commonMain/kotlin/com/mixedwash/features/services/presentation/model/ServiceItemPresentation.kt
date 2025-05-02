package com.mixedwash.features.services.presentation.model

data class ServiceItemPresentation(
    val itemId: String,
    val serviceId: String,
    val serviceName:String,
    val serviceImageUrl: String,
    val name: String,
    val metadata: ServiceItemMetadata? = null,
    val itemPricing: ItemPricing
)
