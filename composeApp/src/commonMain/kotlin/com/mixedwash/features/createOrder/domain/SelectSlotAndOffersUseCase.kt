package com.mixedwash.features.createOrder.domain

import com.mixedwash.domain.models.ErrorType
import com.mixedwash.domain.models.Resource
import com.mixedwash.features.createOrder.domain.models.SlotAndOfferSelectionRequestEntity
import kotlinx.coroutines.delay
import kotlin.random.Random

class SelectSlotAndOffersUseCase {
    suspend operator fun invoke(slotAndOfferSelectionRequestEntity: SlotAndOfferSelectionRequestEntity): Resource<SlotAndOfferSelectionRequestEntity> {
        delay(300)
        return if (Random.nextBoolean()) {
            Resource.Success(
                slotAndOfferSelectionRequestEntity
            )
        } else {
            Resource.Error(error = ErrorType.Api(code = 404, message = "Pickup Slot Not Available"))
        }
    }

}