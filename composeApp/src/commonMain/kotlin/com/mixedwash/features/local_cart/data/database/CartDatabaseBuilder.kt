package com.mixedwash.features.local_cart.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

expect class CartDatabaseBuilder {
    fun getDatabase(): CartDatabase
}

internal const val cartDbFileName = "cart.db"

fun RoomDatabase.Builder<CartDatabase>.create(): CartDatabase =
    this.setDriver(BundledSQLiteDriver())
//        .addMigrations(MIGRATIONS)
        .fallbackToDestructiveMigration(true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
