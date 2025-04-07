package com.mixedwash.features.home.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.domain.models.Result
import com.mixedwash.core.orders.domain.repository.OrdersRepository
import com.mixedwash.core.presentation.components.ButtonData
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.navigation.NavArgType
import com.mixedwash.core.presentation.navigation.NavArgs
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.address.domain.model.toAddress
import com.mixedwash.features.address.domain.repository.AddressRepository
import com.mixedwash.features.address.presentation.AddressSearchState
import com.mixedwash.features.common.data.service.LocationService
import com.mixedwash.features.home.domain.HomeScreenDataRepository
import com.mixedwash.features.home.presentation.model.AddressBottomSheetState
import com.mixedwash.features.home.presentation.model.toPresentation
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import com.mixedwash.libs.loki.core.Place
import com.mixedwash.libs.loki.geocoder.GeocoderResult
import com.mixedwash.libs.loki.geolocation.GeolocatorResult
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.Gray900
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HomeScreenViewModel(
    private val locationService: LocationService,
    private val locationAvailabilityRepository: LocationAvailabilityRepository,
    private val homeScreenDataRepository: HomeScreenDataRepository,
    private val addressRepository: AddressRepository,
    private val ordersRepository: OrdersRepository
) : ViewModel() {

    private val serviceableAddressUidCache = mutableListOf<String>()

    private val _state = MutableStateFlow(HomeScreenState(isLoading = true))
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        HomeScreenState(isLoading = true)
    )

    private val _uiEventsChannel = Channel<HomeScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    init {
        reload()
    }


    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.Reload -> {
                reload()
            }

            is HomeScreenEvent.UpdateLocation -> {
                viewModelScope.launch {
                    updateCartAddress(event.address)
                }
            }

            HomeScreenEvent.OnBannerClick -> {
                sendUiEvent(HomeScreenUiEvent.Navigate(Route.ServicesRoute()))
            }

            HomeScreenEvent.OnIntroClick -> {
                sendUiEvent(HomeScreenUiEvent.Navigate(Route.OnboardingRoute))
            }

            is HomeScreenEvent.OnOfferClick -> {
                sendUiEvent(HomeScreenUiEvent.Navigate(Route.ServicesRoute()))
            }

            HomeScreenEvent.OnCloseAppRequest -> {
                sendUiEvent(HomeScreenUiEvent.CloseApp)
            }

            HomeScreenEvent.OnSeeAllServicesClicked -> {
                viewModelScope.launch {
                    _uiEventsChannel.send(HomeScreenUiEvent.Navigate(Route.ServicesRoute()))
                }
            }

            is HomeScreenEvent.OnServiceClicked -> {
                viewModelScope.launch {
                    sendUiEvent(HomeScreenUiEvent.Navigate(Route.ServicesRoute(serviceId = event.serviceId)))
                }
            }

            HomeScreenEvent.OnNavigateToFaqs -> {
                viewModelScope.launch {
                    sendUiEvent(HomeScreenUiEvent.Navigate(Route.FaqRoute))
                }
            }

            HomeScreenEvent.OnNavigateToProfile -> {
                viewModelScope.launch {
                    sendUiEvent(HomeScreenUiEvent.Navigate(Route.ProfileRoute))
                }
            }

            HomeScreenEvent.OnLocationSlabClicked -> {
                updateState {
                    copy(
                        addressBottomSheetState = AddressBottomSheetState(
                            isLoading = true,
                            title = "Select Address",
                            addresses = emptyList(),
                            selectedAddressId = null,
                            onAddressClicked = {
                                updateState {
                                    copy(
                                        addressBottomSheetState = addressBottomSheetState?.copy(
                                            selectedAddressId = it
                                        )
                                    )
                                }
                                onEvent(
                                    HomeScreenEvent.AddressListEvent.OnAddressClicked(
                                        it
                                    )
                                )
                            },
                            onAddressEdit = null,
                            onSearchBoxClick = { onEvent(HomeScreenEvent.AddressListEvent.OnAddressSearchBoxClicked) },
                            onClose = { onEvent(HomeScreenEvent.OnCloseAddressBottomSheet) },
                            addressSearchState = AddressSearchState(
                                query = "",
                                placeHolder = "Add New Address",
                                enabled = true,
                                autocompleteResult = emptyList(),
                                fetchingLocation = false,
                                onEvent = { onEvent(HomeScreenEvent.AddressListEvent.OnAddressSearchBoxClicked) }
                            )
                        )
                    )
                }
                viewModelScope.launch {
                    addressRepository.getAddresses().onSuccess { addressList ->
                        updateState {
                            copy(
                                addressBottomSheetState = addressBottomSheetState?.copy(
                                    isLoading = false,
                                    addresses = addressList,
                                )
                            )
                        }
                    }
                }
            }

            is HomeScreenEvent.AddressListEvent -> {
                viewModelScope.launch {
                    onEvent(HomeScreenEvent.OnCloseAddressBottomSheet)
                    when (event) {
                        is HomeScreenEvent.AddressListEvent.OnAddressClicked -> {
                            addressRepository.setCurrentAddress(event.uid).onFailure {
                                snackbarEvent(
                                    "Address could not be updated",
                                    SnackBarType.ERROR
                                )
                            }
                            updateCartAddress(addressRepository.getCurrentAddress().getOrNull())
                        }


                        is HomeScreenEvent.AddressListEvent.OnAddressSearchBoxClicked -> {
                            Logger.d("TAG", "OnAddressSearchBoxClicked")
                            navigateToAddressSelectionEvent()
                        }
                    }
                }
            }

            HomeScreenEvent.OnDismissedAddressBottomSheet -> {
                updateState {
                    copy(
                        addressBottomSheetState = null
                    )
                }
            }

            HomeScreenEvent.OnCloseAddressBottomSheet -> {
                sendUiEvent(HomeScreenUiEvent.DismissAddressBottomSheet)
            }

            HomeScreenEvent.OnChangeLocation -> {

                sendUiEvent(HomeScreenUiEvent.DismissAvailabilityBottomSheet)

                updateState {
                    copy(
                        cartAddress = CartAddressState.Unassigned
                    )
                }
                navigateToAddressSelectionEvent()

            }

            HomeScreenEvent.OnScreenStart -> {
                onScreenStart()
            }

            HomeScreenEvent.OnAvailabilityBottomSheetDismissed -> {
                /*
                 * Availability Bottom Sheet should only be dismissed for a good reason since
                * it prevents the user from interacting with the UI if his location is not
                * serviceable.
                *
                * The relevant state changes must be handled by the caller themselves
                * */
            }

            is HomeScreenEvent.OnPreviousOrderClicked -> {
                sendUiEvent(
                    HomeScreenUiEvent.Navigate(
                        Route.OrderDetailsRoute(
                            event.orderId,
                            destinationType = Route.OrderDetailsRoute.DestinationType.VIEW_ORDER_BY_ID
                        )
                    )
                )
            }
        }
    }

    private fun selectCartAddressFromTheAddressScreen() {
        updateState {
            copy(
                cartAddress = CartAddressState.Unassigned
            )
        }
        navigateToAddressSelectionEvent()
    }

    private fun onScreenStart() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            val currentAddress = addressRepository.getCurrentAddress().getOrNull()
            val addressList = addressRepository.getAddresses().getOrDefault(emptyList())
            if (currentAddress == null && addressList.isNotEmpty()) {
                updateState { copy(isLoading = false) }
                navigateToAddressSelectionEvent()
            } else {
                updateCartAddress(currentAddress)
                updateState { copy(isLoading = false) }
            }
        }
    }

    private fun reload() {
        viewModelScope.launch {
            _state.value = HomeScreenState(isLoading = true)
            val data = homeScreenDataRepository.fetchData()
            Logger.d("TAG", data.toString())
            when (data) {
                is Result.Success -> {
                    _state.value = data.data.toPresentation().toUiState().copy(
                        cartAddress = _state.value.cartAddress,
                        activeOrders = ordersRepository.getOrderStatus().getOrNull()
                    )
                }

                is Result.Error -> {
                    snackbarEvent(
                        message = data.error.toString(),
                        type = SnackBarType.ERROR
                    )
                }
            }
        }

    }

    private suspend fun updateCartAddress(address: Address?) {
        if (address != null) {
            updateState {
                copy(cartAddress = CartAddressState.LocationFetched(address))
            }
            updateAvailability()
            return
        }
        updateState {
            copy(
                cartAddress = CartAddressState.Unassigned
            )
        }
        if (!locationService.isLocationEnabled()) {
            updateState {
                copy(
                    cartAddress = CartAddressState.LocationError(
                        dialogPopupData = getDialog(
                            title = "Location Disabled",
                            subtitle = "Please enable location and try again",
                            icon = Icons.Rounded.LocationOn,
                            primaryText = "Retry",
                            onPrimaryClick = {
                                onEvent(HomeScreenEvent.UpdateLocation())
                            },
                        ),
                        issue = LocationIssue.LocationDisabled
                    )
                )
            }
            return
        }


        val result = locationService.getCurrentLocation()
        val fetchedAddress: Address = if (result !is GeolocatorResult.Success) {
            if (result is GeolocatorResult.PermissionDenied) {
                if (result.forever) {
                    updateState {
                        copy(
                            cartAddress = CartAddressState.LocationError(
                                dialogPopupData = getDialog(
                                    title = "Location Permission Denied",
                                    subtitle = "Please grant permission location",
                                    icon = Icons.Rounded.LocationOn,
                                    primaryText = "Grant Permission",
                                    onPrimaryClick = locationService::openSettings,
                                    secondaryText = "Retry",
                                    onSecondaryClick = { onEvent(HomeScreenEvent.UpdateLocation()) },
                                ),
                                issue = LocationIssue.LocationPermissionDeniedForever

                            )
                        )
                    }
                } else {
                    updateState {
                        copy(
                            cartAddress = CartAddressState.LocationError(
                                dialogPopupData = getDialog(
                                    title = "Location Permission Denied",
                                    subtitle = "Please grant permission location",
                                    icon = Icons.Rounded.LocationOn,
                                    primaryText = "Grant Permission",
                                    onPrimaryClick = { onEvent(HomeScreenEvent.UpdateLocation()) },
                                ),
                                issue = LocationIssue.LocationPermissionDenied
                            )
                        )
                    }
                }
            } else {
                updateState {
                    copy(
                        cartAddress = CartAddressState.LocationError(
                            dialogPopupData = getDialog(
                                title = "Error Fetching Location",
                                icon = Icons.Rounded.LocationOn,
                                primaryText = "Retry",
                                onPrimaryClick = { onEvent(HomeScreenEvent.UpdateLocation()) },
                                secondaryText = "Select Manually",
                                onSecondaryClick = { selectCartAddressFromTheAddressScreen() }
                            ),
                            issue = LocationIssue.LocationFetchError
                        )
                    )
                }
            }
            return
        } else {
            val placeResult =
                locationService.getPlaceByCoordinates(result.data.coordinates)
            val addressResult: Address =
                if (placeResult !is GeocoderResult.Success<Place> || placeResult.data.isEmpty()) {
                    updateState {
                        copy(
                            cartAddress = CartAddressState.LocationError(
                                dialogPopupData = getDialog(
                                    title = "Fetching Address Failed",
                                    icon = Icons.Rounded.LocationOn,
                                    primaryText = "Set Manually",
                                    onPrimaryClick = { selectCartAddressFromTheAddressScreen() },
                                    secondaryText = "Retry",
                                    onSecondaryClick = { onEvent(HomeScreenEvent.UpdateLocation()) }
                                ),
                                issue = LocationIssue.LocationFetchError
                            )
                        )
                    }
                    return
                } else {
                    placeResult.data.first().toAddress()
                }
            addressResult
        }

        updateState {
            copy(
                cartAddress = CartAddressState.LocationFetched(
                    address = fetchedAddress.copy(title = fetchedAddress.addressLine2)
                )
            )
        }
        updateAvailability()
    }

    private fun updateAvailability() {
        val cartAddress = _state.value.cartAddress
        if (cartAddress is CartAddressState.LocationFetched && cartAddress.availability is ServiceAvailability.Unassigned) {
            if (serviceableAddressUidCache.none { it == cartAddress.address.uid }) {
                viewModelScope.launch {
                    updateState {
                        copy(
                            isLoading = true
                        )
                    }
                    val address = cartAddress.address
                    val result = locationAvailabilityRepository.isLocationServiceable(
                        currentLat = address.lat,
                        currentLon = address.long,
                        currentPincode = cartAddress.address.pinCode
                    )
                    var error: String? = null
                    if (result !is Result.Success) {
                        error = "Error Fetching Availability"
                    } else if (!result.data) error = "Service Unavailable"
                    if (error != null) {
                        serviceableAddressUidCache.removeAll { it == address.uid }
                        updateState {
                            copy(
                                cartAddress = cartAddress.copy(
                                    availability = ServiceAvailability.Unavailable(
                                        title = error,
                                        description = "Sorry, service is currently unavailable at your current location",
                                        imageUrl = "https://assets-aac.pages.dev/assets/error_kitty.png",
                                        buttonText = "Promise You'll Be Back",
                                        error = error,
                                        currentLocationString = "${address.title} ${address.addressLine1}, ${address.addressLine2} ${address.pinCode}"
                                    )
                                )
                            )
                        }
                    } else {
                        serviceableAddressUidCache.add(address.uid)
                        updateState {
                            copy(
                                cartAddress = cartAddress.copy(
                                    availability = ServiceAvailability.Available
                                )
                            )
                        }
                    }
                    updateState {
                        copy(
                            isLoading = false
                        )
                    }
                }
            } else {
                updateState {
                    copy(
                        cartAddress = cartAddress.copy(
                            availability = ServiceAvailability.Available
                        )
                    )
                }
            }
        }

    }

    private fun navigateToAddressSelectionEvent() {
        sendUiEvent(
            HomeScreenUiEvent.Navigate(
                route = Route.AddressRoute(
                    title = "Select an Address",
                    screenType = Route.AddressRoute.ScreenType.SelectAddress,
                    submitText = "Select Address",
                    onSubmitNavArgsSerialized = Json.encodeToString(
                        NavArgs(NavArgType.NavigateUp)
                    )
                )
            )
        )
    }

    private fun getDialog(
        title: String,
        subtitle: String? = null,
        icon: ImageVector? = null,
        iconColor: Color = Gray800,
        primaryText: String,
        onPrimaryClick: () -> Unit,
        secondaryText: String? = null,
        onSecondaryClick: (() -> Unit)? = null,
        onDismissRequest: () -> Unit = {}
    ): DialogPopupData {
        return DialogPopupData(
            title = title,
            subtitle = subtitle,
            icon = icon,
            iconColor = iconColor,
            primaryButton = ButtonData(
                text = primaryText,
                onClick = onPrimaryClick,
                enabled = { true },
                contentColor = Gray100,
                containerColor = Gray900
            ),
            secondaryButton = secondaryText?.let { text ->
                onSecondaryClick?.let { onClick ->
                    ButtonData(
                        text = text,
                        onClick = onClick,
                        enabled = { true },
                        contentColor = Gray900,
                    )
                }
            },
            onDismissRequest = onDismissRequest
        )
    }

    private fun sendUiEvent(event: HomeScreenUiEvent) {
        viewModelScope.launch { _uiEventsChannel.send(event) }
    }

    private fun snackbarEvent(
        message: String,
        type: SnackBarType,
        duration: SnackbarDuration = SnackbarDuration.Short,
        action: (() -> Unit)? = null,
        actionText: String? = null
    ) {
        viewModelScope.launch {
            _uiEventsChannel.send(
                HomeScreenUiEvent.ShowSnackbar(
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

    private fun updateState(action: HomeScreenState.() -> HomeScreenState) {
        _state.update(action)
    }

}

