package com.mixedwash.features.local_cart.domain.model

import com.mixedwash.features.services.presentation.model.ItemPricing
import com.mixedwash.features.services.presentation.model.ServiceItemMetadata

data class CartItem(
    val itemId: String,
    val serviceId: String,
    val serviceName:String,
    val serviceImageUrl: String,
    val name: String,
    val metadata: ServiceItemMetadata? = null,
    val itemPricing: ItemPricing,
    val deliveryTimeMinInHrs: Int,
    val deliveryTimeMaxInHrs: Int? = null,
    val quantity: Int
)

