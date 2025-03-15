package com.mixedwash.features.common.data.service.local

import androidx.room.Room
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class AddressDatabaseBuilder {
    actual fun getDatabase(): AddressDatabase {
        val dbFilePath = documentDirectory() + "/$addressDbFileName"
        return Room.databaseBuilder<AddressDatabase>(
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