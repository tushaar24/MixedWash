package com.mixedwash.features.common.presentation.phone

import com.mixedwash.core.presentation.models.FormField
import com.mixedwash.core.presentation.models.SnackbarPayload


data class PhoneScreenState(
    val title: String,
    val subtitle: String,
    val phoneNumber: String,
    val phoneNumberError: String? = null,
    val buttonText: String,
    val buttonEnabled: Boolean,
    val formField: _root_ide_package_.com.mixedwash.core.presentation.models.FormField,
    val isLoading: Boolean
)

sealed class PhoneScreenEvent {
    data class UpdatePhone(val value: String) : PhoneScreenEvent()
    data object OnSubmit : PhoneScreenEvent()
}

sealed class PhoneScreenUiEvent {
    data class ShowSnackbar(val payload: SnackbarPayload) : PhoneScreenUiEvent()
    data object OnSubmitSuccess: PhoneScreenUiEvent()
}