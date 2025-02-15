package com.mixedwash.features.createOrder.presentation.models

import com.mixedwash.presentation.models.FormField

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
