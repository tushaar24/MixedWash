package com.mixedwash.features.order_details.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.core.domain.config.AppConfig
import com.mixedwash.core.feature.orders.domain.error.onOrderError
import com.mixedwash.core.feature.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.services.domain.ServicesDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailsScreenViewModel(
    private val ordersRepository: OrdersRepository,
    private val servicesDataRepository: ServicesDataRepository,
    private val appConfig: AppConfig,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val orderId = savedStateHandle.toRoute<Route.OrderDetailsRoute>().orderId
    private val _state = MutableStateFlow(OrderDetailsScreenState())
    val state: StateFlow<OrderDetailsScreenState> = _state.asStateFlow()

    private var _uiEventsChannel = Channel<OrderDetailsScreenUiEvent>(BUFFERED)
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadOrderDetails()
        }
    }

    fun onEvent(event: OrderDetailsScreenEvent) {
        when (event) {
            OrderDetailsScreenEvent.Refresh -> {
                viewModelScope.launch {
                    _state.update { it.copy(isRefreshing = true) }
                    loadOrderDetails()
                    _state.update { it.copy(isRefreshing = false) }
                }
            }

            is OrderDetailsScreenEvent.OnCancelOrder -> {
                viewModelScope.launch { }
            }

            is OrderDetailsScreenEvent.OnDeleteOrder -> {
                execute { ordersRepository.deleteOrder(event.orderId) }
            }

            is OrderDetailsScreenEvent.OnSetDelivered -> {
                execute { ordersRepository.setBookingDelivered(event.bookingId) }
            }

            is OrderDetailsScreenEvent.OnSetOutForDelivery -> {
                execute { ordersRepository.setBookingOutForDelivery(event.bookingId) }
            }

            is OrderDetailsScreenEvent.OnSetOutForPickup -> {
                execute { ordersRepository.setOrderOutForPickup(event.orderId) }
            }

            is OrderDetailsScreenEvent.OnSetPaid -> {
                execute { ordersRepository.setBookingPaid(event.bookingId, true) }
            }

            is OrderDetailsScreenEvent.OnSetPickedUp -> {
                execute { ordersRepository.setOrderPickedUp(event.orderId) }
            }
        }
    }

    private fun execute(action: suspend () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            action()
            loadOrderDetails()
            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun loadOrderDetails() {
        val loadJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    order = ordersRepository.getOrderById(orderId).onOrderError(
                        orderNotFound = { snackbarEvent("Order not found", SnackBarType.ERROR) },
                        other = { error ->
                            snackbarEvent(error.message.orEmpty(), SnackBarType.WARNING)
                            throw error
                        }
                    ).getOrNull(),
                    serviceImageUrls = servicesDataRepository.getAllServices().onFailure { error ->
                        snackbarEvent(
                            "Failed to load service images: ${error.message}",
                            SnackBarType.WARNING
                        )
                    }.getOrNull()?.services?.associate { o ->
                        o.serviceId to o.imageUrl
                    } ?: emptyMap(),
                    stagingEnabled = appConfig.useStagingOrdersService,
                )
            }

        }

        loadJob.join()
    }

    private fun snackbarEvent(
        message: String,
        type: SnackBarType,
        duration: SnackbarDuration = SnackbarDuration.Short,
        action: (() -> Unit)? = null,
        actionText: String? = null
    ) {
        viewModelScope.launch {
            _uiEventsChannel.send(
                OrderDetailsScreenUiEvent.ShowSnackbar(
                    payload = SnackbarPayload(
                        message = message,
                        type = type,
                        duration = duration,
                        action = action,
                        actionText = actionText
                    )
                )
            )
        }
    }
}