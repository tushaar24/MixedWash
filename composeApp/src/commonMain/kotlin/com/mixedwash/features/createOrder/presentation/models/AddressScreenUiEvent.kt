package com.mixedwash.features.createOrder.presentation.models

import com.mixedwash.presentation.components.DialogPopupData
import com.mixedwash.presentation.models.SnackBarType

sealed class AddressScreenUiEvent {
    data class ShowSnackbar(val value: String, val type: SnackBarType) : AddressScreenUiEvent()
    data object CloseForm : AddressScreenUiEvent()
    data class ShowDialogPopup(
        val dialogPopupData: DialogPopupData
    ) : AddressScreenUiEvent()
    data object ClosePopup : AddressScreenUiEvent()
}
