package com.mixedwash.features.createOrder.domain.usecases

import com.mixedwash.domain.models.ErrorType
import com.mixedwash.domain.models.Result
import kotlinx.coroutines.delay
import kotlin.random.Random

object FindUserByPhoneUseCase {
    suspend operator fun invoke(input: String): Result<Boolean> {
        delay(1000)
        return if (Random.nextBoolean()) {
            Result.Success(true)
        } else Result.Error(error = ErrorType.Api(400,"Not Authorized"))
    }

}