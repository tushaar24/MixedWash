package com.mixedwash.features.address.data.service

import com.mixedwash.core.data.USER_COLLECTION
import com.mixedwash.core.data.UserNotFoundException
import com.mixedwash.core.data.UserService
import com.mixedwash.core.data.util.AppCoroutineScope
import com.mixedwash.features.address.domain.error.AddressNotFoundException
import com.mixedwash.features.address.domain.error.OperationFailedException
import com.mixedwash.features.address.domain.model.Address
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class FirebaseAddressService(
    private val userService: UserService,
) {

    private val db = Firebase.firestore
    private val addressMutex = Mutex()

    suspend fun getAddresses(): Result<List<Address>> = runCatching {
        addressMutex.withLock {
            val user = userService.currentUser ?: throw UserNotFoundException()
            user.userMetadata?.addressList ?: emptyList()
        }
    }

    suspend fun addAddress(address: Address): Result<Unit> = runCatching {
        addressMutex.withLock {
            val user = userService.currentUser ?: throw UserNotFoundException()
            val uid = user.uid
            val metadata =
                user.userMetadata ?: throw UserNotFoundException("User metadata not found")

            val updatedAddressList = metadata.addressList.toMutableList().apply {
                add(address)
            }

            db.collection(USER_COLLECTION).document(uid).update(
                mapOf("addressList" to updatedAddressList)
            )
        }
    }.recoverCatching { e ->
        when (e) {
            is UserNotFoundException -> throw e
            else -> throw OperationFailedException("Failed to add address: ${e.message}", e)
        }
    }

    suspend fun upsertAddress(newAddress: Address): Result<Address> = runCatching {
        addressMutex.withLock {
            val user = userService.currentUser ?: throw UserNotFoundException()
            user.userMetadata ?: throw UserNotFoundException("User metadata not found")
            userService.updateMetadata {
                it.copy(addressList = it.addressList.filterNot { address -> address.uid == newAddress.uid }
                    .plus(newAddress))
            }
            newAddress
        }
    }

    suspend fun updateAddress(address: Address): Result<Unit> = runCatching {
        addressMutex.withLock {
            val user = userService.currentUser ?: throw UserNotFoundException()
            val uid = user.uid
            val metadata =
                user.userMetadata ?: throw UserNotFoundException("User metadata not found")

            val updatedAddressList = metadata.addressList.map {
                if (it.uid == address.uid) address else it
            }

            if (updatedAddressList.none { it.uid == address.uid }) {
                throw AddressNotFoundException()
            }

            db.collection(USER_COLLECTION).document(uid).update(
                mapOf("addressList" to updatedAddressList)
            )
        }
    }.recoverCatching { e ->
        when (e) {
            is UserNotFoundException, is AddressNotFoundException -> throw e
            else -> throw OperationFailedException("Failed to update address: ${e.message}", e)
        }
    }

    suspend fun deleteAddress(addressUid: String): Result<Unit> = runCatching {
        addressMutex.withLock {
            val user = userService.currentUser ?: throw UserNotFoundException()
            val uid = user.uid
            val metadata =
                user.userMetadata ?: throw UserNotFoundException("User metadata not found")

            val originalSize = metadata.addressList.size
            val updatedAddressList = metadata.addressList.filter { it.uid != addressUid }

            if (updatedAddressList.size == originalSize) {
                throw AddressNotFoundException()
            }

            userService.updateMetadata { it.copy(addressList = updatedAddressList) }

            db.collection(USER_COLLECTION).document(uid).update(
                mapOf("addressList" to updatedAddressList)
            )
        }
    }.recoverCatching { e ->
        when (e) {
            is UserNotFoundException, is AddressNotFoundException -> throw e
            else -> throw OperationFailedException("Failed to delete address: ${e.message}", e)
        }
    }

    suspend fun getAddressById(addressUid: String): Result<Address> = runCatching {
        addressMutex.withLock {
            val user = userService.currentUser ?: throw UserNotFoundException()
            val metadata =
                user.userMetadata ?: throw UserNotFoundException("User metadata not found")

            metadata.addressList.find { it.uid == addressUid }
                ?: throw AddressNotFoundException()
        }
    }.recoverCatching { e ->
        when (e) {
            is UserNotFoundException, is AddressNotFoundException -> throw e
            else -> throw OperationFailedException("Failed to get address: ${e.message}", e)
        }
    }

    suspend fun setCurrentAddress(addressUid: String?): Result<Unit> = runCatching {
        addressMutex.withLock {
            userService.updateMetadata { it.copy(defaultAddressId = addressUid) }
        }
    }

    fun getCurrentAddress(): Result<Address> = runCatching {
        val user = userService.currentUser ?: throw UserNotFoundException()
        val userMetadata =
            user.userMetadata ?: throw UserNotFoundException("UserMetadata not found")
        if (userMetadata.defaultAddressId == null) throw AddressNotFoundException()
        val defaultAddress =
            user.userMetadata.addressList.find { it.uid == userMetadata.defaultAddressId }
                ?: throw AddressNotFoundException()
        return Result.success(defaultAddress)
    }


}