package com.mixedwash.core.orders.data.repository

import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.BookingData
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.error.OrderException
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.orders.domain.service.OrderDraftService
import com.mixedwash.features.address.domain.model.Address
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock

class MockOrdersRepositoryImpl(
    private val orderDraftService: OrderDraftService,
    override val isStagingEnabled: Boolean = false
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

    override suspend fun clearOrderDraft(): Result<Order?> = orderDraftService.clearOrderDraft()

    override suspend fun getAllOrdersMostRecentFirst(): Result<List<Order>> {
        return Result.success(userOrders)
    }

    override suspend fun getOrderById(id: String): Result<Order> {
        return runCatching {
            userOrders.find { it.id == id } ?: throw OrderException.OrderNotFound
        }
    }

    override suspend fun getOrderByBookingId(bookingId: String): Result<Order> {
        return runCatching {
            val order = userOrders.find { it.bookings.any { booking -> booking.id == bookingId } }
                ?: throw OrderException.OrderNotFound

            // Verify that the booking exists in the order
            val bookingExists = order.bookings.any { it.id == bookingId }
            if (!bookingExists) {
                throw OrderException.BookingNotFound
            }

            order
        }
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

    /**
     * Helper function that executes the given operation only when the useStagingCollection property is true.
     * Otherwise, it returns a failure with IllegalStagingOperationException.
     *
     * @param block The operation to execute when useStagingCollection is true
     * @return Result of the operation or IllegalStagingOperationException if useStagingCollection is false
     */
    private suspend fun <T> withStagingMode(block: suspend () -> Result<T>): Result<T> {
        return mutex.withLock {
            if (!isStagingEnabled) {
                return Result.failure(OrderException.IllegalStagingOperationException)
            }

            try {
                block()
            } catch (e: OrderException) {
                // Propagate OrderException directly
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteOrder(orderId: String): Result<Unit> {
        return withStagingMode {
            runCatching {
                val index = userOrders.indexOfFirst { it.id == orderId }
                if (index == -1) {
                    throw OrderException.OrderNotFound
                }

                userOrders.removeAt(index)
                Unit
            }
        }
    }

    override suspend fun setOrderOutForPickup(orderId: String): Result<Order> {
        return withStagingMode {
            runCatching {
                val index = userOrders.indexOfFirst { it.id == orderId }
                if (index == -1) {
                    throw OrderException.OrderNotFound
                }

                val order = userOrders[index]
                val updatedOrder = order.copy(outForPickupSeconds = Clock.System.now().epochSeconds)
                userOrders[index] = updatedOrder
                updatedOrder
            }
        }
    }

    override suspend fun setOrderPickedUp(orderId: String): Result<Order> {
        return withStagingMode {
            runCatching {
                val index = userOrders.indexOfFirst { it.id == orderId }
                if (index == -1) {
                    throw OrderException.OrderNotFound
                }

                val order = userOrders[index]
                val updatedOrder = order.copy(pickedUpSeconds = Clock.System.now().epochSeconds)
                userOrders[index] = updatedOrder
                updatedOrder
            }
        }
    }

    override suspend fun setBookingOutForDelivery(
        orderId: String,
        bookingId: String
    ): Result<Order> {
        return withStagingMode {
            runCatching {
                val index = userOrders.indexOfFirst { it.id == orderId }
                if (index == -1) {
                    throw OrderException.OrderNotFound
                }

                val order = userOrders[index]
                val updatedBookings = order.bookings.map { b ->
                    if (b.id == bookingId) {
                        b.copy(outForDeliverySeconds = Clock.System.now().epochSeconds)
                    } else {
                        b
                    }
                }

                if (updatedBookings.all { it.id != bookingId }) {
                    throw OrderException.BookingNotFound
                }

                val updatedOrder = order.copy(bookings = updatedBookings)
                userOrders[index] = updatedOrder
                updatedOrder
            }
        }
    }

    override suspend fun setBookingDelivered(orderId: String, bookingId: String): Result<Order> {
        return withStagingMode {
            runCatching {
                val index = userOrders.indexOfFirst { it.id == orderId }
                if (index == -1) {
                    throw OrderException.OrderNotFound
                }

                val order = userOrders[index]
                val updatedBookings = order.bookings.map { b ->
                    if (b.id == bookingId) {
                        b.copy(deliveredSeconds = Clock.System.now().epochSeconds)
                    } else {
                        b
                    }
                }

                if (updatedBookings.all { it.id != bookingId }) {
                    throw OrderException.BookingNotFound
                }

                val updatedOrder = order.copy(bookings = updatedBookings)
                userOrders[index] = updatedOrder
                updatedOrder
            }
        }
    }

    override suspend fun setBookingPaid(
        orderId: String,
        bookingId: String,
        isPaid: Boolean
    ): Result<Order> {
        return withStagingMode {
            runCatching {
                val index = userOrders.indexOfFirst { it.id == orderId }
                if (index == -1) {
                    throw OrderException.OrderNotFound
                }

                val order = userOrders[index]
                val updatedBookings = order.bookings.map { b ->
                    if (b.id == bookingId) {
                        b.copy(isPaid = isPaid)
                    } else {
                        b
                    }
                }

                if (updatedBookings.all { it.id != bookingId }) {
                    throw OrderException.BookingNotFound
                }

                val updatedOrder = order.copy(bookings = updatedBookings)
                userOrders[index] = updatedOrder
                updatedOrder
            }
        }
    }

    override suspend fun clearAllOrders(): Result<Unit> {
        return withStagingMode {
            runCatching {
                userOrders.clear()
            }
        }
    }

    override suspend fun fetchActiveBookings(): Result<List<Pair<String, Booking>>> {
        return Result.success(
            userOrders.flatMap { order ->
                order.bookings.filter { booking ->
                    booking.deliveredSeconds == null
                }.map { booking ->
                    Pair(order.id, booking)
                }
            }
        )
    }
}