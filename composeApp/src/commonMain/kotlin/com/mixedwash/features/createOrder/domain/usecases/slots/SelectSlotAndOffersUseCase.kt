package com.mixedwash.features.createOrder.domain.usecases.slots

import com.mixedwash.domain.models.ErrorType
import com.mixedwash.domain.models.Result
import com.mixedwash.features.createOrder.data.local.model.SlotAndOfferSelectionRequestEntity
import kotlinx.coroutines.delay
import kotlin.random.Random

class SelectSlotAndOffersUseCase {
    suspend operator fun invoke(slotAndOfferSelectionRequestEntity: SlotAndOfferSelectionRequestEntity): Result<SlotAndOfferSelectionRequestEntity> {
        delay(300)
        return if (Random.nextBoolean()) {
            Result.Success(
                slotAndOfferSelectionRequestEntity
            )
        } else {
            Result.Error(error = ErrorType.Api(code = 404, message = "Pickup Slot Not Available"))
        }
    }

}