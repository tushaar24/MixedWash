package com.mixedwash.features.history.domain.model

import com.mixedwash.features.common.presentation.services.model.ServiceItem
import org.jetbrains.compose.resources.DrawableResource

data class OrderHistoryDTO(
    val orderId: Long,
    val serviceItems: List<ServiceItem>,
    val orderedTimestamp: Long,
    val deliveryTimestamp: Long?,   // null if the order was cancelled
    val orderDeliveryStatus: OrderDeliveryStatus,
    val price: Int,
)

data class InsightMetricDTO(
    val metric: String,
    val unit: String,
    val icon: DrawableResource
)
