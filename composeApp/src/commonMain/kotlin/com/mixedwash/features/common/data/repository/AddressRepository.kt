package com.mixedwash.features.common.data.repository

import com.mixedwash.features.common.data.entities.AddressEntity
import com.mixedwash.features.common.data.service.local.AddressDataStore
import com.mixedwash.features.common.data.service.local.AddressDatabase
import com.mixedwash.features.common.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow

class AddressRepositoryImpl(
    private val db: AddressDatabase,
    private val addressDataStore: AddressDataStore
) : AddressRepository {
    override fun getAddressesFlow(): Result<Flow<List<AddressEntity>>> {
        return runCatching {
            db.addressDao().getAddressesFlow()
        }
    }

    override suspend fun getAddresses(): Result<List<AddressEntity>> {
        return runCatching {
            db.addressDao().getAddresses()
        }
    }

    override suspend fun getAddressByUid(uid: String): Result<AddressEntity> {
        return runCatching {
            db.addressDao().getAddressById(uid) ?: throw NoSuchElementException("Address not found")
        }
    }

    override suspend fun upsertAddress(address: AddressEntity): Result<Unit> {
        return runCatching {
            db.addressDao().upsertAddress(address)
        }
    }

    override suspend fun deleteAddress(address: AddressEntity): Result<Unit> {
        return runCatching {
            db.addressDao().deleteAddress(address)
        }
    }

    override suspend fun setDefaultAddress(uid: String): Result<Unit> {
        return runCatching {
            addressDataStore.put(uid)
        }
    }

    override suspend fun getDefaultAddress(): Result<String> {
        return runCatching {
            addressDataStore.get() ?: throw NoSuchElementException("Address not found")
        }
    }
}