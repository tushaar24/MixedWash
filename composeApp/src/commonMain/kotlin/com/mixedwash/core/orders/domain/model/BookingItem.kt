package com.mixedwash.core.orders.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingItem(
    @SerialName("item_id")
    val itemId: String,
    @SerialName("name")
    val name: String,
    @SerialName("service_name")
    val serviceName: String,
    @SerialName("quantity")
    val quantity: Int,
    @SerialName("item_pricing")
    val itemPricing: BookingItemPricing,
    @SerialName("service_id")
    val serviceId: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("created_millis")
    val createdMillis: Long,
    @SerialName("final_price")
    val finalPrice: Int? = null,
    @SerialName("final_quantity")
    val finalQuantity: Int? = null
)

fun Booking.calculateTotalPrice(): Int {
    return bookingItems.sumOf { it.calculateItemPrice() }
}

fun BookingItem.calculateItemPrice(): Int {
    return when (itemPricing) {
        is BookingItemPricing.SubItemRangedPricing -> itemPricing.maxPrice.div(100) * quantity
        is BookingItemPricing.SubItemFixedPricing -> itemPricing.fixedPrice.div(100) * quantity
        is BookingItemPricing.ServiceItemPricing -> {
            val basePrice = itemPricing.pricePerUnit.div(100) * itemPricing.minimumUnits
            if (basePrice > itemPricing.minimumPrice) {
                basePrice
            } else {
                itemPricing.minimumPrice.div(100)
            }
        }
    }
}
