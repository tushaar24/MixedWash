package com.mixedwash.features.history.presentation

import com.mixedwash.core.booking.domain.model.BookingData
import com.mixedwash.features.history.domain.model.InsightMetric

data class OrderHistoryState(
    val orders: List<BookingData>,
    val insights: List<InsightMetric>,
)

sealed interface OrderHistoryEvent {
    data class OnOrderDetails(val orderId: String) : OrderHistoryEvent

}