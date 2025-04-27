package com.mixedwash.features.order_details.presentation

import com.mixedwash.core.feature.orders.domain.model.Order

data class OrderDetailsScreenState(
    val order: Order? = null,
    val serviceImageUrls: Map<String, String> = emptyMap(),
    val stagingEnabled: Boolean = false
)

sealed class OrderDetailsScreenEvent {
}