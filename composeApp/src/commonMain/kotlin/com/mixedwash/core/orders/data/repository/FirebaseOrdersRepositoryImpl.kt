package com.mixedwash.core.orders.data.repository

import com.mixedwash.core.orders.data.service.OrderService
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

class FirebaseOrdersRepositoryImpl(
    private val orderDraftService: OrderDraftService,
    private val orderService: OrderService
) : OrdersRepository {

    private val mutex = Mutex()

    override val isStagingEnabled: Boolean
        get() = orderService.useStagingCollection

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
        return runCatching {
            orderService.getAllOrdersMostRecentFirst().getOrThrow()
        }
    }

    override suspend fun getOrderById(id: String): Result<Order> {
        return orderService.getOrderById(id)
    }

    override suspend fun getOrderByBookingId(bookingId: String): Result<Order> {
        return runCatching {
            val orders = orderService.getAllOrdersMostRecentFirst().getOrThrow()
            val order =
                orders.firstOrNull { it.bookings.any { booking -> booking.id == bookingId } }
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
        return runCatching {
            val draftOrder =
                orderDraftService.getCurrentDraft() ?: throw OrderException.OrderNotFound

            // Save the order to Firebase
            orderService.placeOrder(draftOrder.copy(createdAtSeconds = Clock.System.now().epochSeconds))
                .getOrThrow()

            // Clear the draft after successful placement
            orderDraftService.clearOrderDraft()

            draftOrder
        }
    }

    /**
     * Helper function that executes the given operation only when useStagingCollection is true.
     * Otherwise, it returns a failure with IllegalStagingOperationException.
     *
     * @param block The operation to execute when useStagingCollection is true
     * @return Result of the operation or IllegalStagingOperationException if useStagingCollection is false
     */
    private suspend fun <T> ifStaging(block: suspend () -> Result<T>): Result<T> {
        return mutex.withLock {
            if (!isStagingEnabled) {
                return@withLock Result.failure(OrderException.IllegalStagingOperationException)
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
        return ifStaging {
            orderService.deleteOrder(orderId)
        }
    }

    override suspend fun setOrderOutForPickup(orderId: String): Result<Order> {
        return ifStaging {
            orderService.updateOrder(orderId) { order ->
                order.copy(outForPickupSeconds = Clock.System.now().epochSeconds)
            }
        }
    }

    override suspend fun setOrderPickedUp(orderId: String): Result<Order> {
        return ifStaging {
            orderService.updateOrder(orderId) { order ->
                order.copy(pickedUpSeconds = Clock.System.now().epochSeconds)
            }
        }
    }

    override suspend fun setBookingOutForDelivery(orderId: String, bookingId: String): Result<Order> {
        return updateBooking(orderId, bookingId) { booking ->
            booking.copy(outForDeliverySeconds = Clock.System.now().epochSeconds)
        }
    }

    override suspend fun setBookingDelivered(orderId: String, bookingId: String): Result<Order> {
        return updateBooking(orderId, bookingId) { booking ->
            booking.copy(deliveredSeconds = Clock.System.now().epochSeconds)
        }
    }

    override suspend fun setBookingPaid(
        orderId: String,
        bookingId: String,
        isPaid: Boolean
    ): Result<Order> {
        return updateBooking(orderId, bookingId) { booking ->
            booking.copy(isPaid = isPaid)
        }
    }

    /**
     * Helper function to update a specific booking within an order.
     *
     * @param orderId ID of the order containing the booking
     * @param bookingId ID of the booking to update
     * @param update Function that receives a booking and returns an updated version
     * @return Result with the updated Order or failure
     */
    private suspend fun updateBooking(
        orderId: String,
        bookingId: String,
        update: (booking: com.mixedwash.core.orders.domain.model.Booking) -> com.mixedwash.core.orders.domain.model.Booking
    ): Result<Order> {
        return ifStaging {
            orderService.updateBooking(orderId, bookingId, update)
        }
    }

    override suspend fun clearAllOrders(): Result<Unit> {
        return ifStaging {
            orderService.clearAllOrders()
        }
    }

    override suspend fun fetchActiveBookings(): Result<List<Pair<String, Booking>>> {
        return orderService.fetchActiveOrders()
    }
}