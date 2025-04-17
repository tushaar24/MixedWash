package com.mixedwash.features.order_review.presentation

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

class OrderReviewScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val localCartRepository: LocalCartRepository,
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    val route = savedStateHandle.toRoute<Route.OrderReviewRoute>()

    private val _uiEventsChannel = Channel<OrderReviewScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    private val _state: MutableStateFlow<OrderReviewScreenState> = MutableStateFlow(
        OrderReviewScreenState(
            bookings = emptyList(),
            deliveryAddress = null,
            title = "",
        )
    )

    val state: StateFlow<OrderReviewScreenState> = _state.asStateFlow().onStart {
        viewModelScope.launch {
            when (route.destinationType) {
                Route.OrderReviewRoute.DestinationType.CONFIRM_DRAFT_ORDER -> ordersRepository.getOrderDraft()
                Route.OrderReviewRoute.DestinationType.VIEW_ORDER_BY_BOOKING_ID -> {
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
                        title = if (route.destinationType == Route.OrderReviewRoute.DestinationType.CONFIRM_DRAFT_ORDER) "Review Your Booking" else "Order  #" + route.bookingId!!.takeLast(6),
                        screenType = if (route.destinationType == Route.OrderReviewRoute.DestinationType.CONFIRM_DRAFT_ORDER) OrderReviewScreenType.CONFIRMATION else OrderReviewScreenType.ORDER_DETAILS,
                        note = if (route.destinationType == Route.OrderReviewRoute.DestinationType.CONFIRM_DRAFT_ORDER) "*final order cost will be calculated based on the exact contents of your order confirmed after processing." else null
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
        initialValue = OrderReviewScreenState(
            bookings = emptyList(),
            deliveryAddress = null,
            title = "",
        )
    )

    fun onEvent(event: OrderReviewScreenEvent) {
        when (event) {
            OrderReviewScreenEvent.OnPlaceOrder -> {
                viewModelScope.launch {
                    ordersRepository.placeDraftOrder().onSuccess {
                        localCartRepository.clearCartItems().onFailure { e ->
                            //snackbarEvent("Error clearing cart", type = SnackBarType.ERROR)
                            Logger.e("SlotSelectionScreenViewModel", "Error clearing cart")
                            e.printStackTrace()
                        }
                        sendEvent(OrderReviewScreenUiEvent.NavigateToOrderConfirmation(it.bookings.first().id))
                    }.onFailure {
                        showSnackbar(SnackbarPayload("Error Placing Booking", SnackBarType.ERROR))
                        Logger.e(TAG, it.message ?: "Error Placing Booking")
                    }
                }
            }
        }
    }

    private fun showSnackbar(snackbarPayload: SnackbarPayload) {
        sendEvent(OrderReviewScreenUiEvent.ShowSnackbar(snackbarPayload))
    }

    private fun sendEvent(event: OrderReviewScreenUiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(event)
        }
    }
}