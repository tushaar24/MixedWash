package com.mixedwash.features.slot_selection.data.repository

import com.mixedwash.features.slot_selection.data.model.response.AvailableSlotsResponseDto
import com.mixedwash.features.slot_selection.domain.api.FillerSlotsApi
import com.mixedwash.features.slot_selection.domain.api.SlotsApi
import com.mixedwash.features.slot_selection.domain.model.FillerConfig
import com.mixedwash.features.slot_selection.domain.repository.SlotsRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class SlotsRepositoryImpl(
    private val slotsApi: SlotsApi,
    private val fillerSlotsApi: FillerSlotsApi,
) : SlotsRepository {

    override suspend fun fetchAvailableSlots(): Result<AvailableSlotsResponseDto> {
        return runCatching {
            // Get today's date
            val today = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date

            val todaySlots = slotsApi.getTodaySlots().getOrThrow()

            val fillerConfig = FillerConfig(
                startDateInclusive = today.plus(DatePeriod(days = 1)),
                daysToGenerate = 4,
                slotConfigs = fillerSlotsApi.defaultSlotConfigs
            )
            val futureSlots = fillerSlotsApi.generateSlots(fillerConfig)

            val allSlots = listOf(todaySlots) + futureSlots
            AvailableSlotsResponseDto(
                pickupSlots = allSlots,
                dropSlots = allSlots // Assuming same slots for pickup and drop
            )

        }
    }
}