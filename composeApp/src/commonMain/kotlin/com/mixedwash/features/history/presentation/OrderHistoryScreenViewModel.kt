package com.mixedwash.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.booking.domain.repository.BookingsRepository
import com.mixedwash.features.history.domain.model.insightMetrics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderHistoryScreenViewModel(private val bookingsRepository: BookingsRepository) :
    ViewModel() {

    private val initialState = OrderHistoryState(
        orders = emptyList(),
        insights = insightMetrics
    )

    private var _state = MutableStateFlow(initialState)
    val state: StateFlow<OrderHistoryState> = _state.asStateFlow()

    init {
        loadInitialData()
        calculateMetrics()
    }

    fun onEvent(event: OrderHistoryEvent) {
        when (event) {
            is OrderHistoryEvent.OnOrderDetails -> {}
        }
    }

    private fun loadInitialData() = viewModelScope.launch {
        _state.update {
            it.copy(
                orders = bookingsRepository.getBookings().getOrNull() ?: emptyList()
            )
        }
    }

    private fun calculateMetrics() {
        val quantityInKg = _state.value.orders.sumOf { it.bookingItems.sumOf { it.quantity } } + 90

        _state.update {
            it.copy(
                insights = insightMetrics.map { metric -> metric.copy(value = when (metric.metric) {
                    "time saved" -> calculateTimeSaved(quantityInKg)
                    "water saved" -> calculateWaterSaved(quantityInKg)
                    "washed" -> quantityInKg
                    else -> 0
                }) }
            )
        }
    }

    // getting clothes washed at a laundry service will save about 4lts of water
    // per kg of clothes washed
    private fun calculateWaterSaved(quantityInKg: Int) : Int {
        val waterSavedPerKg = 4
        return quantityInKg * waterSavedPerKg
    }

    // it takes about 2hours to wash and iron clothes per load(typically 8kgs),
    // so getting laundry done at a laundry service will save an hour per 4kgs
    // of clothes washed
    private fun calculateTimeSaved(quantityInKg: Int) : Int {
        return quantityInKg / 4
    }
}