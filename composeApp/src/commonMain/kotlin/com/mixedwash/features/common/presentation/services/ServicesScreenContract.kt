package com.mixedwash.features.common.presentation.services

import com.mixedwash.features.common.presentation.services.model.ServiceItem
import com.mixedwash.features.common.presentation.services.model.ServiceVariantName

/**
 * @param serviceItems list of all the service items with their details(eg: wash & fold, dy clean, etc)
 * @param currentServiceId the id of the service that is currently being shown on the service screen
 * @param selectedServices the tabs of these services will contain a checkmark, indicating that the user has added these services to cart
 * @param currentVariantName the name of the current selected variant(mixed or segregated) (null if not applicable(for eg in heavy wash service)
 * @param cartEntries the details of the items added to cart by the user
 * @param totalCost the total cost of all the items added to the cart
 */
data class ServicesScreenState(
    val serviceItems: List<ServiceItem>,
    val currentServiceId: Int,
    val selectedServices: List<Int>,
    val currentVariantName: ServiceVariantName?,
    val cartEntries: List<CartEntry>,
    val totalCost: Float,
) {
    // this gets the ServiceVariant object from ServiceVariantName enum class
    val currentVariant = serviceItems[currentServiceId].variants?.find { it.variantName == currentVariantName }

    // min. cart price = price of the service * min. cart for current variant
    val minCartPrice = serviceItems[currentServiceId].pricing?.times(currentVariant?.minCart ?: 1)
}

sealed interface ServicesScreenEvent {
    data class ToggleCurrentService(val newService: Int) : ServicesScreenEvent
    data class ToggleCurrentVariant(val newVariantName: ServiceVariantName) : ServicesScreenEvent
    data class UpdateCartEntry(val cartEntry: CartEntry) : ServicesScreenEvent
}



/**
 * This represents a single cart entry when a user selects a service from the services screen
 */
data class CartEntry(
    val serviceId: Int,
    val name: String,
    val price: Float,
)