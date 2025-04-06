package com.mixedwash.core.orders.data.service

import com.mixedwash.core.data.UserService
import com.mixedwash.core.data.util.AppCoroutineScope
import com.mixedwash.core.orders.domain.model.Order
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

const val ORDERS_COLLECTION = "ORDERS"

interface OrderService {
    suspend fun saveOrder(order: Order): Result<Order>
    suspend fun getOrderById(orderId: String): Result<Order>
    suspend fun getOrders(): Result<List<Order>>
    suspend fun updateOrder(order: Order): Result<Order>
    suspend fun deleteOrder(orderId: String): Result<Unit>
}

class FirebaseOrderService(
    private val userService: UserService,
    private val appCoroutineScope: AppCoroutineScope
) : OrderService {

    private val db = Firebase.firestore
    private val orderMutex = Mutex()

    override suspend fun saveOrder(order: Order): Result<Order> {
        return orderMutex.withLock {
            runCatching {
                // Save to main orders collection
                db.collection(ORDERS_COLLECTION)
                    .document(order.id)
                    .set(order)

                order
            }
        }
    }

    override suspend fun getOrderById(orderId: String): Result<Order> {
        return runCatching {
            val snapshot = db.collection(ORDERS_COLLECTION)
                .document(orderId)
                .get()

            if (snapshot.exists) {
                snapshot.data<Order>()
            } else {
                throw NoSuchElementException("Order not found")
            }
        }
    }

    override suspend fun getOrders(): Result<List<Order>> {
        return runCatching {
            val userId = userService.currentUser?.uid ?: throw IllegalStateException("Current user hsa no id")
            val snapshot = db.collection(ORDERS_COLLECTION)
                .where { "customer_id" equalTo userId }
                .get()

            snapshot.documents.mapNotNull { doc ->
                doc.data<Order>()
            }
        }
    }

    override suspend fun updateOrder(order: Order): Result<Order> {
        return orderMutex.withLock {
            runCatching {
                // Update in main orders collection
                db.collection(ORDERS_COLLECTION)
                    .document(order.id)
                    .update(order)

                order
            }
        }
    }

    override suspend fun deleteOrder(orderId: String): Result<Unit> {
        return orderMutex.withLock {
            runCatching {
                // Delete from main orders collection
                db.collection(ORDERS_COLLECTION)
                    .document(orderId)
                    .delete()
            }
        }
    }
}