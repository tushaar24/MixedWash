package com.mixedwash.features.services.presentation

import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.local_cart.presentation.model.CartItemPresentation
import com.mixedwash.features.services.presentation.model.ServicePresentation

data class ServicesScreenState(
    val services: List<ServicePresentation> = emptyList(),
    val isLoading: Boolean,
    val selectedServiceId: String? = null,
    val cartItems: List<CartItemPresentation> = emptyList()
)


sealed class ServicesScreenEvent {
    data class OnServiceClick(val serviceId: String) : ServicesScreenEvent()
    data class OnItemAdd(val itemId: String) : ServicesScreenEvent()
    data class OnItemIncrement(val itemId: String) : ServicesScreenEvent()
    data class OnItemDecrement(val itemId: String) : ServicesScreenEvent()
    data class OnItemDelete(val itemId: String) : ServicesScreenEvent()
    data class OnOpenServiceItemsBottomSheet(val serviceId: String) : ServicesScreenEvent()
    data object OnProcessingDetailsClicked : ServicesScreenEvent()
}

sealed class ServicesScreenUiEvent {
    data class ShowSnackbar(val payload: SnackbarPayload) : ServicesScreenUiEvent()
    data class OpenServiceItemsBottomSheet(val serviceId: String) : ServicesScreenUiEvent()
    data object OpenProcessingDetailsBottomSheet : ServicesScreenUiEvent()
}