package com.mixedwash.features.slot_selection.presentation

import androidx.compose.runtime.Immutable
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.features.slot_selection.domain.model.response.DateSlot
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot
import com.mixedwash.features.slot_selection.presentation.model.BookingSlotState
import com.mixedwash.features.slot_selection.presentation.model.PickupSlotState

@Immutable
data class SlotSelectionScreenState(
    val isLoading: Boolean,
    val screenTitle: String,
    val pickupSlotState: PickupSlotState,
    val bookingsSlotStates: List<BookingSlotState>,
    val screenEvent: (SlotSelectionScreenEvent) -> Unit = {},
    val deliveryNotes: String = "",
) {
    fun canSubmit(): Boolean {
        return pickupSlotState.timeSlotSelectedId != null && bookingsSlotStates.all{it.timeSlotSelectedId != null}
    }
}

sealed class SlotSelectionScreenEvent {
    data class OnPickupDateSelected(val dateSlot: DateSlot) : SlotSelectionScreenEvent()
    data class OnPickupTimeSelected(val dateSlot:DateSlot, val timeSlot: TimeSlot) : SlotSelectionScreenEvent()
    data class OnBookingDateSelected(val bookingId: Int, val dateSlot: DateSlot) : SlotSelectionScreenEvent()
    data class OnBookingTimeSelected(val bookingId: Int, val dateSlot: DateSlot, val timeSlot: TimeSlot) : SlotSelectionScreenEvent()
    data object OnTogglePickupExpanded: SlotSelectionScreenEvent()
    data class OnToggleBookingExpanded(val bookingId: Int) : SlotSelectionScreenEvent()
    data class OnDeliveryNotesUpdate(val value: String) : SlotSelectionScreenEvent()
    data object OnSubmit : SlotSelectionScreenEvent()
}

sealed class SlotSelectionScreenUiEvent {
    data class ShowSnackbar(val value: String, val type: SnackBarType) : SlotSelectionScreenUiEvent()
    data object NavigateToReview : SlotSelectionScreenUiEvent()
}