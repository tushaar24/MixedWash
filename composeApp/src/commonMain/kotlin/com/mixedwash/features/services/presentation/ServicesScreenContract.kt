package com.mixedwash.features.services.presentation

import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.local_cart.presentation.model.CartItemPresentation
import com.mixedwash.features.services.presentation.model.GenderPresentation
import com.mixedwash.features.services.presentation.model.ServicePresentation

data class ServiceSubItemsListState(
    val title: String,
    val description: String,
    val placeHolder: String,
    val query: String,
    val items: List<CartItemPresentation>,
    val filters : List<GenderPresentation>,
)



data class ServicesScreenState(
    val services: List<ServicePresentation> = emptyList(),
    val isLoading: Boolean,
    val selectedServiceId: String? = null,
    val cartItems: List<CartItemPresentation> = emptyList(),
    val subItemsListState: ServiceSubItemsListState? = null,
)


sealed class ServicesScreenEvent {
    data class OnServiceClick(val serviceId: String) : ServicesScreenEvent()
    data class OnItemAdd(val itemId: String) : ServicesScreenEvent()
    data class OnItemIncrement(val itemId: String) : ServicesScreenEvent()
    data class OnItemDecrement(val itemId: String) : ServicesScreenEvent()
    data class OnItemDelete(val itemId: String) : ServicesScreenEvent()
    data class OnOpenSubItemsSheet(val serviceId: String) : ServicesScreenEvent()
    data class OnFilterClicked(val gender: GenderPresentation): ServicesScreenEvent()
    data class OnSubItemsQuery(val query: String): ServicesScreenEvent()
    data object OnCloseSubItemsSheet : ServicesScreenEvent()
    data object OnClosedSubItemsSheet : ServicesScreenEvent()
    data object OnProcessingDetailsClicked : ServicesScreenEvent()
    data object OnProceedClick : ServicesScreenEvent()
}

sealed class ServicesScreenUiEvent {
    data class ShowSnackbar(val payload: SnackbarPayload) : ServicesScreenUiEvent()
    data object OpenProcessingDetailsBottomSheet : ServicesScreenUiEvent()
    data object CloseSubItemsSheet : ServicesScreenUiEvent()
    data object ProceedToSlotSelection : ServicesScreenUiEvent()
}