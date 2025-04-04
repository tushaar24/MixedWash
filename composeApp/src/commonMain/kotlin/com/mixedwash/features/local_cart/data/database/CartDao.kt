package com.mixedwash.features.local_cart.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.mixedwash.features.local_cart.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM CartItemEntity")
    suspend fun getItems(): List<CartItemEntity>

    @Query("SELECT * FROM CartItemEntity")
    fun getItemsFlow(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM CartItemEntity WHERE itemId = :itemId LIMIT 1")
    suspend fun getItemById(itemId: String): CartItemEntity?

    @Upsert
    suspend fun upsertItem(item: CartItemEntity)

    @Delete
    suspend fun deleteItem(item: CartItemEntity)

    @Query("DELETE FROM CartItemEntity")
    suspend fun deleteAllItems()

}