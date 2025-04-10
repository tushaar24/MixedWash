package com.mixedwash.features.order_confirmation.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.core.presentation.navigation.NavArgType
import com.mixedwash.core.presentation.navigation.NavArgs
import com.mixedwash.core.presentation.navigation.PopUpOption
import com.mixedwash.core.presentation.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OrderConfirmationScreenViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<Route.OrderConfirmationRoute>()
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
                sendUiEvent(
                    OrderConfirmationScreenUiEvent.Navigate(
                        NavArgs(
                            navType = NavArgType.Navigate(
                                Route.HomeRoute,
                                popUpOption = PopUpOption.PopToRoute(Route.HomeRoute, false),
                                launchSingleTop = true
                            )
                        )
                    )
                )
            }

            OrderConfirmationScreenEvent.OnOrderStatusClicked -> {
                sendUiEvent(
                    OrderConfirmationScreenUiEvent.Navigate(
                        NavArgs(
                            navType = NavArgType.Navigate(
                                Route.OrderDetailsRoute(
                                    bookingId = route.bookingId,
                                    destinationType = Route.OrderDetailsRoute.DestinationType.VIEW_ORDER_BY_BOOKING_ID
                                ),
                                popUpOption = PopUpOption.PopToRoute(Route.HomeRoute, false),
                                launchSingleTop = true
                            )
                        )
                    )
                )
            }

            OrderConfirmationScreenEvent.OnSupportClicked -> {
                sendUiEvent(
                    OrderConfirmationScreenUiEvent.Navigate(
                        NavArgs(
                            navType = NavArgType.Navigate(
                                Route.FaqRoute,
                                popUpOption = PopUpOption.PopToRoute(Route.HomeRoute, false),
                                launchSingleTop = true
                            )
                        )
                    )
                )
            }
        }
    }

    private fun sendUiEvent(event: OrderConfirmationScreenUiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(event)
        }
    }
}