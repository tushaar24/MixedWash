package com.mixedwash.core.orders.data.service

import com.mixedwash.core.data.UserService
import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.orders.domain.model.error.OrderException
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

const val ORDERS_COLLECTION = "ORDERS"
const val STAGING_ORDERS_COLLECTION = "STAGING_ORDERS"
const val BOOKINGS_SUBCOLLECTION = "BOOKINGS"

interface OrderService {
    val useStagingCollection: Boolean
    suspend fun placeOrder(order: Order): Result<Order>
    suspend fun getOrderById(orderId: String): Result<Order>
    suspend fun getAllOrdersMostRecentFirst(): Result<List<Order>>
    suspend fun updateOrder(orderId: String, update: (Order) -> Order): Result<Order>
    suspend fun deleteOrder(orderId: String): Result<Unit>
    suspend fun clearAllOrders(): Result<Unit>
    suspend fun updateBooking(
        orderId: String,
        bookingId: String,
        update: (Booking) -> Booking
    ): Result<Order>

    /**
     * Returns a list of active bookings along with their order ids.
     */
    suspend fun fetchActiveOrders(): Result<List<Pair<String, Booking>>>
}

class FirebaseOrderService(
    private val userService: UserService,
    override val useStagingCollection: Boolean = false,
) : OrderService {

    private val db = Firebase.firestore
    private val orderMutex = Mutex()
    private val orderCollection =
        if (useStagingCollection) STAGING_ORDERS_COLLECTION else ORDERS_COLLECTION

    override suspend fun placeOrder(order: Order): Result<Order> {
        return orderMutex.withLock {
            runCatching {
                // Create order document without bookings
                val orderWithoutBookings = order.copy(bookings = emptyList())
                val orderRef = db.collection(orderCollection).document(order.id)

                // Save the order document
                orderRef.set(orderWithoutBookings)

                // Save each booking as a document in the bookings subcollection
                order.bookings.forEach { booking ->
                    orderRef.collection(BOOKINGS_SUBCOLLECTION)
                        .document(booking.id)
                        .set(booking)
                }

                order // Return the original order object with bookings
            }
        }
    }

    override suspend fun getOrderById(orderId: String): Result<Order> {
        return runCatching {
            coroutineScope {
                // Get the order document and bookings in parallel
                val orderDeferred = async {
                    val orderSnapshot = db.collection(orderCollection)
                        .document(orderId)
                        .get()

                    if (!orderSnapshot.exists) {
                        throw OrderException.OrderNotFound
                    }

                    orderSnapshot.data<Order>()
                }

                val bookingsDeferred = async {
                    val bookingsSnapshot = db.collection(orderCollection)
                        .document(orderId)
                        .collection(BOOKINGS_SUBCOLLECTION)
                        .get()

                    bookingsSnapshot.documents.map { doc ->
                        doc.data<Booking>()
                    }
                }

                // Wait for both operations to complete
                val orderWithoutBookings = orderDeferred.await()
                val bookings = bookingsDeferred.await()

                // Return the order with bookings
                orderWithoutBookings.copy(bookings = bookings)
            }
        }
    }

    override suspend fun getAllOrdersMostRecentFirst(): Result<List<Order>> {
        return runCatching {
            val userId = userService.currentUser?.uid
                ?: throw IllegalStateException("Current user has no id")

            coroutineScope {
                val ordersSnapshot = db.collection(orderCollection)
                    .where { "customer_id" equalTo userId }
                    .get()

                val orderDeferreds = ordersSnapshot.documents.map { doc ->
                    val order = doc.data<Order>()

                    val bookingsDeferred = async {
                        val bookingsSnapshot = db.collection(orderCollection)
                            .document(order.id)
                            .collection(BOOKINGS_SUBCOLLECTION)
                            .get()

                        bookingsSnapshot.documents.map { bookingDoc ->
                            bookingDoc.data<Booking>()
                        }
                    }

                    async {
                        val bookings = bookingsDeferred.await()
                        order.copy(bookings = bookings)
                    }
                }

                // Wait for all orders to be processed
                val ordersWithBookings = orderDeferreds.map { it.await() }
                ordersWithBookings.sortedByDescending { it.createdAtSeconds }
            }
        }
    }

    override suspend fun updateOrder(orderId: String, update: (Order) -> Order): Result<Order> {
        return orderMutex.withLock {
            runCatching {
                val order = getOrderById(orderId).getOrElse {
                    // Rethrow OrderNotFound exceptions
                    if (it is OrderException.OrderNotFound) {
                        throw it
                    }
                    throw Exception("Failed to get order", it)
                }

                val updatedOrder = update(order)
                val orderRef = db.collection(orderCollection).document(orderId)

                // Update the order document without bookings
                val orderWithoutBookings = updatedOrder.copy(bookings = emptyList())
                orderRef.set(orderWithoutBookings)

                // Get existing bookings IDs to identify which ones were removed
                val existingBookingsSnapshot = orderRef.collection(BOOKINGS_SUBCOLLECTION).get()
                val existingBookingIds = existingBookingsSnapshot.documents.map { it.id }.toSet()
                val updatedBookingIds = updatedOrder.bookings.map { it.id }.toSet()

                // Delete bookings that were removed
                val bookingsToDelete = existingBookingIds - updatedBookingIds
                bookingsToDelete.forEach { bookingId ->
                    orderRef.collection(BOOKINGS_SUBCOLLECTION).document(bookingId).delete()
                }

                // Update or add bookings
                updatedOrder.bookings.forEach { booking ->
                    orderRef.collection(BOOKINGS_SUBCOLLECTION)
                        .document(booking.id)
                        .set(booking)
                }

                updatedOrder
            }
        }
    }

    override suspend fun deleteOrder(orderId: String): Result<Unit> {
        return orderMutex.withLock {
            runCatching {
                val orderRef = db.collection(orderCollection).document(orderId)

                // Delete all booking documents in the subcollection
                val bookingsSnapshot = orderRef.collection(BOOKINGS_SUBCOLLECTION).get()
                bookingsSnapshot.documents.forEach { doc ->
                    orderRef.collection(BOOKINGS_SUBCOLLECTION).document(doc.id).delete()
                }

                // Delete the order document
                orderRef.delete()
            }
        }
    }

    override suspend fun clearAllOrders(): Result<Unit> {
        return orderMutex.withLock {
            runCatching {
                val orders = getAllOrdersMostRecentFirst().getOrThrow()
                orders.forEach { order ->
                    deleteOrder(order.id).getOrThrow()
                }
            }
        }
    }

    override suspend fun updateBooking(
        orderId: String,
        bookingId: String,
        update: (Booking) -> Booking
    ): Result<Order> {
        return orderMutex.withLock {
            runCatching {
                // Get the order with all bookings
                val order = getOrderById(orderId).getOrThrow()

                // Find the booking to update
                val bookingToUpdate = order.bookings.find { it.id == bookingId }
                    ?: throw OrderException.BookingNotFound

                // Apply the update
                val updatedBooking = update(bookingToUpdate)

                // Update the booking document in the subcollection
                db.collection(orderCollection)
                    .document(orderId)
                    .collection(BOOKINGS_SUBCOLLECTION)
                    .document(bookingId)
                    .set(updatedBooking)

                // Return the updated order
                val updatedBookings = order.bookings.map {
                    if (it.id == bookingId) updatedBooking else it
                }

                order.copy(bookings = updatedBookings)
            }
        }
    }


    override suspend fun fetchActiveOrders(): Result<List<Pair<String, Booking>>> {
        val orders = getAllOrdersMostRecentFirst().getOrNull() ?: return Result.failure(Exception("Failed to fetch orders"))
        return Result.success(
            orders.flatMap { order ->
                order.bookings.filter { booking ->
                    booking.deliveredSeconds == null
                }.map { booking ->
                    Pair(order.id, booking)
                }
            }
        )
    }
}