package com.mixedwash.features.slot_selection.domain.api

import com.mixedwash.features.slot_selection.data.model.DateSlotDto
import com.mixedwash.features.slot_selection.domain.model.FillerConfig
import com.mixedwash.features.slot_selection.domain.model.TimeSlotConfig

interface FillerSlotsApi {
    val defaultSlotConfigs: List<TimeSlotConfig>
    fun generateSlots(config: FillerConfig): List<DateSlotDto>
}
