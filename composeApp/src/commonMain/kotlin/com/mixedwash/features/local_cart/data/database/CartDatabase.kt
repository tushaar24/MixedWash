package com.mixedwash.features.local_cart.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.mixedwash.features.local_cart.data.model.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 2)
abstract class CartDatabase: RoomDatabase() {
    abstract fun dao(): CartDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CartDatabaseConstructor : RoomDatabaseConstructor<CartDatabase> {
    override fun initialize(): CartDatabase
}


