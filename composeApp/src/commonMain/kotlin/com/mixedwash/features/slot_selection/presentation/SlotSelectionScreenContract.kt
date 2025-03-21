package com.mixedwash.features.slot_selection.presentation

import androidx.compose.runtime.Immutable
import com.mixedwash.core.booking.domain.model.BookingData
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.features.slot_selection.domain.model.response.DateSlot
import com.mixedwash.features.slot_selection.domain.model.response.Offer
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot

@Immutable
data class SlotSelectionScreenState(
    val isLoading: Boolean,
    val title: String,
    val pickupSlots: List<DateSlot>,
    val dropSlots: List<DateSlot>,
    val commonOffers: List<Offer>,
    val pickupDateSelectedId: Int? = null,
    val dropDateSelectedId: Int? = null,
    val pickupTimeSelectedId: Int? = null,
    val dropTimeSelectedId: Int? = null,
    val selectedOfferCode: String? = null,
    val screenEvent: (SlotSelectionScreenEvent) -> Unit = {},
    val deliveryNotes: String = "",
) {
    fun getOffers(): List<Offer> {
        val pickupSlotOffers = pickupSlots
            .firstOrNull { it.id == pickupDateSelectedId }
            ?.timeSlots
            ?.firstOrNull { it.id == pickupTimeSelectedId }
            ?.offersAvailable
            ?: emptyList()

        val dropSlotOffers = dropSlots
            .firstOrNull { it.id == dropDateSelectedId }
            ?.timeSlots
            ?.firstOrNull { it.id == dropTimeSelectedId }
            ?.offersAvailable
            ?: emptyList()

        return (commonOffers + pickupSlotOffers + dropSlotOffers).distinctBy { it.code }
    }
}

sealed class SlotSelectionScreenEvent {
    data class OnPickupDateSelected(val dateSlot: DateSlot) : SlotSelectionScreenEvent()
    data class OnDropDateSelect(val dateSlot: DateSlot) : SlotSelectionScreenEvent()
    data class OnPickupTimeSelected(val timeSlot: TimeSlot) : SlotSelectionScreenEvent()
    data class OnDropTimeSelected(val timeSlot: TimeSlot) : SlotSelectionScreenEvent()
    data class OnOfferSelected(val offer: Offer) : SlotSelectionScreenEvent()
    data class OnDeliveryNotesChange(val value: String) : SlotSelectionScreenEvent()
    data object OnSubmit : SlotSelectionScreenEvent()
}

sealed class SlotSelectionScreenUiEvent {
    data class ShowSnackbar(val value: String, val type: SnackBarType) : SlotSelectionScreenUiEvent()
    data class NavigateToReview(val bookingData: BookingData) : SlotSelectionScreenUiEvent()
}


