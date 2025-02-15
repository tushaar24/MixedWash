package com.mixedwash.features.createOrder.domain

import com.mixedwash.domain.models.ErrorType
import com.mixedwash.domain.models.Resource
import kotlinx.coroutines.delay
import kotlin.random.Random

object FindUserByPhoneUseCase {
    suspend operator fun invoke(input: String): Resource<Boolean> {
        delay(1000)
        return if (Random.nextBoolean()) {
            Resource.Success(true)
        } else Resource.Error(error = ErrorType.Api(400,"Not Authorized"))
    }

}