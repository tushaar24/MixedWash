package com.mixedwash.features.local_cart.data.model

data class ItemPricingEntity(
    val pricingType: PricingTypeEntity,
    val rangedMinPrice: Int?=null,
    val rangedMaxPrice: Int?=null,
    val fixedPrice: Int?=null,
    val servicePricePerUnit: Int?=null,
    val serviceUnit: String?=null,
    val serviceMinimumUnits: Int?=null,
    val serviceMinimumPrice: Int?=null
)
