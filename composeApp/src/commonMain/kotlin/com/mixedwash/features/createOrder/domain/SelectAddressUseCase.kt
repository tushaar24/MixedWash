package com.mixedwash.features.createOrder.domain

import com.mixedwash.domain.models.ErrorType
import com.mixedwash.domain.models.Resource
import com.mixedwash.features.createOrder.presentation.models.AddressId
import kotlinx.coroutines.delay
import kotlin.random.Random

class SelectAddressUseCase() {
    suspend operator fun invoke(id: AddressId): Resource<Unit> {
        delay(300)
        return if (Random.nextBoolean()) {
            Resource.Success(Unit)
        } else {
            Resource.Error(error = ErrorType.Api(code = 400, message = "Bad Request"))
        }
    }

}
