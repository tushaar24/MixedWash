package com.mixedwash.core.booking.domain.model

import com.mixedwash.features.local_cart.domain.model.CartItem
import com.mixedwash.features.services.presentation.model.ItemPricing
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot
import kotlinx.datetime.Clock

fun TimeSlot.toBookingTimeSlot() = BookingTimeSlot(
    id = id,
    startTimeStamp = startTimeStamp,
    endTimeStamp = endTimeStamp
)

fun CartItem.toBookingItem(
    createdMillis: Long = Clock.System.now().toEpochMilliseconds()
): BookingItem {
    return BookingItem(
        serviceId = serviceId,
        name = name,
        itemPricing = itemPricing.toBookingItemPricing(),
        itemId = itemId,
        quantity = quantity,
        createdMillis = createdMillis,
        imageUrl = metadata?.imageUrl,
        serviceName = serviceName
    )
}

fun ItemPricing.toBookingItemPricing(): BookingItemPricing {
    return when (this) {
        is ItemPricing.SubItemRangedPricingPresentation -> BookingItemPricing.SubItemRangedPricing(
            minPrice = minPrice,
            maxPrice = maxPrice
        )

        is ItemPricing.SubItemFixedPricingPresentation -> BookingItemPricing.SubItemFixedPricing(
            fixedPrice = fixedPrice
        )

        is ItemPricing.ServiceItemPricingPresentation -> BookingItemPricing.ServiceItemPricing(
            pricePerUnit = pricePerUnit,
            unit = unit,
            minimumUnits = minimumUnits,
            minimumPrice = minimumPrice
        )
    }
}