package com.mixedwash.features.local_cart.domain

import com.mixedwash.features.local_cart.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

interface LocalCartRepository {
    fun getCartItemFlow() : Result<Flow<List<CartItemEntity>>>
    suspend fun getCartItems() : Result<List<CartItemEntity>>
    suspend fun getCartItem(itemId: String) : Result<CartItemEntity?>
    suspend fun upsertCartItem(item: CartItemEntity) : Result<Unit>
    suspend fun deleteCartItem(itemId: String): Result<Unit>
    suspend fun incrementCartItem(itemId: String): Result<Unit>
    suspend fun decrementCartItem(itemId: String): Result<Unit>
    fun getMinimumProcessingDurationHrs(): Result<Flow<Int>>
}