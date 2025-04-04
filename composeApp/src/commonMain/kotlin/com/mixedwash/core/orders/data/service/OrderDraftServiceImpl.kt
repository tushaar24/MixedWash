package com.mixedwash.core.orders.data.service

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.core.orders.domain.model.BookingState
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.error.OrderException
import com.mixedwash.core.orders.domain.service.OrderDraftService
import com.mixedwash.features.address.domain.model.Address
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class OrderDraftServiceImpl : OrderDraftService {

    private var orderDraft: Order? = null
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
                    id = Uuid.random().toHexString(),
                    bookings = bookings,
                    customerId = userId,
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

    override suspend fun getCurrentDraft(): Order? {
        return orderDraft
    }
}