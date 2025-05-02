package com.mixedwash.features.home.presentation.model

import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.address.presentation.AddressSearchState

data class AddressBottomSheetState(
    val title: String,
    val isLoading: Boolean = false,
    val addresses: List<Address>,
    val selectedAddressId: String? = null,
    val onAddressClicked: ((String) -> Unit)? = null,
    val onAddressEdit: ((Address) -> Unit)? = null,
    val onSearchBoxClick: () -> Unit,
    val onClose: () -> Unit,
    val addressSearchState: AddressSearchState
)