package com.mixedwash.features.history.domain

import com.mixedwash.features.history.domain.model.OrderHistoryDTO

interface HistoryRepository {
    suspend fun getAllOrders(): Result<List<OrderHistoryDTO>>
}