package com.mixedwash.features.order_details.presentation

import com.mixedwash.core.orders.domain.model.Order

data class OrderDetailsScreenState(
    val order: Order? = null,
    val serviceImageUrls: Map<String, String> = emptyMap(),
)

sealed class OrderDetailsScreenEvent {
}