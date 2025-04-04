package com.mixedwash.features.slot_selection.presentation.model

import com.mixedwash.features.slot_selection.domain.model.response.DateSlot
import kotlin.random.Random

data class BookingSlotState(
    val id: Int = Random.nextInt(),
    val bookingServices: List<BookingServiceMetadata> = emptyList(),
    val serviceDurationInHrs: Int = 0,
    val dateSlotSelectedId: Int? = null,
    val timeSlotSelectedId: Int? = null,
    val dateSlots: List<DateSlot> = emptyList(),
    val isExpanded: Boolean = false
)