package com.mixedwash.features.services.presentation.model

import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.data.model.GenderEntity
import com.mixedwash.features.local_cart.data.model.ItemPricingEntity
import com.mixedwash.features.local_cart.data.model.PricingTypeEntity
import com.mixedwash.features.local_cart.data.model.ItemMetadataEntity
import com.mixedwash.features.services.data.remote.model.GenderDto
import com.mixedwash.features.services.data.remote.model.ItemPricingDto
import com.mixedwash.features.services.data.remote.model.PricingMetadataDto
import com.mixedwash.features.services.data.remote.model.ServiceDto
import com.mixedwash.features.services.data.remote.model.ServiceDetailDto
import com.mixedwash.features.services.data.remote.model.ServiceItemDto
import com.mixedwash.features.services.data.remote.model.ServiceItemMetadataDto

// ServiceItem mapping
fun ServiceItemDto.toPresentation(serviceId: String): ServiceItemPresentation = ServiceItemPresentation(
    itemId = this.itemId,
    name = this.name,
    metadata = this.metadata?.toPresentation(),
    itemPricing = this.itemPricing.toPresentation(),
    serviceId = serviceId
)

fun ServiceItemMetadataDto.toPresentation(): ServiceItemMetadata =
    ServiceItemMetadata(
        imageUrl = this.imageUrl,
        gender = this.gender?.toPresentation()
    )

// ItemPricing mapping
fun ItemPricingDto.toPresentation(): ItemPricing = when (this) {
    is ItemPricingDto.SubItemRangedPricingDto -> ItemPricing.SubItemRangedPricingPresentation(
        minPrice = this.minPrice,
        maxPrice = this.maxPrice
    )
    is ItemPricingDto.SubItemFixedPricingDto -> ItemPricing.SubItemFixedPricingPresentation(
        fixedPrice = this.fixedPrice
    )
    is ItemPricingDto.ServiceItemPricingDto -> ItemPricing.ServiceItemPricingPresentation(
        pricePerUnit = this.pricePerUnit,
        unit = this.unit,
        minimumUnits = this.minimumUnits,
        minimumPrice = this.minimumPrice
    )
}

// Service mapping
fun ServiceDto.toPresentation(): ServicePresentation = ServicePresentation(
    serviceId = this.serviceId,
    title = this.title,
    description = this.description,
    imageUrl = this.imageUrl,
    note = this.note,
    items = this.items?.map { it.toPresentation(serviceId = serviceId) },
    pricingMetadata = this.pricingMetadata?.toPresentation(),
    inclusions = this.inclusions,
    exclusions = this.exclusions,
    deliveryTimeMinInHrs = this.deliveryTimeMinInHrs,
    deliveryTimeMaxInHrs = this.deliveryTimeMaxInHrs,
    details = this.details.map { it.toPresentation() }
)

fun ServiceDetailDto.toPresentation(): ServiceDetailPresentation =
    ServiceDetailPresentation(
        key = this.key,
        value = this.value
    )

// PricingMetadata mapping
fun PricingMetadataDto.toPresentation(): PricingMetadataPresentation = when (this) {
    is PricingMetadataDto.SubItemsPricingDTO -> PricingMetadataPresentation.SubItemsPricingPresentation(
        startingPrice = this.startingPrice
    )
    is PricingMetadataDto.ServicePricingDTO -> PricingMetadataPresentation.ServicePricingPresentation(
        pricePerUnit = this.pricePerUnit,
        unit = this.unit,
        minimumUnits = this.minimumUnits,
        minimumPrice = this.minimumPrice
    )
}

fun GenderDto.toPresentation() : Gender = when (this) {
    GenderDto.MALE -> Gender.MALE
    GenderDto.FEMALE -> Gender.FEMALE
    GenderDto.BOTH -> Gender.BOTH
}

fun ServiceItemPresentation.toCartItemEntity(deliveryTimeMinInHrs: Int, deliveryTimeMaxInHrs: Int?) : CartItemEntity {
    return CartItemEntity(
        itemId = itemId,
        serviceId = serviceId,
        name = name,
        metadata = metadata?.toEntity(),
        itemPricing = itemPricing.toItemPricingEntity(),
        quantity = 1,
        deliveryTimeMinInHrs = deliveryTimeMinInHrs,
        deliveryTimeMaxInHrs = deliveryTimeMaxInHrs
    )
}

fun ServiceItemMetadata.toEntity() : ItemMetadataEntity {
    return ItemMetadataEntity(
        imageUrl = imageUrl,
        gender = when(gender) {
            Gender.MALE -> GenderEntity.MALE
            Gender.FEMALE -> GenderEntity.FEMALE
            Gender.BOTH -> GenderEntity.BOTH
            null -> null
        }
    )
}

fun ItemPricing.toItemPricingEntity() : ItemPricingEntity {
    return when(this) {
        is ItemPricing.SubItemRangedPricingPresentation -> ItemPricingEntity(
            pricingType = PricingTypeEntity.RANGED,
            rangedMinPrice = minPrice,
            rangedMaxPrice = maxPrice,
        )

        is ItemPricing.ServiceItemPricingPresentation -> ItemPricingEntity(
            pricingType = PricingTypeEntity.SERVICE,
            servicePricePerUnit = pricePerUnit,
            serviceUnit = unit,
            serviceMinimumUnits = minimumUnits,
            serviceMinimumPrice = minimumPrice,
        )
        is ItemPricing.SubItemFixedPricingPresentation -> ItemPricingEntity(
            pricingType = PricingTypeEntity.FIXED,
            fixedPrice = fixedPrice,
        )
    }
}


