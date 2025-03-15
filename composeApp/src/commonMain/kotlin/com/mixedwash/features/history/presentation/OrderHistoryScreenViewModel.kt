package com.mixedwash.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.features.history.data.HistoryService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderHistoryScreenViewModel(private val historyService: HistoryService) : ViewModel() {

    private val initialState = OrderHistoryState(
        orders = emptyList(),
        insights = emptyList()
    )

    private var _state = MutableStateFlow<OrderHistoryState>(initialState)
    val state: StateFlow<OrderHistoryState> = _state.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: OrderHistoryEvent) {
        when (event) {
            is OrderHistoryEvent.OnOrderDetails -> {}
        }
    }

    private fun loadInitialData() = viewModelScope.launch {
        _state.update {
            it.copy(
                orders = historyService.getAllOrders().getOrNull() ?: emptyList()
            )
        }
    }
}