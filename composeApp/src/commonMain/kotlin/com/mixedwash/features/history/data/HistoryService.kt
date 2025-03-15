package com.mixedwash.features.history.data

import com.mixedwash.features.history.domain.HistoryRepository
import com.mixedwash.features.history.domain.model.OrderHistoryDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class HistoryService(private val historyRepository: HistoryRepository) {
    suspend fun getAllOrders(): Result<List<OrderHistoryDTO>> =
        withContext(Dispatchers.IO) {
            historyRepository.getAllOrders()
        }
}