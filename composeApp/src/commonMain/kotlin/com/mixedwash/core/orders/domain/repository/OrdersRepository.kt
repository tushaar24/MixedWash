package com.mixedwash.core.orders.domain.repository

import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.core.orders.domain.model.Order

interface OrdersRepository {
    suspend fun setOrderDraft(
        userId: String,
        bookingsData: List<BookingData>,
        offer: String? = null,
        deliveryNotes: String,
        address: Address
    ): Result<Order>
    suspend fun getOrderDraft() : Result<Order>
    suspend fun clearOrderDraft() :Result<Order?>
    suspend fun placeDraftOrder(): Result<Order>
    suspend fun getOrders(): Result<List<Order>>
    suspend fun getOrderById(id: String): Result<Order>
}