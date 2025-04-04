package com.mixedwash.features.local_cart.data.repository

import com.mixedwash.features.local_cart.data.database.CartDao
import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import com.mixedwash.features.local_cart.domain.error.CartException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class LocalCartRepositoryImpl(private val cartDao : CartDao, private val dispatcher: CoroutineDispatcher = Dispatchers.IO)  : LocalCartRepository{
    private val mutex = Mutex()

    override fun getCartItemFlow(): Result<Flow<List<CartItemEntity>>> = 
        Result.runCatching { cartDao.getItemsFlow() }

    override suspend fun getCartItems(): Result<List<CartItemEntity>> = withContext(dispatcher) {
        Result.runCatching { cartDao.getItems() }
    }

    override suspend fun getCartItem(itemId: String): Result<CartItemEntity?> = withContext(dispatcher) {
        Result.runCatching { cartDao.getItemById(itemId) }
    }

    override suspend fun upsertCartItem(item: CartItemEntity): Result<Unit> = withContext(dispatcher) {
        mutex.withLock {
            Result.runCatching {
                cartDao.upsertItem(item)
            }
        }
    }

    override suspend fun deleteCartItem(itemId: String): Result<Unit> = withContext(dispatcher) {
        mutex.withLock {
            Result.runCatching {
                val item = cartDao.getItemById(itemId) ?: return@runCatching failure<Unit>(CartException.ItemNotFoundException(itemId)).getOrThrow()
                cartDao.deleteItem(item)
            }
        }
    }

    override suspend fun incrementCartItem(itemId: String): Result<Unit> = withContext(dispatcher) {
        mutex.withLock {
            Result.runCatching {
                val item = cartDao.getItemById(itemId)
                        ?: return@runCatching failure<Unit>(CartException.ItemNotFoundException(itemId)).getOrThrow()
                cartDao.upsertItem(item.copy(quantity = item.quantity + 1))
            }
        }
    }

    override suspend fun decrementCartItem(itemId: String): Result<Unit> = withContext(dispatcher) {
        mutex.withLock {
            Result.runCatching {
                val item = cartDao.getItemById(itemId) ?: return@runCatching failure<Unit>(CartException.ItemNotFoundException(itemId)).getOrThrow()
                if (item.quantity == 1) return@runCatching deleteItem(item).getOrThrow()
                cartDao.upsertItem(item.copy(quantity = item.quantity - 1))
            }
        }
    }

    override suspend fun clearCartItems(): Result<Unit> = withContext(dispatcher) {
        mutex.withLock {
            runCatching {
                cartDao.deleteAllItems()
            }
        }
    }

    override fun getMinimumProcessingDurationHrs(): Result<Flow<Int>> = 
        runCatching {
            getCartItemFlow().getOrThrow().map { items ->
                if (items.isEmpty()) 0
                else items.maxOf { it.deliveryTimeMaxInHrs ?: it.deliveryTimeMinInHrs }
            }
        }

    private suspend fun deleteItem(item: CartItemEntity): Result<Unit> = withContext(dispatcher) {
        runCatching { cartDao.deleteItem(item) }
    }
}