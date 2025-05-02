package com.mixedwash.features.slot_selection.presentation.model

import com.mixedwash.features.slot_selection.domain.model.response.DateSlot

data class PickupSlotState (
    val dateSlotSelectedId: Int?=null,
    val timeSlotSelectedId: Int?=null,
    val slots : List<DateSlot> = emptyList(),
    val isExpanded: Boolean = false,
)