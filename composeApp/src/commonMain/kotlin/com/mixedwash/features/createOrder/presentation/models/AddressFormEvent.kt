package com.mixedwash.features.createOrder.presentation.models

import com.mixedwash.presentation.models.FieldID

sealed class AddressFormEvent {
    data class OnFieldUpdate(val fieldId: FieldID, val value: String) : AddressFormEvent()
    data object OnFormEditSave : AddressFormEvent()
    data object OnFormEditCancel : AddressFormEvent()
    data object OnFormCreate : AddressFormEvent()
    data object OnFormViewDelete : AddressFormEvent()
    data object OnFormViewEdit : AddressFormEvent()
    data object OnFormClosed : AddressFormEvent()
}