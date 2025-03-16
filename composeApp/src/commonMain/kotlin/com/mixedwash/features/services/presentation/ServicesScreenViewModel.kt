package com.mixedwash.features.services.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.util.fastMap
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
import com.mixedwash.features.local_cart.domain.model.CartItem
import com.mixedwash.features.local_cart.domain.model.toCartItem
import com.mixedwash.features.local_cart.domain.model.toDomain
import com.mixedwash.features.services.data.remote.model.ServiceDto
import com.mixedwash.features.services.domain.ServicesDataRepository
import com.mixedwash.features.services.presentation.model.Gender
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

private const val TAG = "ServicesScreenViewModel"

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
        cartRepository.getCartItemFlow().getOrElse { flowOf(emptyList()) },
    ) { currentState, cartItems ->
        currentState.copy(
            cartItems = cartItems.map { it.toDomain() },
            subItemsListState = currentState.subItemsListState?.let {
                it.copy(
                    items = it.items.fastMap { subItem ->
                        cartItems.firstOrNull { cartItem ->
                            cartItem.itemId == subItem.itemId
                        }?.let { cartItem ->
                            if (cartItem.quantity != subItem.quantity)
                                subItem.copy(quantity = cartItem.quantity)
                            else subItem
                        } ?: subItem
                    }
                )
            }
        )
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


            is ServicesScreenEvent.OnOpenSubItemsSheet -> {
                viewModelScope.launch {
                    val service = getServiceById(event.serviceId) ?: return@launch
                    val subItemsListState = ServiceSubItemsListState(
                        title = "${service.title} Services",
                        description = "add the service items below to your order",
                        placeHolder = "search for an item",
                        query = "",
                        items = service.items?.map {
                            it.toCartItem(
                                service.deliveryTimeMinInHrs,
                                service.deliveryTimeMaxInHrs
                            )
                        } ?: emptyList(),
                        filters = emptyList()
                    )

                    updateState {
                        copy(subItemsListState = subItemsListState)
                    }
                }
            }

            ServicesScreenEvent.OnProcessingDetailsClicked -> {
                sendUiEvent(ServicesScreenUiEvent.OpenProcessingDetailsBottomSheet)
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

            ServicesScreenEvent.OnCloseSubItemsSheet -> {
                sendUiEvent(ServicesScreenUiEvent.CloseSubItemsSheet)
            }

            is ServicesScreenEvent.OnFilterClicked -> {
                state.value.subItemsListState?.filters?.let { list ->
                    val updatedFilters = if (list.contains(event.gender)) {
                        list - event.gender
                    } else {
                        list + event.gender
                    }
                    updateState {
                        val updatedList = selectedServiceId?.let {
                            filterSubItemsList(
                                serviceId = it,
                                searchQuery = state.value.subItemsListState?.query ?: "",
                                filters = updatedFilters,
                            ).getOrNull()
                        } ?: emptyList()
                        copy(
                            subItemsListState = subItemsListState?.copy(
                                filters = updatedFilters,
                                items = updatedList
                            )
                        )
                    }
                }
            }

            is ServicesScreenEvent.OnSubItemsQuery -> {
                viewModelScope.launch {
                    updateState {
                        val updatedCartItems = selectedServiceId?.let {
                            filterSubItemsList(
                                serviceId =  it,
                                searchQuery = event.query,
                                filters = state.value.subItemsListState?.filters ?: emptyList()
                            ).getOrNull()
                        } ?: emptyList()
                        copy(
                            subItemsListState = subItemsListState?.copy(
                                query = event.query, items = updatedCartItems
                            )
                        )
                    }
                }
            }

            ServicesScreenEvent.OnClosedSubItemsSheet -> {
                updateState {
                    copy(subItemsListState = null)
                }
            }

            ServicesScreenEvent.OnProceedClick -> {
                if(state.value.cartItems.isEmpty()) {
                    snackbarEvent(
                        message = "No items in cart",
                        type = SnackBarType.ERROR
                    )
                    return
                }
                sendUiEvent(ServicesScreenUiEvent.ProceedToSlotSelection)
            }
        }
    }

    private fun filterSubItemsList(
        serviceId: String,
        searchQuery: String = "",
        filters: List<Gender>
    ): Result<List<CartItem>> {
        val result = runCatching {
            state.value.subItemsListState?.run {
                val service = getServiceById(serviceId)
                    ?: throw IllegalStateException("Service not found for ID: $serviceId")

                val itemsList = service.items?.map {
                    it.toCartItem(
                        service.deliveryTimeMinInHrs,
                        service.deliveryTimeMaxInHrs
                    )
                }?.toMutableList() ?: throw IllegalStateException(
                    "Items list is null for service ID: $serviceId"
                )

                val queriedList = if (searchQuery.isNotBlank()) {
                    itemsList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                } else {
                    itemsList
                }
                val queriedFilteredList = if (filters.isNotEmpty()) {
                    queriedList.filter { item ->
                        filters.any {
                            it == item.metadata?.gender || item.metadata?.gender == Gender.BOTH
                        }
                    }
                } else {
                    queriedList
                }
                queriedFilteredList
            } ?: throw IllegalStateException("SubItemsListState is null")

        }.onFailure { e ->
            Logger.e(TAG, e.message ?: "Sub Items Filtering Failed")
        }
        return result
    }

    private fun getCartItemById(itemId: String): CartItem? {
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

    private fun sendUiEvent(event: ServicesScreenUiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(event)
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