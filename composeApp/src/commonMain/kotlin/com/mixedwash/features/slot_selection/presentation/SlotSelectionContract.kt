package com.mixedwash.features.slot_selection.presentation

import androidx.compose.runtime.Immutable
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.features.slot_selection.presentation.model.DateSlotPresentation
import com.mixedwash.features.slot_selection.presentation.model.OfferPresentation
import com.mixedwash.features.slot_selection.presentation.model.TimeSlotPresentation

@Immutable
data class SlotSelectionScreenState(
    val isLoading: Boolean,
    val title: String,
    val pickupSlots: List<DateSlotPresentation>,
    val dropSlots: List<DateSlotPresentation>,
    val commonOffers: List<OfferPresentation>,
    val pickupDateSelectedId: Int? = null,
    val dropDateSelectedId: Int? = null,
    val pickupTimeSelectedId: Int? = null,
    val dropTimeSelectedId: Int? = null,
    val selectedOfferCode: String? = null,
    val screenEvent: (SlotSelectionScreenEvent) -> Unit = {},
    val deliveryNotes: String = "",
) {
    fun getOffers(): List<OfferPresentation> {
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
    data class OnPickupDateSelected(val dateSlot: DateSlotPresentation) : SlotSelectionScreenEvent()
    data class OnDropDateSelect(val dateSlot: DateSlotPresentation) : SlotSelectionScreenEvent()
    data class OnPickupTimeSelected(val timeSlot: TimeSlotPresentation) : SlotSelectionScreenEvent()
    data class OnDropTimeSelected(val timeSlot: TimeSlotPresentation) : SlotSelectionScreenEvent()
    data class OnOfferSelected(val offer: OfferPresentation) : SlotSelectionScreenEvent()
    data class OnDeliveryNotesChange(val value: String) : SlotSelectionScreenEvent()
    data object OnSubmit : SlotSelectionScreenEvent()
}

sealed class SlotSelectionScreenUiEvent {
    data class ShowSnackbar(val value: String, val type: SnackBarType) : SlotSelectionScreenUiEvent()
}


