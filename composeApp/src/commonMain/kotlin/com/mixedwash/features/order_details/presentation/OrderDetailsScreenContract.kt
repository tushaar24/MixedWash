package com.mixedwash.features.order_details.presentation

import com.mixedwash.core.feature.orders.domain.model.Order

data class OrderDetailsScreenState(
    val order: Order? = null,
    val serviceImageUrls: Map<String, String> = emptyMap(),
    val stagingEnabled: Boolean = false,
    val stagingOperations: List<StagingOperation> = emptyList(),
    val isRefreshing: Boolean = false,
)

data class StagingOperation(
    val operationName: String,
    val callback: (String, String) -> Unit,
)

sealed class OrderDetailsScreenEvent {
    data object Refresh: OrderDetailsScreenEvent()
}