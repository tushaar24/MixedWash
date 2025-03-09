package com.mixedwash.features.history.presentation

import com.mixedwash.features.history.domain.model.InsightMetricDTO
import com.mixedwash.features.history.domain.model.OrderHistoryDTO

data class OrderHistoryState(
    val orders: List<OrderHistoryDTO>,
    val insights: List<InsightMetricDTO>,
)

sealed interface OrderHistoryEvent {

}