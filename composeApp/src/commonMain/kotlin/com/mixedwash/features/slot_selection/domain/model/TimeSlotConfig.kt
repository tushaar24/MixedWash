package com.mixedwash.features.slot_selection.domain.model

import kotlinx.datetime.LocalTime

data class TimeSlotConfig(
    val startTime: LocalTime,
    val endTime: LocalTime
)
