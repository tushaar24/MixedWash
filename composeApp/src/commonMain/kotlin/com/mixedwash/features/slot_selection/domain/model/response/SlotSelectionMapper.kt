package com.mixedwash.features.slot_selection.domain.model.response

import com.mixedwash.features.slot_selection.data.model.DateSlotDto
import com.mixedwash.features.slot_selection.data.model.OfferDto
import com.mixedwash.features.slot_selection.data.model.TimeSlotDto
import com.mixedwash.features.slot_selection.data.model.response.AvailableSlotsResponseDto

object SlotSelectionMapper {
    fun AvailableSlotsResponseDto.toDomain() = SlotsResponse(
        pickupSlots = pickupSlots.map { it.toDomain() },
        dropSlots = dropSlots.map { it.toDomain() },
        commonOffers = commonOffers?.map { it.toDomain() }
    )

    fun DateSlotDto.toDomain() = DateSlot(
        id = id,
        timeStamp = timeStamp,
        timeSlots = timeSlots.map { it.toDomain() }
    )

    fun TimeSlotDto.toDomain() = TimeSlot(
        id = id,
        startTimeStamp = startTimeStamp,
        endTimeStamp = endTimeStamp,
        isAvailable = isAvailable,
        unavailableText = unavailableText,
        offersAvailable = offersAvailable.map { it.toDomain() }
    )

    fun OfferDto.toDomain() = Offer(
        code = code,
        icon = icon,
        title = title,
        subtitle = subtitle,
        isSelected = isSelected,
        isAvailable = isAvailable
    )

    fun TimeSlot.toDto() = TimeSlotDto(
        id = id,
        startTimeStamp = startTimeStamp,
        endTimeStamp = endTimeStamp,
        isAvailable = isAvailable,
        unavailableText = unavailableText,
        offersAvailable = offersAvailable.map { it.toDto() }
    )

    fun Offer.toDto() = OfferDto(
        code = code,
        icon = icon,
        title = title,
        subtitle = subtitle,
        isSelected = isSelected,
        isAvailable = isAvailable
    )
} 