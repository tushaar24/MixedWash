package com.mixedwash.features.slot_selection.presentation.model

import androidx.compose.runtime.Immutable
import kotlin.random.Random

@Immutable
data class DateSlotPresentation(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val timeStamp: Long,
    val timeSlots: List<TimeSlotPresentation>,
) {
    fun isAvailable(): Boolean = timeSlots.any { it.isAvailable }
} 