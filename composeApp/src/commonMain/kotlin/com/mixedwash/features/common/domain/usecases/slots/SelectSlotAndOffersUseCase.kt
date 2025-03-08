package com.mixedwash.features.common.domain.usecases.slots

import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.common.data.entities.SlotAndOfferSelectionRequestEntity
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