package com.mixedwash.features.slot_selection.domain.api

import com.mixedwash.features.slot_selection.data.model.DateSlotDto

interface SlotsApi {
    suspend fun getTodaySlots(): Result<DateSlotDto>
}
