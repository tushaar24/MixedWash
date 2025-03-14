package com.mixedwash.features.history.data

import com.mixedwash.features.history.domain.HistoryRepository
import com.mixedwash.features.history.domain.model.OrderHistoryDTO

class HistoryRepositoryImpl : HistoryRepository {
    override suspend fun getAllOrders(): Result<List<OrderHistoryDTO>> {
        return Result.success(emptyList())  // todo: send parsed json data
    }
}