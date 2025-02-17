package com.mixedwash.features.createOrder.presentation.phone

import com.mixedwash.presentation.models.FormField
import com.mixedwash.presentation.models.SnackBarType


data class PhoneScreenState(
    val title: String,
    val subtitle: String,
    val phoneNumber: String,
    val phoneNumberError: String? = null,
    val buttonText: String,
    val buttonEnabled: Boolean,
    val formField: FormField,
    val onSubmit: () -> Unit,
    val isLoading: Boolean
)

sealed class PhoneScreenEvent {
    data class UpdatePhone(val value: String) : PhoneScreenEvent()
    data object OnSubmit : PhoneScreenEvent()
}

sealed class PhoneScreenUiEvent {
    data class ShowSnackbar(val value : String, val type: SnackBarType) : PhoneScreenUiEvent()
}