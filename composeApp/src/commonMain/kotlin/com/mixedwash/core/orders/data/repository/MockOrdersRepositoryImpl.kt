package com.mixedwash.core.orders.data.repository

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.core.orders.domain.model.BookingState
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.error.OrderException
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.orders.domain.service.OrderDraftService
import com.mixedwash.features.address.domain.model.Address
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock

class MockOrdersRepositoryImpl(
    private val orderDraftService: OrderDraftService
) : OrdersRepository {

    private val userOrders: MutableList<Order> = mutableListOf()
    private val mutex = Mutex()

    override suspend fun setOrderDraft(
        userId: String,
        bookingsData: List<BookingData>,
        offer: String?,
        deliveryNotes: String,
        address: Address
    ): Result<Order> = orderDraftService.setOrderDraft(
            userId = userId,
            bookingsData = bookingsData,
            offer = offer,
            deliveryNotes = deliveryNotes,
            address = address
        )


    override suspend fun getOrderDraft(): Result<Order> = orderDraftService.getOrderDraft()


    override suspend fun clearOrderDraft(): Result<Order?> =  orderDraftService.clearOrderDraft()


    override suspend fun getOrders(): Result<List<Order>> {
        return Result.success(userOrders)
    }

    override suspend fun getOrderById(id: String): Result<Order> {
        return userOrders.find { it.id == id }?.let {
            Result.success(it)
        } ?: Result.failure(OrderException.OrderNotFound)
    }

    override suspend fun placeDraftOrder(): Result<Order> {
        return mutex.withLock {
            runCatching {
                val draftOrder = orderDraftService.getCurrentDraft()
                    ?: throw OrderException.OrderNotFound

                userOrders.add(draftOrder)

                // Clear the draft after successful placement
                orderDraftService.clearOrderDraft()

                draftOrder
            }
        }
    }
}