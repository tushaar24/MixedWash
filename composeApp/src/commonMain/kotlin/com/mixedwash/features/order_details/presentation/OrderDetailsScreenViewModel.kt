package com.mixedwash.features.order_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.core.domain.config.AppConfig
import com.mixedwash.core.feature.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.services.domain.ServicesDataRepository
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
        loadOrderDetails()
    }

    fun onEvent(event: OrderDetailsScreenEvent) {

    }

    private fun loadOrderDetails() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    order = ordersRepository.getOrderById(orderId).getOrNull(),
                    serviceImageUrls = servicesDataRepository.getAllServices().getOrNull()?.services?.associate { o ->
                        o.serviceId to o.imageUrl
                    } ?: emptyMap(),
                    stagingEnabled = appConfig.useStagingOrdersService
                )
            }
        }
    }
}