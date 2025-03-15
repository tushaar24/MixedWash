package com.mixedwash.features.slot_selection.domain

import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.slot_selection.data.model.request.SlotsSelectionRequestEntity
import kotlinx.coroutines.delay
import kotlin.random.Random

class SelectSlotAndOffersUseCase {
    suspend operator fun invoke(slotAndOfferSelectionRequestEntity: SlotsSelectionRequestEntity): Result<SlotsSelectionRequestEntity> {
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