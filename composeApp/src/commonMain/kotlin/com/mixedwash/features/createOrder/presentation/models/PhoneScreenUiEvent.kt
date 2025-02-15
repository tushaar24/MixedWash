package com.mixedwash.features.createOrder.presentation.models

import com.mixedwash.presentation.models.SnackBarType

sealed class PhoneScreenUiEvent {
    data class ShowSnackbar(val value : String, val type: SnackBarType) : PhoneScreenUiEvent()
}