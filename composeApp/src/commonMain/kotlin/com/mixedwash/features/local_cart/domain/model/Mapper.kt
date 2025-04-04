package com.mixedwash.features.local_cart.domain.model

import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.data.model.GenderEntity
import com.mixedwash.features.local_cart.data.model.ItemMetadataEntity
import com.mixedwash.features.local_cart.data.model.ItemPricingEntity
import com.mixedwash.features.local_cart.data.model.PricingTypeEntity
import com.mixedwash.features.services.presentation.model.Gender
import com.mixedwash.features.services.presentation.model.ItemPricing
import com.mixedwash.features.services.presentation.model.ServiceItemMetadata
import com.mixedwash.features.services.presentation.model.ServiceItemPresentation

fun CartItemEntity.toDomain() = CartItem (
    itemId = itemId,
    name = name,
    metadata = metadata?.toDomain(),
    itemPricing = itemPricing.toDomain(),
    serviceId = serviceId,
    deliveryTimeMinInHrs = deliveryTimeMinInHrs,
    deliveryTimeMaxInHrs = deliveryTimeMaxInHrs,
    quantity = quantity,
    serviceName = serviceName,
    serviceImageUrl = serviceImageUrl
)

fun ItemMetadataEntity.toDomain() = ServiceItemMetadata(
    imageUrl = imageUrl,
    gender = gender?.toDomain()
)

fun ItemPricingEntity.toDomain() : ItemPricing = when (pricingType) {
    PricingTypeEntity.FIXED -> ItemPricing.SubItemFixedPricingPresentation(
        fixedPrice = fixedPrice ?: 0
    )
    PricingTypeEntity.RANGED -> ItemPricing.SubItemRangedPricingPresentation(
        minPrice = rangedMinPrice ?: 0,
        maxPrice = rangedMaxPrice ?: 0
    )
    PricingTypeEntity.SERVICE -> ItemPricing.ServiceItemPricingPresentation(
        pricePerUnit = servicePricePerUnit ?: 0,
        unit = serviceUnit ?: "",
        minimumPrice = serviceMinimumPrice ?: 0,
        minimumUnits = serviceMinimumUnits ?: 0,
    )
}

fun GenderEntity.toDomain() = when (this) {
    GenderEntity.FEMALE -> Gender.FEMALE
    GenderEntity.MALE -> Gender.MALE
}

fun ServiceItemPresentation.toCartItem(deliveryTimeMinInHrs: Int, deliveryTimeMaxInHrs: Int?) = CartItem(
    itemId = itemId,
    name = name,
    metadata = metadata,
    itemPricing = itemPricing,
    serviceId = serviceId,
    deliveryTimeMinInHrs = deliveryTimeMinInHrs,
    deliveryTimeMaxInHrs = deliveryTimeMaxInHrs,
    quantity = 0,
    serviceName = serviceName,
    serviceImageUrl = serviceImageUrl
)
