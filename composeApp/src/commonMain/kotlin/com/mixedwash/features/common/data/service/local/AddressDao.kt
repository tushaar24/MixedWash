package com.mixedwash.features.common.data.service.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mixedwash.features.common.data.entities.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM AddressEntity")
    suspend fun getAddresses(): List<AddressEntity>

    @Query("SELECT * FROM AddressEntity")
    fun getAddressesFlow(): Flow<List<AddressEntity>>

    @Query("SELECT * FROM AddressEntity WHERE uid = :uid LIMIT 1")
    suspend fun getAddressById(uid: String): AddressEntity?

    @Upsert
    suspend fun upsertAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)
}
