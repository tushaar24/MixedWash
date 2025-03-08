package com.mixedwash.features.services.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import com.mixedwash.features.services.data.remote.model.ServiceDto
import com.mixedwash.features.services.domain.ServicesDataRepository
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
        currentState.copy(cartItems = cartItems)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ServicesScreenState(isLoading = true)
    )

    init {
        reload()
    }


    fun onEvent(event: ServicesScreenEvent) {
        when (event) {
            is ServicesScreenEvent.OnItemAdd -> {
                viewModelScope.launch {
                    val item = cartItem(event.itemId)?.let { it.copy(quantity = it.quantity + 1) }
                        ?: serviceItem(event.itemId) ?: return@launch

                    cartRepository.upsertCartItem(item)
                }
            }

            is ServicesScreenEvent.OnServiceClick -> {
                updateState {
                    copy(selectedServiceId = event.serviceId)
                }
            }

            is ServicesScreenEvent.OnItemRemove -> {
                viewModelScope.launch {
                    val item = cartItem(event.itemId) ?: return@launch
                    cartRepository.deleteCartItem(item).onFailure { e ->
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
        }
    }

    private fun cartItem(itemId: String): CartItemEntity? {
        return state.value.cartItems.firstOrNull { item ->
            item.itemId == itemId
        }
    }

    private fun serviceItem(itemId: String): CartItemEntity? {
        return state.value.services.firstNotNullOfOrNull { service ->
            service.items?.firstOrNull { item -> item.itemId == itemId }?.toCartItemEntity()
        }
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
            if (services.isFailure) {
                snackbarEvent(
                    message = "Error fetching services",
                    type = SnackBarType.ERROR
                )
            } else {
                services.getOrNull()?.let {
                    updateState {
                        copy(
                            services = it.services.map { serviceDto: ServiceDto -> serviceDto.toPresentation() },
                            isLoading = false,
                            selectedServiceId = selectedServiceId
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