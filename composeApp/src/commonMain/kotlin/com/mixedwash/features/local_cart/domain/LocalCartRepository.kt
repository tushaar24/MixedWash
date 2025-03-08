package com.mixedwash.features.local_cart.domain

import com.mixedwash.features.local_cart.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

interface LocalCartRepository {
    fun getCartItemFlow() : Result<Flow<List<CartItemEntity>>>
    suspend fun getCartItems() : Result<List<CartItemEntity>>
    suspend fun getCartItem(itemId: String) : Result<CartItemEntity?>
    suspend fun upsertCartItem(item: CartItemEntity) : Result<Unit>
    suspend fun deleteCartItem(item: CartItemEntity) : Result<Unit>
}