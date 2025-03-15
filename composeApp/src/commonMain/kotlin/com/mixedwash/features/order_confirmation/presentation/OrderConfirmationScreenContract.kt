package com.mixedwash.features.order_confirmation.presentation

import com.mixedwash.Route

data class OrderConfirmationScreenState(
    val title: String,
    val description: String,
)

sealed interface OrderConfirmationScreenEvent {
    data object OnBackHome : OrderConfirmationScreenEvent
    data object OnCheckOrderStatus : OrderConfirmationScreenEvent
    data object OnContactUs : OrderConfirmationScreenEvent
}

sealed interface OrderConfirmationScreenUiEvent {
    data class Navigate(val route: Route) : OrderConfirmationScreenUiEvent
}