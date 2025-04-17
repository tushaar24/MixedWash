package com.mixedwash.features.order_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.core.presentation.util.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDetailsScreenViewModel(
    private val ordersRepository: OrdersRepository,
    savedStateHandle: SavedStateHandle
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
            val x = async { ordersRepository.getOrderByBookingId(orderId) }
            Logger.d("fcuk", x.await().getOrNull().toString())
            _state.update {
                it.copy(order = ordersRepository.getOrderByBookingId(orderId).getOrNull())
            }
        }
    }
}