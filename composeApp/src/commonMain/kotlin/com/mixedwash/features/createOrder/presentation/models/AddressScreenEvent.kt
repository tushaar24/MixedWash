package com.mixedwash.features.createOrder.presentation.models

sealed class AddressScreenEvent {
    data class OnAddressSelect(val index: Int) : AddressScreenEvent()
    data class OnAddressEdit(val address: Address) : AddressScreenEvent()
    data object OnAddAddress : AddressScreenEvent()
    data object OnScreenSubmit : AddressScreenEvent()
    data class OnFormEvent(val formEvent: AddressFormEvent) : AddressScreenEvent()
    data class OnAddressDeleteConfirmation(val addressId: String) : AddressScreenEvent()
}