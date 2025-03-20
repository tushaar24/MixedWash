package com.mixedwash.features.address.domain.repository

import com.mixedwash.features.address.domain.model.Address

interface AddressRepository {
    suspend fun getAddresses(): Result<List<Address>>
    suspend fun getAddressByUid(uid: String): Result<Address>
    suspend fun upsertAddress(address: Address) : Result<Address>
    suspend fun deleteAddress(uid: String) : Result<Unit>
    suspend fun setCurrentAddress(uid: String?) : Result<Unit>
    suspend fun getCurrentAddress() : Result<Address>
}

