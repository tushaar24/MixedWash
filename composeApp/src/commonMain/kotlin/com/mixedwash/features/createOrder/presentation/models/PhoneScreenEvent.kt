package com.mixedwash.features.createOrder.presentation.models

sealed class PhoneScreenEvent {
    data class UpdatePhone(val value: String) : PhoneScreenEvent()
    data object OnSubmit : PhoneScreenEvent()
}