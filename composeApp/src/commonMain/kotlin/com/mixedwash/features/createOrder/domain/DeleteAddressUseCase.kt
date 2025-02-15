package com.mixedwash.features.createOrder.domain

import com.mixedwash.domain.models.ErrorType
import com.mixedwash.domain.models.Resource
import com.mixedwash.features.createOrder.domain.models.AddressEntity
import kotlinx.coroutines.delay
import kotlin.random.Random

class DeleteAddressUseCase {
    suspend operator fun invoke(addressEntity: AddressEntity): Resource<AddressEntity> {
        delay(300)
        return if (Random.nextBoolean()) {
            Resource.Success(addressEntity)
        } else {
            Resource.Error(error = ErrorType.Api(code = 400, message = "Bad Request"))
        }
    }
}
