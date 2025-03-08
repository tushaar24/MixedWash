package com.mixedwash.features.local_cart.data.repository

import com.mixedwash.features.local_cart.data.database.CartDao
import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import kotlinx.coroutines.flow.Flow

class LocalCartRepositoryImpl(private val cartDao : CartDao)  : LocalCartRepository{
    override fun getCartItemFlow(): Result<Flow<List<CartItemEntity>>> {
        return Result.runCatching { cartDao.getItemsFlow() }
    }

    override suspend fun getCartItems(): Result<List<CartItemEntity>> {
        return Result.runCatching { cartDao.getItems() }
    }

    override suspend fun getCartItem(itemId: String): Result<CartItemEntity?> {
        return Result.runCatching { cartDao.getItemById(itemId) }
    }

    override suspend fun upsertCartItem(item: CartItemEntity): Result<Unit> {
        return Result.runCatching { cartDao.upsertItem(item) }
    }

    override suspend fun deleteCartItem(item: CartItemEntity): Result<Unit> {
        return Result.runCatching { cartDao.deleteItem(item) }
    }
}