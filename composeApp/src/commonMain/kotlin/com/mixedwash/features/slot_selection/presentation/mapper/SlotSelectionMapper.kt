package com.mixedwash.features.slot_selection.presentation.mapper

import com.mixedwash.features.slot_selection.data.model.DateSlotDto
import com.mixedwash.features.slot_selection.data.model.OfferDto
import com.mixedwash.features.slot_selection.data.model.TimeSlotDto
import com.mixedwash.features.slot_selection.data.model.response.AvailableSlotsResponseDto
import com.mixedwash.features.slot_selection.presentation.model.DateSlotPresentation
import com.mixedwash.features.slot_selection.presentation.model.OfferPresentation
import com.mixedwash.features.slot_selection.presentation.model.SlotsResponsePresentation
import com.mixedwash.features.slot_selection.presentation.model.TimeSlotPresentation

object SlotSelectionMapper {
    fun AvailableSlotsResponseDto.toPresentation() = SlotsResponsePresentation(
        pickupSlots = pickupSlots.map { it.toPresentation() },
        dropSlots = dropSlots.map { it.toPresentation() },
        commonOffers = commonOffers?.map { it.toPresentation() }
    )

    fun DateSlotDto.toPresentation() = DateSlotPresentation(
        id = id,
        timeStamp = timeStamp,
        timeSlots = timeSlots.map { it.toPresentation() }
    )

    fun TimeSlotDto.toPresentation() = TimeSlotPresentation(
        id = id,
        startTimeStamp = startTimeStamp,
        endTimeStamp = endTimeStamp,
        isAvailable = isAvailable,
        unavailableText = unavailableText,
        offersAvailable = offersAvailable.map { it.toPresentation() }
    )

    fun OfferDto.toPresentation() = OfferPresentation(
        code = code,
        icon = icon,
        title = title,
        subtitle = subtitle,
        isSelected = isSelected,
        isAvailable = isAvailable
    )

    fun TimeSlotPresentation.toDto() = TimeSlotDto(
        id = id,
        startTimeStamp = startTimeStamp,
        endTimeStamp = endTimeStamp,
        isAvailable = isAvailable,
        unavailableText = unavailableText,
        offersAvailable = offersAvailable.map { it.toDto() }
    )

    fun OfferPresentation.toDto() = OfferDto(
        code = code,
        icon = icon,
        title = title,
        subtitle = subtitle,
        isSelected = isSelected,
        isAvailable = isAvailable
    )
} 