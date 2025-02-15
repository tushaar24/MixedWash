package com.mixedwash.features.createOrder.presentation.models

import com.mixedwash.presentation.models.SnackBarType

sealed class SlotSelectionScreenUiEvent {
    data class ShowSnackbar(val value: String, val type: SnackBarType) : SlotSelectionScreenUiEvent()
}
