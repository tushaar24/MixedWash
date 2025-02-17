package com.mixedwash.features.createOrder.domain.repository

import com.mixedwash.domain.models.Result
import com.mixedwash.features.createOrder.data.local.model.AddressEntity
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    fun getAddressesFlow(): Result<Flow<List<AddressEntity>>>
    suspend fun getAddresses(): Result<List<AddressEntity>>
    suspend fun getAddressesFlow(uid: String): Result<AddressEntity>
    suspend fun upsertAddress(address: AddressEntity) : Result<Unit>
    suspend fun deleteAddress(address: AddressEntity) : Result<Unit>
    suspend fun setDefaultAddress(uid: String) : Result<Unit>
    suspend fun getDefaultAddress() : Result<String>
}

