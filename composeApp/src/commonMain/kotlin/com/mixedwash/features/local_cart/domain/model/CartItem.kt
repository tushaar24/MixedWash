package com.mixedwash.features.local_cart.domain.model

import com.mixedwash.features.services.presentation.model.ItemPricing
import com.mixedwash.features.services.presentation.model.ServiceItemMetadata

data class CartItem(
    val itemId: String,
    val name: String,
    val metadata: ServiceItemMetadata? = null,
    val itemPricing: ItemPricing,
    val serviceId: String,
    val deliveryTimeMinInHrs: Int,
    val deliveryTimeMaxInHrs: Int? = null,
    val quantity: Int
)

