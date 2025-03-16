package com.mixedwash.features.local_cart.data.repository

import com.mixedwash.features.local_cart.data.database.CartDao
import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import com.mixedwash.features.local_cart.domain.error.CartException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LocalCartRepositoryImpl(private val cartDao : CartDao)  : LocalCartRepository{
    private val mutex = Mutex()

    override fun getCartItemFlow(): Result<Flow<List<CartItemEntity>>> {
        return Result.runCatching { cartDao.getItemsFlow() }
    }

    override suspend fun getCartItems(): Result<List<CartItemEntity>> {
        return Result.runCatching { cartDao.getItems() }
    }

    override suspend fun getCartItem(itemId: String): Result<CartItemEntity?> {
        return Result.runCatching { cartDao.getItemById(itemId) }
    }

    override suspend fun upsertCartItem(item: CartItemEntity): Result<Unit> = mutex.withLock{
        return Result.runCatching {
            cartDao.upsertItem(item)
        }
    }

    override suspend fun deleteCartItem(itemId: String): Result<Unit> = mutex.withLock {
        return Result.runCatching {
            val item = cartDao.getItemById(itemId) ?: return failure(CartException.ItemNotFoundException(itemId))
            cartDao.deleteItem(item)
        }
    }

    override suspend fun incrementCartItem(itemId: String): Result<Unit> = mutex.withLock {
        return Result.runCatching {
            val item = cartDao.getItemById(itemId)
                    ?: return failure(CartException.ItemNotFoundException(itemId))
            cartDao.upsertItem(item.copy(quantity = item.quantity + 1))
        }
    }

    override suspend fun decrementCartItem(itemId: String): Result<Unit> = mutex.withLock {
        return Result.runCatching {
            val item = cartDao.getItemById(itemId) ?: return failure(CartException.ItemNotFoundException(itemId))
            if (item.quantity == 1) return@runCatching deleteItem(item).getOrThrow()
            cartDao.upsertItem(item.copy(quantity = item.quantity - 1))
        }
    }

    override fun getMinimumProcessingDurationHrs(): Result<Flow<Int>> {
        return runCatching {
            getCartItemFlow().getOrThrow().map { items ->
                if (items.isEmpty()) 0
                else items.maxOf { it.deliveryTimeMaxInHrs ?: it.deliveryTimeMinInHrs }
            }
        }
    }

    private suspend fun deleteItem(item: CartItemEntity): Result<Unit> {
        return runCatching { cartDao.deleteItem(item) }
    }
}