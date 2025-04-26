package com.mixedwash.features.order_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.core.domain.config.AppConfig
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.services.domain.ServicesDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        }
    }

    private suspend fun loadOrderDetails() {
        val loadJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    order = ordersRepository.getOrderById(orderId).getOrNull(),
                    serviceImageUrls = servicesDataRepository.getAllServices()
                        .getOrNull()?.services?.associate { o ->
                            o.serviceId to o.imageUrl
                        } ?: emptyMap(),
                    stagingEnabled = appConfig.useStagingOrdersService,
                    stagingOperations = getStagingOperations()
                )
            }

        }

        loadJob.join();
    }

    private fun getStagingOperations(): List<StagingOperation> {
        return listOf(
            StagingOperation(
                operationName = "Delete Order",
                callback = { bookingId, orderId ->
                    viewModelScope.launch {
                        ordersRepository.deleteOrder(orderId)
                        loadOrderDetails()
                    }
                }
            ),

            StagingOperation(
                operationName = "Cancel Order",
                callback = { bookingId, orderId ->
                    // todo
                }
            ),

            StagingOperation(
                operationName = "Set Out For Pickup",
                callback = { bookingId, orderId ->
                    viewModelScope.launch {
                        ordersRepository.setOrderOutForPickup(orderId)
                        loadOrderDetails()
                    }
                }
            ),

            StagingOperation(
                operationName = "Set Picked Up",
                callback = { bookingId, orderId ->
                    viewModelScope.launch {
                        ordersRepository.setOrderPickedUp(orderId)
                        loadOrderDetails()
                    }
                }
            ),

            StagingOperation(
                operationName = "Set Out For Delivery",
                callback = { bookingId, orderId ->
                    viewModelScope.launch {
                        ordersRepository.setBookingOutForDelivery(bookingId)
                        loadOrderDetails()
                    }
                }
            ),

            StagingOperation(
                operationName = "Set Delivered",
                callback = { bookingId, orderId ->
                    viewModelScope.launch {
                        ordersRepository.setBookingDelivered(bookingId)
                        loadOrderDetails()
                    }
                }
            ),

            StagingOperation(
                operationName = "Set Paid",
                callback = { bookingId, orderId ->
                    viewModelScope.launch {
                        ordersRepository.setBookingPaid(bookingId, true)
                        loadOrderDetails()
                    }
                }
            )
        )
    }
}