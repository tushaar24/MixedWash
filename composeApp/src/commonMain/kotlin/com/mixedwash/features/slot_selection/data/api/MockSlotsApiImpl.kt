package com.mixedwash.features.slot_selection.data.api

import com.mixedwash.features.slot_selection.data.model.DateSlotDto
import com.mixedwash.features.slot_selection.domain.api.FillerSlotsApi
import com.mixedwash.features.slot_selection.domain.api.SlotsApi
import com.mixedwash.features.slot_selection.domain.model.FillerConfig
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class MockSlotsApiImpl(private val fillerSlotsApi: FillerSlotsApi) : SlotsApi {
    override suspend fun getTodaySlots(): Result<DateSlotDto> {
        return runCatching {
            fillerSlotsApi.generateSlots(
                config = FillerConfig(
                    startDateInclusive = Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
                    daysToGenerate = 1,
                    slotConfigs = fillerSlotsApi.defaultSlotConfigs
                )
            ).first()
        }
    }
}
