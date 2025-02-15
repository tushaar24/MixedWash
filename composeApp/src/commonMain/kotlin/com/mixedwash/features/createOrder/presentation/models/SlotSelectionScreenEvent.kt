package com.mixedwash.features.createOrder.presentation.models

import com.mixedwash.features.createOrder.presentation.screens.DateSlot
import com.mixedwash.features.createOrder.presentation.screens.Offer
import com.mixedwash.features.createOrder.presentation.screens.TimeSlot

sealed class SlotSelectionScreenEvent {
    data class OnPickupDateSelected(val dateSlot: DateSlot) : SlotSelectionScreenEvent()
    data class OnDropDateSelect(val dateSlot: DateSlot) : SlotSelectionScreenEvent()
    data class OnPickupTimeSelected(val timeSlot: TimeSlot) : SlotSelectionScreenEvent()
    data class OnDropTimeSelected(val timeSlot: TimeSlot) : SlotSelectionScreenEvent()
    data class OnOfferSelected(val offer: Offer) : SlotSelectionScreenEvent()
    data class OnDeliveryNotesChange(val value: String) : SlotSelectionScreenEvent()
    data object OnSubmit : SlotSelectionScreenEvent()
}