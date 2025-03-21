package com.mixedwash.features.history.presentation

import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.core.booking.domain.model.BookingData
import com.mixedwash.features.history.domain.model.InsightMetric

data class OrderHistoryScreenState(
    val orders: List<BookingData>,
    val insights: List<InsightMetric>,
)

sealed interface OrderHistoryScreenEvent {
    data class OnOrderDetailsScreen(val orderId: String) : OrderHistoryScreenEvent
}

sealed interface OrderHistoryScreenUiEvent {
    data class Navigate(val route: Route) : OrderHistoryScreenUiEvent
}