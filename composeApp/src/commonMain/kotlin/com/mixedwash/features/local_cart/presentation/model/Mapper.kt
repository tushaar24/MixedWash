package com.mixedwash.features.local_cart.presentation.model

import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.data.model.GenderEntity
import com.mixedwash.features.local_cart.data.model.ItemMetadataEntity
import com.mixedwash.features.local_cart.data.model.ItemPricingEntity
import com.mixedwash.features.local_cart.data.model.PricingTypeEntity
import com.mixedwash.features.services.presentation.model.GenderPresentation
import com.mixedwash.features.services.presentation.model.ItemPricingPresentation
import com.mixedwash.features.services.presentation.model.ServiceItemMetadataPresentation

fun CartItemEntity.toPresentation() = CartItemPresentation(
    itemId = itemId,
    name = name,
    metadata = metadata?.toPresentation(),
    itemPricing = itemPricing.toPresentation(),
    serviceId = serviceId,
    deliveryTimeMinInHrs = deliveryTimeMinInHrs,
    deliveryTimeMaxInHrs = deliveryTimeMaxInHrs,
    quantity = quantity
)

fun ItemMetadataEntity.toPresentation() = ServiceItemMetadataPresentation(
    imageUrl = imageUrl,
    gender = gender?.toPresentation()
)

fun ItemPricingEntity.toPresentation() : ItemPricingPresentation = when (pricingType) {
    PricingTypeEntity.FIXED -> ItemPricingPresentation.SubItemFixedPricingPresentation(
        fixedPrice = fixedPrice ?: 0
    )
    PricingTypeEntity.RANGED -> ItemPricingPresentation.SubItemRangedPricingPresentation(
        minPrice = rangedMinPrice ?: 0,
        maxPrice = rangedMaxPrice ?: 0
    )
    PricingTypeEntity.SERVICE -> ItemPricingPresentation.ServiceItemPricingPresentation(
        pricePerUnit = servicePricePerUnit ?: 0,
        unit = serviceUnit ?: "",
        minimumPrice = serviceMinimumPrice ?: 0,
        minimumUnits = serviceMinimumUnits ?: 0,
    )
}

fun GenderEntity.toPresentation() = when (this) {
    GenderEntity.FEMALE -> GenderPresentation.FEMALE
    GenderEntity.MALE -> GenderPresentation.MALE
    GenderEntity.BOTH -> GenderPresentation.BOTH
}