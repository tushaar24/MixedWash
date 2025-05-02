package com.mixedwash.features.order_review.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.core.feature.crash.domain.CrashReporter
import com.mixedwash.core.feature.orders.domain.error.onOrderError
import com.mixedwash.core.feature.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.navigation.Route
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
    private val ordersRepository: OrdersRepository,
    private val crashReporter: CrashReporter
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
                        note = if (route.destinationType == Route.OrderReviewRoute.DestinationType.CONFIRM_DRAFT_ORDER) "Order cost is finalized after processing based on order contents and verified before delivery" else null
                    )
                }
            }.onFailure {
                showSnackbar(SnackbarPayload("Error Loading Booking", SnackBarType.ERROR))
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
                            crashReporter.recordException(e, "Error clearing cart")
                        }
                        sendEvent(OrderReviewScreenUiEvent.NavigateToOrderConfirmation(it.bookings.first().id))
                    }.onOrderError (
                        failedToCreateOrder = {
                            sendEvent(OrderReviewScreenUiEvent.ShowSnackbar(SnackbarPayload("Failed to create order", SnackBarType.ERROR)))
                            crashReporter.recordException(it, "Failed to create order")
                        },
                        orderDraftNotFound = {
                            sendEvent(OrderReviewScreenUiEvent.ShowSnackbar(SnackbarPayload("Order draft not found", SnackBarType.ERROR)))
                            crashReporter.recordException(it, "Order draft expected but not found")
                        }
                    )
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