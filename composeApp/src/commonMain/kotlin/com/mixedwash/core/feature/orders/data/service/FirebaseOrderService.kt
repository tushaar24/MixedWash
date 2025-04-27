package com.mixedwash.core.feature.orders.data.service

import com.mixedwash.core.domain.error.UnauthorizedRequestException
import com.mixedwash.core.feature.auth.domain.UserService
import com.mixedwash.core.feature.auth.domain.model.User
import com.mixedwash.core.feature.orders.data.model.BookingDto
import com.mixedwash.core.feature.orders.data.model.toBooking
import com.mixedwash.core.feature.orders.data.model.toBookingDto
import com.mixedwash.core.feature.orders.data.model.toOrder
import com.mixedwash.core.feature.orders.data.model.toOrderDto
import com.mixedwash.core.feature.orders.domain.error.OrderException
import com.mixedwash.core.feature.orders.domain.model.Booking
import com.mixedwash.core.feature.orders.domain.model.Order
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.Transaction
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

const val ORDERS_COLLECTION = "ORDERS"
const val STAGING_ORDERS_COLLECTION = "STAGING_ORDERS"
const val BOOKINGS_SUB_COLLECTION = "BOOKINGS"
const val STAGING_BOOKINGS_SUB_COLLECTION = "STAGING_BOOKINGS"

interface OrderService {
    val useStagingCollection: Boolean
    suspend fun placeOrder(order: Order): Result<Unit>
    suspend fun getOrderById(orderId: String): Result<Order>
    suspend fun getAllOrdersMostRecentFirst(): Result<List<Order>>
    suspend fun updateOrder(orderId: String, update: (Order) -> Order): Result<Unit>
    suspend fun deleteOrder(orderId: String): Result<Unit>
    suspend fun clearAllOrders(): Result<Unit>
    suspend fun updateBooking(
        bookingId: String,
        update: (Booking) -> Booking
    ): Result<Unit>

    /**
     * Returns a list of active bookings along with their order ids.
     */
    suspend fun fetchActiveOrders(): Result<List<Pair<String, Booking>>>
}

class FirebaseOrderService(
    private val userService: UserService,
    override val useStagingCollection: Boolean = false,
) : com.mixedwash.core.feature.orders.data.service.OrderService {

    private val db = Firebase.firestore
    private val orderMutex = Mutex()
    private val CURRENT_ORDER_COLLECTION =
        if (useStagingCollection) com.mixedwash.core.feature.orders.data.service.STAGING_ORDERS_COLLECTION else com.mixedwash.core.feature.orders.data.service.ORDERS_COLLECTION
    private val CURRENT_BOOKINGS_SUB_COLLECTION =
        if (useStagingCollection) com.mixedwash.core.feature.orders.data.service.STAGING_BOOKINGS_SUB_COLLECTION else com.mixedwash.core.feature.orders.data.service.BOOKINGS_SUB_COLLECTION
    private val user: User
        get() = userService.currentUser ?: throw IllegalStateException("No current user")

    override suspend fun placeOrder(order: Order): Result<Unit> {
        return orderMutex.withLock {
            runCatching<Unit> { db.runTransaction { setOrder(order) } }
        }
    }

    override suspend fun getOrderById(orderId: String): Result<Order> {
        return runCatching {
            coroutineScope {
                // Get the order document and bookings in parallel
                val order = async {
                    val orderSnapshot = db.collection(CURRENT_ORDER_COLLECTION)
                        .document(orderId)
                        .get()

                    if (!orderSnapshot.exists) {
                        throw OrderException.OrderNotFound
                    }

                    val orderDocument = orderSnapshot.data<com.mixedwash.core.feature.orders.data.model.OrderDto>()
                    if (orderDocument.userId != user.uid) throw UnauthorizedRequestException()
                    orderDocument
                }

                val bookingsDto = async {
                    val bookingsSnapshot = db.collection(CURRENT_ORDER_COLLECTION)
                        .document(orderId)
                        .collection(CURRENT_BOOKINGS_SUB_COLLECTION)
                        .get()

                    bookingsSnapshot.documents.map { doc ->
                        doc.data<BookingDto>()
                    }
                }


                // Return the order with bookings
                order.await().toOrder(bookingsDto.await().map { it.toBooking() })
            }
        }
    }

    override suspend fun getAllOrdersMostRecentFirst(): Result<List<Order>> {
        return runCatching {
            val userId = user.uid

            coroutineScope {
                val orderDtoList = async {
                    db.collection(CURRENT_ORDER_COLLECTION)
                        .where { "user_id" equalTo userId }
                        .orderBy("created_at_seconds", Direction.DESCENDING)
                        .get()
                        .documents.map { doc ->
                            doc.data<com.mixedwash.core.feature.orders.data.model.OrderDto>()
                        }
                }

                val bookingsMap = async {
                    db.collectionGroup(CURRENT_BOOKINGS_SUB_COLLECTION)
                        .where { "user_id" equalTo userId }
                        .get()
                        .documents.map { document -> document.data<BookingDto>() }
                        .groupBy { it.orderId }
                }


                orderDtoList.await().map { orderDto ->
                    val orderBookings =
                        bookingsMap.await()[orderDto.id]?.map { it.toBooking() } ?: emptyList()
                    orderDto.toOrder(orderBookings)
                }
            }
        }
    }


    override suspend fun updateOrder(orderId: String, update: (Order) -> Order): Result<Unit> {
        return orderMutex.withLock {
            runCatching<Unit> {
                val order = getOrderById(orderId).getOrElse {
                    if (it is OrderException.OrderNotFound) {
                        throw it
                    }
                    throw Exception("Failed to get order", it)
                }

                val updatedOrder = update(order)

                // clear existing bookings for order
                db.runTransaction {
                    db.collectionGroup(CURRENT_BOOKINGS_SUB_COLLECTION)
                        .where { "order_id" equalTo orderId }
                        .get()
                        .documents
                        .forEach { delete(it.reference) }

                    setOrder(updatedOrder)
                }
            }
        }
    }

    override suspend fun deleteOrder(orderId: String): Result<Unit> {
        return orderMutex.withLock {
            runCatching<Unit> {
                val orderRef = db.collection(CURRENT_ORDER_COLLECTION).document(orderId)
                val bookingDocs = orderRef.collection(CURRENT_BOOKINGS_SUB_COLLECTION).get().documents
                db.runTransaction {
                    bookingDocs.forEach { delete(it.reference) }
                    delete(orderRef)
                }
            }
        }
    }

    override suspend fun clearAllOrders(): Result<Unit> {
        return orderMutex.withLock {
            runCatching {
                val orders =
                    db.collection(CURRENT_ORDER_COLLECTION).where { "user_id" equalTo user.uid }
                        .get().documents
                val bookings =
                    db.collectionGroup(CURRENT_BOOKINGS_SUB_COLLECTION).where { "user_id" equalTo user.uid }
                        .get().documents
                db.runTransaction {
                    bookings.forEach { delete(it.reference) }
                    orders.forEach { delete(it.reference) }
                }
            }
        }
    }

    override suspend fun updateBooking(
        bookingId: String,
        update: (Booking) -> Booking
    ): Result<Unit> {
        return orderMutex.withLock {
            runCatching<Unit> {
                // Find the booking document using collectionGroup query
                val bookingDocSnapShot = db.collectionGroup(CURRENT_BOOKINGS_SUB_COLLECTION)
                    .where { "id" equalTo bookingId }
                    .get()
                    .documents
                    .firstOrNull().let { it ?: throw OrderException.BookingNotFound }

                val bookingDto = bookingDocSnapShot.data<BookingDto>()

                val updatedBooking = update(bookingDto.toBooking())

                bookingDocSnapShot.reference.set(
                    updatedBooking.toBookingDto(
                        orderId = bookingDto.orderId,
                        userId = user.uid,
                        createdAtSeconds = bookingDto.createdAtSeconds
                    )
                )
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


    private fun Transaction.setOrder(order: Order): Result<Unit> {
        return runCatching<Unit> {
            // Create order document without bookings
            val orderDto = order.toOrderDto(user.uid)
            val orderRef = db.collection(CURRENT_ORDER_COLLECTION).document(order.id)

            set(documentRef = orderRef, data = orderDto)
            // Save each booking as a document in the bookings sub-collection
            order.bookings.forEach { booking ->
                set(
                    documentRef = orderRef.collection(CURRENT_BOOKINGS_SUB_COLLECTION)
                        .document(booking.id),
                    data = booking.toBookingDto(
                        orderId = order.id,
                        userId = user.uid,
                        createdAtSeconds = order.createdAtSeconds
                    )
                )
            }
        }.onFailure { throw OrderException.FailedToCreateOrder }
    }
}