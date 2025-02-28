package com.mixedwash.features.common.presentation.slot_selection

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.mixedwash.core.presentation.models.SnackBarType
import kotlin.random.Random

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

@Immutable
data class DateSlot(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val timeStamp: Long,
    val timeSlots: List<TimeSlot>,
) {
    fun isAvailable(): Boolean = timeSlots.any { it.isAvailable }

}

@Immutable
data class TimeSlot(
    val id: Int = Random.nextInt(Int.MAX_VALUE),
    val startTimeStamp: Long,
    val endTimeStamp: Long,
    val isAvailable: Boolean,
    val unavailableText: String? = null,
    val offersAvailable: List<Offer> = emptyList(),
)

@Immutable
data class Offer(
    val code: String,
    val icon: ImageVector = Icons.Rounded.Star,
    val title: String,
    val subtitle: String,
    val isSelected: Boolean = false,
    val isAvailable: Boolean = true
)

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
}


