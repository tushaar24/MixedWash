package com.mixedwash.features.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.mixedwash.features.common.data.service.local.DummyData
import com.mixedwash.features.history.data.HistoryService
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class OrderHistoryScreenViewModel(private val historyService: HistoryService) : ViewModel() {

    private val initialState = OrderHistoryState(
        orders = emptyList(),
        insights = DummyData.insightMetrics
    )

    private var _uiState = MutableStateFlow<OrderHistoryState>(initialState)
    val uiState: StateFlow<OrderHistoryState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    fun onEvent(orderHistoryEvent: OrderHistoryEvent) {

    }

    private fun loadInitialData() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                orders = historyService.getAllOrders().getOrNull() ?: emptyList()
            )
        }
    }
}