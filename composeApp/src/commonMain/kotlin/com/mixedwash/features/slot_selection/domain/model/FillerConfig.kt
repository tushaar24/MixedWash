package com.mixedwash.features.slot_selection.domain.model

import kotlinx.datetime.LocalDate

data class FillerConfig(
    val startDateInclusive: LocalDate,
    val daysToGenerate: Int,
    val slotConfigs: List<TimeSlotConfig>
)
