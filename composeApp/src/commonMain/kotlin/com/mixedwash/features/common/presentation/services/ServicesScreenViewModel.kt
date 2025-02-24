package com.mixedwash.features.common.presentation.services

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.mixedwash.features.common.data.service.local.DummyData
import com.mixedwash.features.common.presentation.services.model.ServiceVariantName



class ServicesScreenViewModel : ViewModel(){

    private val initialState = ServicesScreenState(
        serviceItems = DummyData.services,
        currentServiceId = 0,
        cartEntries = emptyList(),
        currentVariantName = ServiceVariantName.Mixed,
        totalCost = 0f,
        selectedServices = emptyList(),
    )

    private var _uiState = MutableStateFlow<ServicesScreenState>(initialState)
    val uiState: StateFlow<ServicesScreenState> = _uiState.asStateFlow()

    fun onEvent(event: ServicesScreenEvent) {
        when (event) {
            is ServicesScreenEvent.ToggleCurrentService -> {
                toggleCurrentService(event.newService)
            }

            is ServicesScreenEvent.ToggleCurrentVariant -> {
                toggleServiceVariant(event.newVariantName)
            }

            is ServicesScreenEvent.UpdateCartEntry -> {
                updateCartEntry(event.cartEntry)
            }
        }
    }

    private fun toggleCurrentService(newServiceId: Int) {
        _uiState.update {
            it.copy(currentServiceId = newServiceId, currentVariantName = ServiceVariantName.Mixed)
        }
    }

    private fun toggleOptedService(serviceId: Int, opted: Boolean) {
        _uiState.update {
            if (opted) {
                it.copy(selectedServices = it.selectedServices + serviceId)
            } else {
                it.copy(selectedServices = it.selectedServices - serviceId)
            }
        }
    }

    private fun updateCartEntry(cartEntry: CartEntry) {
        // check if this service(not just the variant) is already present
        // NOTE: if this request is first made from the "mixed" variant, and then again from
        //       the "segregated" variant, we'll still have an "existing entry", which will then be deleted
        val existingEntry = _uiState.value.cartEntries.find { it.serviceId == cartEntry.serviceId }

        if (existingEntry != null) {    // entry exists, remove
            _uiState.update {
                it.copy(
                    cartEntries = it.cartEntries - existingEntry    // remove the existing entry, and not the newly sent
                )
            }

            updateCost(-existingEntry.price)    // decrement the total cart price by existing entry's price
            toggleOptedService(cartEntry.serviceId, false)  // this service is no longer opted, remove from list

        } else {    // not found, add
            _uiState.update {
                it.copy(
                    cartEntries = it.cartEntries + cartEntry
                )
            }

            updateCost(cartEntry.price)
            toggleOptedService(cartEntry.serviceId, true)
        }
    }

    private fun toggleServiceVariant(newVariantName: ServiceVariantName) {
        _uiState.update {
            it.copy(currentVariantName = newVariantName)
        }
    }

    // the parameter 'add' can be positive or negative depending on whether
    // the item is being added or removed
    private fun updateCost(add: Float) {
        _uiState.update {
            it.copy(totalCost = it.totalCost + add)
        }
    }
}