package com.mixedwash.features.createOrder.domain.usecases.address

import com.mixedwash.features.createOrder.data.local.model.AddressEntity
import com.mixedwash.features.createOrder.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow

class AddressUseCases(private val repository: AddressRepository) {
    fun getAddressesFlow() = repository.getAddressesFlow()
    suspend fun getAddresses()  = repository.getAddresses()
    suspend fun upsertAddress(address: AddressEntity)  = repository.upsertAddress(address)
    suspend fun deleteAddress(address: AddressEntity) = repository.deleteAddress(address)
    suspend fun setDefaultAddress(uid: String) = repository.setDefaultAddress(uid)
    suspend fun getDefaultAddress() = repository.getDefaultAddress()
}

