package com.mixedwash.features.order_confirmation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OrderConfirmationScreenViewModel : ViewModel() {
    private val initialState = OrderConfirmationScreenState(
        title = "Order Placed \uD83C\uDF89",
        description = "Thank you for placing an order with us. You will receive an email confirmation shortly.",
    )
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<OrderConfirmationScreenState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<OrderConfirmationScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    fun onEvent(event: OrderConfirmationScreenEvent) {
        when (event) {
            OrderConfirmationScreenEvent.OnBackHome -> {
                viewModelScope.launch {
                    _uiEventsChannel.send(OrderConfirmationScreenUiEvent.Navigate(Route.HomeRoute))
                }
            }
            OrderConfirmationScreenEvent.OnCheckOrderStatus -> { }
            OrderConfirmationScreenEvent.OnContactUs -> { }
        }
    }
}