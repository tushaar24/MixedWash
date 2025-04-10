package com.mixedwash.features.order_confirmation.presentation

import com.mixedwash.core.presentation.navigation.NavArgs

data class OrderConfirmationScreenState(
    val title: String,
    val description: String,
)

sealed interface OrderConfirmationScreenEvent {
    data object OnBackHome : OrderConfirmationScreenEvent
    data object OnOrderStatusClicked : OrderConfirmationScreenEvent
    data object OnSupportClicked : OrderConfirmationScreenEvent
}

sealed interface OrderConfirmationScreenUiEvent {
    data class Navigate(val navArgs: NavArgs) : OrderConfirmationScreenUiEvent
}