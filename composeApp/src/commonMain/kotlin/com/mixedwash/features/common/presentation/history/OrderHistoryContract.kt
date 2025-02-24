package com.mixedwash.features.common.presentation.history

import com.mixedwash.features.common.presentation.history.model.InsightMetric
import com.mixedwash.features.common.presentation.history.model.OrderHistoryData


data class OrderHistoryState(
    val orders: List<OrderHistoryData>,
    val insights: List<InsightMetric>,
)

sealed interface OrderHistoryEvent {

}