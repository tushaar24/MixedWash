package com.mixedwash.features.address.data.repository

import com.mixedwash.features.address.data.service.FirebaseAddressService
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.address.domain.repository.AddressRepository

class FirebaseAddressRepositoryImpl(
    private val firebaseAddressService: FirebaseAddressService
) : AddressRepository {

    override suspend fun getAddresses(): Result<List<Address>> =
        firebaseAddressService.getAddresses()


    override suspend fun getAddressByUid(uid: String): Result<Address> =
        firebaseAddressService.getAddressById(uid)

    override suspend fun upsertAddress(address: Address): Result<Address> =
        firebaseAddressService.upsertAddress(address)

    override suspend fun deleteAddress(uid: String): Result<Unit> =
        firebaseAddressService.deleteAddress(addressUid = uid)

    override suspend fun setCurrentAddress(uid: String?): Result<Unit> =
        firebaseAddressService.setCurrentAddress(uid)


    override suspend fun getCurrentAddress(): Result<Address> =
        firebaseAddressService.getCurrentAddress()

}