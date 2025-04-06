package com.mixedwash.core.orders.data.repository

import com.mixedwash.core.orders.data.service.OrderService
import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.error.OrderException
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.orders.domain.service.OrderDraftService
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.home.presentation.model.OrderStatus
import com.mixedwash.features.home.presentation.model.toOrderStatus
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Clock

class FirebaseOrdersRepositoryImpl(
    private val orderDraftService: OrderDraftService,
    private val orderService: OrderService
) : OrdersRepository {

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

    override suspend fun clearOrderDraft(): Result<Order?> = orderDraftService.clearOrderDraft()

    override suspend fun getOrders(): Result<List<Order>> {
        return runCatching {
            orderService.getOrders().getOrThrow()
        }
    }

    override suspend fun getOrderById(id: String): Result<Order> {
        return orderService.getOrderById(id)
    }

    override suspend fun placeDraftOrder(): Result<Order> {
        return runCatching {
            val draftOrder = orderDraftService.getCurrentDraft()
                ?: throw OrderException.OrderNotFound


            // Save the order to Firebase
            orderService.saveOrder(draftOrder.copy(createdAtSeconds = Clock.System.now().epochSeconds))
                .getOrThrow()

            // Clear the draft after successful placement
            orderDraftService.clearOrderDraft()

            draftOrder
        }
    }

    override suspend fun getOrderStatus(): Result<List<OrderStatus>> {
        val orders = orderService.getOrders().getOrNull()
            ?: return Result.failure(Exception("failed to fetch"))

        return Result.success(orders.flatMap { order ->
            order.bookings.map {
                it.toOrderStatus()
            }
        })
    }
}