package com.mixedwash.features.common.data.repository

import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.common.data.entities.AddressEntity
import com.mixedwash.features.common.data.service.local.AddressDataStore
import com.mixedwash.features.common.data.service.local.AddressDatabase
import com.mixedwash.features.common.domain.repository.AddressRepository
import kotlinx.coroutines.flow.Flow

class AddressRepositoryImpl(
    private val db: AddressDatabase,
    private val addressDataStore: AddressDataStore
) : AddressRepository {
    override  fun getAddressesFlow(): Result<Flow<List<AddressEntity>>> {
        return try {
            Result.Success(db.addressDao().getAddressesFlow())
        } catch(e: Exception) {
            Result.Error(ErrorType.Unknown(e.message ?: "Unknown Error"))
        }

    }

    override suspend fun getAddresses(): Result<List<AddressEntity>> {
        return try {
            Result.Success(db.addressDao().getAddresses())
        } catch(e: Exception) {
            Result.Error(ErrorType.Unknown(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun getAddressesFlow(uid: String): Result<AddressEntity> {
        return try {
            Result.Success(db.addressDao().getAddressById(uid) ?: throw Exception("Address not found"))
        } catch(e: Exception) {
            if(e.message == "Address not found") {
                Result.Error(ErrorType.NotFound(e.message ?: "Unknown Error"))
            }
            else Result.Error(ErrorType.Unknown(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun upsertAddress(address: AddressEntity) : Result<Unit> {
        return try {
            Result.Success(db.addressDao().upsertAddress(address))
        } catch(e: Exception) {
            Result.Error(ErrorType.Unknown(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun deleteAddress(address: AddressEntity) : Result<Unit> {
        return try {
            Result.Success(db.addressDao().deleteAddress(address))
        } catch(e: Exception) {
            Result.Error(ErrorType.Unknown(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun setDefaultAddress(uid: String) : Result<Unit> {
        return try {
            Result.Success(addressDataStore.put(uid))
        } catch(e: Exception) {
            Result.Error(ErrorType.Unknown(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun getDefaultAddress(): Result<String> {
        return try {
            Result.Success(addressDataStore.get() ?: throw Exception("Address not found"))
        } catch (e: Exception) {
            if(e.message == "Address not found") {
                Result.Error(ErrorType.NotFound(e.message ?: "Unknown Error"))
            }
            else Result.Error(ErrorType.Unknown(e.message ?: "Unknown Error"))
        }
    }
}