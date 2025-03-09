package com.mixedwash.features.history.data

import com.mixedwash.features.common.data.service.local.DummyData
import com.mixedwash.features.history.domain.HistoryRepository
import com.mixedwash.features.history.domain.model.OrderHistoryDTO

class HistoryRepositoryImpl : HistoryRepository {
    override suspend fun getAllOrders(): Result<List<OrderHistoryDTO>> {
        return Result.success(DummyData.orderHistoryMock)
    }
}