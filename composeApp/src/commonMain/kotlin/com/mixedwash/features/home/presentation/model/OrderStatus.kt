package com.mixedwash.features.home.presentation.model

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.features.home.data.models.OrderStatusDto

// OrderStatus.kt
data class OrderStatus(
    val orderId: String,
    val title: String,
    val subtitle: String,
    val description: String
)

// Converter from OrderStatusDto to OrderStatus
fun OrderStatusDto.toPresentation(): OrderStatus = OrderStatus(
    orderId = orderId,
    title = title,
    subtitle = subtitle,
    description = description
)

fun Booking.toOrderStatus(): OrderStatus = OrderStatus(
    orderId = this.id,
    title = this.bookingItems.first().name,
    subtitle = "",
    description = "",
)
