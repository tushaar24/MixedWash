package com.mixedwash.features.common.data.service.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mixedwash.features.common.data.entities.AddressEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [AddressEntity::class], version = 1)
abstract class AddressDatabase : RoomDatabase() {
    abstract fun addressDao(): AddressDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AddressDatabase> {
    override fun initialize(): AddressDatabase
}

internal const val addressDbFileName = "address.db"

expect class AddressDatabaseBuilder {
    fun getDatabase(): AddressDatabase
}

fun RoomDatabase.Builder<AddressDatabase>.create(): AddressDatabase =
    this.setDriver(BundledSQLiteDriver())
//        .addMigrations(MIGRATIONS)    // TODO : ADD MIGRATIONS
//        .fallbackToDestructiveMigrationOnDowngrade()
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()

