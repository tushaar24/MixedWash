package com.mixedwash.features.order_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "OrderDetailsScreenViewModel"

class OrderDetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val localCartRepository: LocalCartRepository,
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    val route = savedStateHandle.toRoute<Route.OrderDetailsRoute>()

    private val _uiEventsChannel = Channel<OrderDetailsScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    private val _state: MutableStateFlow<OrderDetailsScreenState> = MutableStateFlow(
        OrderDetailsScreenState(
            bookings = emptyList(),
            deliveryAddress = null,
            title = "",
        )
    )

    val state: StateFlow<OrderDetailsScreenState> = _state.asStateFlow().onStart {
        viewModelScope.launch {
            when (route.destinationType) {
                Route.OrderDetailsRoute.DestinationType.CONFIRM_DRAFT_ORDER -> ordersRepository.getOrderDraft()
                Route.OrderDetailsRoute.DestinationType.VIEW_ORDER_BY_BOOKING_ID -> {
                    if (route.bookingId == null) {
                        showSnackbar(SnackbarPayload("No Booking Id Provided", SnackBarType.ERROR))
                        return@launch
                    }
                    ordersRepository.getOrderByBookingId(route.bookingId)
                }
            }.onSuccess { order ->
                _state.update {
                    it.copy(
                        bookings = order.bookings,
                        deliveryAddress = order.address,
                        title = if (route.destinationType == Route.OrderDetailsRoute.DestinationType.CONFIRM_DRAFT_ORDER) "Review Your Booking" else "Order  #" + route.bookingId!!.takeLast(6),
                        screenType = if (route.destinationType == Route.OrderDetailsRoute.DestinationType.CONFIRM_DRAFT_ORDER) OrderDetailsScreenType.CONFIRMATION else OrderDetailsScreenType.ORDER_DETAILS,
                        note = if (route.destinationType == Route.OrderDetailsRoute.DestinationType.CONFIRM_DRAFT_ORDER) "*final order cost will be calculated based on the exact contents of your order confirmed after processing." else null
                    )
                }
            }.onFailure {
                showSnackbar(SnackbarPayload("Error Loading Booking", SnackBarType.ERROR))
                Logger.e(TAG, it.message ?: "Error Loading Booking")
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = OrderDetailsScreenState(
            bookings = emptyList(),
            deliveryAddress = null,
            title = "",
        )
    )

    fun onEvent(event: OrderDetailsScreenEvent) {
        when (event) {
            OrderDetailsScreenEvent.OnPlaceOrder -> {
                viewModelScope.launch {
                    ordersRepository.placeDraftOrder().onSuccess {
                        localCartRepository.clearCartItems().onFailure { e ->
                            //snackbarEvent("Error clearing cart", type = SnackBarType.ERROR)
                            Logger.e("SlotSelectionScreenViewModel", "Error clearing cart")
                            e.printStackTrace()
                        }
                        sendEvent(OrderDetailsScreenUiEvent.NavigateToOrderConfirmation(it.id))
                    }.onFailure {
                        showSnackbar(SnackbarPayload("Error Placing Booking", SnackBarType.ERROR))
                        Logger.e(TAG, it.message ?: "Error Placing Booking")
                    }
                }
            }
        }
    }

    private fun showSnackbar(snackbarPayload: SnackbarPayload) {
        sendEvent(OrderDetailsScreenUiEvent.ShowSnackbar(snackbarPayload))
    }

    private fun sendEvent(event: OrderDetailsScreenUiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(event)
        }
    }
}