package com.mixedwash.features.createOrder.data.local.service

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase

actual class AddressDatabaseBuilder(private val ctx: Application) {
    actual fun getDatabase(): AddressDatabase {
        val appContext = ctx.applicationContext
        val dbFile = appContext.getDatabasePath(addressDbFileName)
        return Room.databaseBuilder<AddressDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).create()
    }
}