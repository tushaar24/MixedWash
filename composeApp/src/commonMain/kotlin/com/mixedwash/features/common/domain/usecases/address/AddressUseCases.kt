package com.mixedwash.features.common.domain.usecases.address

import com.mixedwash.features.common.data.entities.AddressEntity
import com.mixedwash.features.common.domain.repository.AddressRepository

class AddressUseCases(private val repository: AddressRepository) {
    fun getAddressesFlow() = repository.getAddressesFlow()
    suspend fun getAddresses()  = repository.getAddresses()
    suspend fun upsertAddress(address: AddressEntity)  = repository.upsertAddress(address)
    suspend fun deleteAddress(address: AddressEntity) = repository.deleteAddress(address)
    suspend fun setDefaultAddress(uid: String) = repository.setDefaultAddress(uid)
    suspend fun getDefaultAddress() = repository.getDefaultAddress()
}

