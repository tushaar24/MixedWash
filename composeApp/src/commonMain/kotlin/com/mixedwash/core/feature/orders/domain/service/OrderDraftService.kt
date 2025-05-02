package com.mixedwash.core.feature.orders.domain.service

import com.mixedwash.core.feature.orders.domain.model.BookingData
import com.mixedwash.core.feature.orders.domain.model.Order
import com.mixedwash.features.address.domain.model.Address

interface OrderDraftService {
    suspend fun setOrderDraft(
        bookingsData: List<BookingData>,
        offer: String? = null,
        deliveryNotes: String,
        address: Address
    ): Result<Order>

    suspend fun getOrderDraft(): Result<Order>

    suspend fun clearOrderDraft(): Result<Order?>

    suspend fun getCurrentDraft(): Order?
}