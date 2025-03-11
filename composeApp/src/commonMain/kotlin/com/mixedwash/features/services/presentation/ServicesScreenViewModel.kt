package com.mixedwash.features.services.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.Route
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import com.mixedwash.features.local_cart.domain.error.onCartError
import com.mixedwash.features.local_cart.presentation.model.CartItemPresentation
import com.mixedwash.features.local_cart.presentation.model.toPresentation
import com.mixedwash.features.services.data.remote.model.ServiceDto
import com.mixedwash.features.services.domain.ServicesDataRepository
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.features.services.presentation.model.toCartItemEntity
import com.mixedwash.features.services.presentation.model.toPresentation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ServicesScreenViewModel(
    private val servicesDataRepository: ServicesDataRepository,
    private val cartRepository: LocalCartRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _uiEventsChannel = Channel<ServicesScreenUiEvent>(BUFFERED)
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ServicesScreenState(isLoading = true))

    val state = combine(
        _state,
        cartRepository.getCartItemFlow().getOrElse { flowOf(emptyList()) }
    ) { currentState, cartItems ->
        currentState.copy(cartItems = cartItems.map { it.toPresentation() })
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ServicesScreenState(isLoading = true)
    )

    init {
        val selectedServiceId =
            runCatching { savedStateHandle.toRoute<Route.ServicesRoute>().serviceId }.getOrNull()
        reload(selectedServiceId)
    }


    fun onEvent(event: ServicesScreenEvent) {
        when (event) {

            is ServicesScreenEvent.OnServiceClick -> {
                updateState {
                    copy(selectedServiceId = event.serviceId)
                }
            }

            is ServicesScreenEvent.OnItemIncrement -> {
                viewModelScope.launch {
                    cartRepository.incrementCartItem(event.itemId).onCartError(itemNotFound = {
                        snackbarEvent(
                            "Quantity Increment Failed: Item Not Found", SnackBarType.ERROR
                        )
                    })
                }
            }

            is ServicesScreenEvent.OnItemDecrement -> {
                viewModelScope.launch {
                    cartRepository.decrementCartItem(event.itemId).onCartError(itemNotFound = {
                        snackbarEvent(
                            message = "Quantity Decrement Failed: Item Not Found",
                            type = SnackBarType.ERROR
                        )
                    })
                }
            }

            is ServicesScreenEvent.OnItemDelete -> {
                viewModelScope.launch {
                    cartRepository.deleteCartItem(event.itemId).onFailure { e ->
                        snackbarEvent(message = "Error removing item", type = SnackBarType.ERROR)
                        e.printStackTrace()
                    }
                }
            }


            is ServicesScreenEvent.OnOpenServiceItemsBottomSheet -> {
                viewModelScope.launch {
                    _uiEventsChannel
                        .send(
                            ServicesScreenUiEvent.OpenServiceItemsBottomSheet(
                                serviceId = event.serviceId
                            )
                        )
                }
            }

            ServicesScreenEvent.OnProcessingDetailsClicked -> {
                viewModelScope.launch {
                    _uiEventsChannel.send(ServicesScreenUiEvent.OpenProcessingDetailsBottomSheet)
                }
            }

            is ServicesScreenEvent.OnItemAdd -> {
                viewModelScope.launch {
                    Logger.d("TAG", "Creating Cart Item ${event.itemId}")
                    createCartItemById(event.itemId)?.let { cartItem ->
                        Logger.d("TAG", "Created $cartItem")
                        cartRepository.upsertCartItem(cartItem).onFailure { e ->
                            snackbarEvent(message = "Error adding item", type = SnackBarType.ERROR)
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun getCartItemById(itemId: String): CartItemPresentation? {
        return state.value.cartItems.firstOrNull { item ->
            item.itemId == itemId
        }
    }

    private fun createCartItemById(itemId: String): CartItemEntity? {
        return state.value.services.firstNotNullOfOrNull { service ->
            service.items?.firstOrNull { item -> item.itemId == itemId }?.toCartItemEntity(
                deliveryTimeMinInHrs = service.deliveryTimeMinInHrs,
                deliveryTimeMaxInHrs = service.deliveryTimeMaxInHrs
            )
        }
    }

    private fun getServiceById(serviceId: String): ServicePresentation? {
        return state.value.services.firstOrNull { it.serviceId == serviceId }
    }

    private fun snackbarEvent(
        message: String,
        type: SnackBarType,
        duration: SnackbarDuration = SnackbarDuration.Short,
        action: (() -> Unit)? = null,
        actionText: String? = null
    ) {
        viewModelScope.launch {
            _uiEventsChannel.send(  // <- here
                ServicesScreenUiEvent.ShowSnackbar(
                    payload = SnackbarPayload(
                        message = message,
                        type = type,
                        duration = duration,
                        action = action,
                        actionText = actionText
                    )
                )
            )
        }
    }


    private fun reload(selectedServiceId: String? = null) {
        viewModelScope.launch {
            val services = servicesDataRepository.getServices()
            val preselectedService = services.getOrNull()?.services?.firstOrNull { it.serviceId == selectedServiceId }?.serviceId
            if (services.isFailure) {
                snackbarEvent(
                    message = "Error fetching services",
                    type = SnackBarType.ERROR
                )
            } else {
                services.getOrNull()?.let { res ->
                    updateState {
                        copy(
                            services = res.services.map { serviceDto: ServiceDto -> serviceDto.toPresentation() },
                            isLoading = false,
                            selectedServiceId = preselectedService
                                ?: res.services.firstOrNull()?.serviceId
                        )
                    }
                }
            }
        }
    }

    private fun updateState(action: ServicesScreenState.() -> ServicesScreenState) {
        _state.update (action)
    }

}