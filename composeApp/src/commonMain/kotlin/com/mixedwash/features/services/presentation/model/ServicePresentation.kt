package com.mixedwash.features.services.presentation.model

data class ServicePresentation(
    val serviceId: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val items: List<ServiceItemPresentation>? = null,
    val pricingMetadata: PricingMetadataPresentation? = null,
    val inclusions: String? = null,
    val exclusions: String,
    val deliveryTimeMinInHrs: Int,
    val deliveryTimeMaxInHrs: Int,
    val details: List<ServiceDetailPresentation>
)



