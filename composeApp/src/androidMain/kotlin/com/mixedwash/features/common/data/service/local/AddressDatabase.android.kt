package com.mixedwash.features.common.data.service.local

import android.app.Application
import androidx.room.Room
import com.mixedwash.features.common.data.service.local.AddressDatabase
import com.mixedwash.features.common.data.service.local.addressDbFileName
import com.mixedwash.features.common.data.service.local.create

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