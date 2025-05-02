package com.mixedwash.features.local_cart.data.database

import androidx.room.Room
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class CartDatabaseBuilder {
    actual fun getDatabase(): CartDatabase {
        val dbFilePath = documentDirectory() + "/$cartDbFileName"
        return Room.databaseBuilder<CartDatabase>(
            name = dbFilePath,
        ).create()
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }

}