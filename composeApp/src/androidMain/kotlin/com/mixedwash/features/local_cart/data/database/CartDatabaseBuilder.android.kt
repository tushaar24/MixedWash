package com.mixedwash.features.local_cart.data.database

import android.app.Application
import androidx.room.Room

actual class CartDatabaseBuilder(private val ctx: Application) {
    actual fun getDatabase(): CartDatabase {
        val appContext = ctx.applicationContext
        val dbFile = appContext.getDatabasePath(cartDbFileName)
        return Room.databaseBuilder<CartDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).create()
    }
}