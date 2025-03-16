package com.mixedwash.core.booking.domain.model

data class BookingItem(
    val itemId: String,
    val name: String,
    val quantity: Int,
    val itemPricing: BookingItemPricing,
    val serviceId: String,
    val imageUrl :String?=null,
    val createdMillis: Long
)



