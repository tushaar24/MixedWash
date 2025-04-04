package com.mixedwash.core.orders.data.repository

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.core.orders.domain.model.BookingState
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.error.OrderException
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.features.address.domain.model.Address
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MockOrdersRepositoryImpl : OrdersRepository {

    private var orderDraft: Order? = null
    private val userOrders: MutableList<Order> = mutableListOf()
    private val mutex = Mutex()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun setOrderDraft(
        userId: String,
        bookingsData: List<BookingData>,
        offer: String?,
        deliveryNotes: String,
        address: Address
    ): Result<Order> {
        return mutex.withLock {
            runCatching {
                val bookings = bookingsData.map {
                    Booking(
                        id = Uuid.random().toHexString(),
                        pickupSlotSelected = it.pickupSlotSelected,
                        dropSlotSelected = it.dropSlotSelected,
                        bookingItems = it.bookingItems,
                        state = BookingState.Draft,
                        stateHistory = emptyList()
                    )
                }
                orderDraft = Order(
                    id = Uuid.random().toString(),
                    bookings = bookings,
                    customerId = userId, // This should be properly implemented in production
                    deliveryNotes = deliveryNotes,
                    address = address,
                    notes = null,
                    offers = emptyList(),
                )
                orderDraft!!
            }
        }
    }

    override suspend fun getOrderDraft(): Result<Order> {
        return orderDraft?.let {
            Result.success(it)
        } ?: Result.failure(OrderException.OrderNotFound)
    }

    override suspend fun clearOrderDraft(): Result<Order?> {
        return mutex.withLock {
            runCatching {
                val order = orderDraft
                orderDraft = null
                order
            }
        }
    }

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
                orderDraft?.let { draftOrder ->
                    val updatedBookings = draftOrder.bookings.map { booking ->
                        booking.copy(
                            state = BookingState.Initiated(
                                placedAtMillis = Clock.System.now().toEpochMilliseconds()
                            )
                        )
                    }
                    val finalOrder = draftOrder.copy(bookings = updatedBookings)
                    userOrders.add(finalOrder)
                    finalOrder
                } ?: throw OrderException.OrderNotFound
            }
        }
    }

}