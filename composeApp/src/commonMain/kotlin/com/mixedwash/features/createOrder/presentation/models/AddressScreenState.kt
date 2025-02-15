package com.mixedwash.features.createOrder.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class AddressScreenState(
    val title: String,
    val addressList: List<Address>,
    val selectedIndex: Int,
    val isLoading: Boolean,
    val buttonText: String,
    val onAddressSelected: (Int) -> Unit,
    val onAddressEdit: (Address) -> Unit,
    val onAddAddress: () -> Unit,
    val submitScreenCallBack: () -> Unit,
    val formEventCallBack: (AddressFormEvent) -> Unit,
    val formState: AddressFormState?
)

