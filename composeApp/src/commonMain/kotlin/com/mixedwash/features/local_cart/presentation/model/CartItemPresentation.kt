package com.mixedwash.features.local_cart.presentation.model

import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.data.model.GenderEntity
import com.mixedwash.features.local_cart.data.model.ItemMetadataEntity
import com.mixedwash.features.local_cart.data.model.ItemPricingEntity
import com.mixedwash.features.local_cart.data.model.PricingTypeEntity
import com.mixedwash.features.services.presentation.model.GenderPresentation
import com.mixedwash.features.services.presentation.model.ItemPricingPresentation
import com.mixedwash.features.services.presentation.model.ServiceItemMetadataPresentation

data class CartItemPresentation(
    val itemId: String,
    val name: String,
    val metadata: ServiceItemMetadataPresentation? = null,
    val itemPricing: ItemPricingPresentation,
    val serviceId: String,
    val deliveryTimeMinInHrs: Int,
    val deliveryTimeMaxInHrs: Int? = null,
    val quantity: Int
)

