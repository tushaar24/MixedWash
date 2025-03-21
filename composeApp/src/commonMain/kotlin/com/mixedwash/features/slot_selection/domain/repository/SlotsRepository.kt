package com.mixedwash.features.slot_selection.domain.repository

import com.mixedwash.features.slot_selection.data.model.response.AvailableSlotsResponseDto

interface SlotsRepository {
    suspend fun fetchAvailableSlots(): Result<AvailableSlotsResponseDto>
}