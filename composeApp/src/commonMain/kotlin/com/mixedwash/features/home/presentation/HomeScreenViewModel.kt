package com.mixedwash.features.home.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.Route
import com.mixedwash.core.domain.models.Result
import com.mixedwash.core.presentation.components.ButtonData
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.common.data.service.local.LocationService
import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.common.presentation.address.model.toAddress
import com.mixedwash.features.home.domain.HomeScreenDataRepository
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val locationService: LocationService,
    private val locationAvailabilityRepository: LocationAvailabilityRepository,
    private val homeScreenDataRepository: HomeScreenDataRepository
) : ViewModel() {


    private val _state = MutableStateFlow(HomeScreenState(isLoading = true))
    val state = _state.onEach {
        updateAvailability()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(3000),
        HomeScreenState(isLoading = true)
    )

    private val _uiEventsChannel = Channel<HomeScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    init {
        reload()
        viewModelScope.launch { updateLocation(null) }
    }


    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.Reload -> {
                reload()
            }

            is HomeScreenEvent.UpdateLocation -> {
                viewModelScope.launch {
                    updateLocation(event.address)
                }
            }

            HomeScreenEvent.OnBannerClick -> {
                sendEvent(HomeScreenUiEvent.Navigate(Route.ServicesRoute()))
            }

            HomeScreenEvent.OnIntroClick -> {
                TODO()
            }

            is HomeScreenEvent.OnOfferClick -> {
                sendEvent(HomeScreenUiEvent.Navigate(Route.ServicesRoute()))
            }

            HomeScreenEvent.OnDismissPermanentBottomSheet -> {
                sendEvent(HomeScreenUiEvent.NavigateUp)
            }

            HomeScreenEvent.OnSeeAllServicesClicked -> {
                viewModelScope.launch {
                    _uiEventsChannel.send(HomeScreenUiEvent.Navigate(Route.ServicesRoute()))
                }
            }

            is HomeScreenEvent.OnServiceClicked -> {
                viewModelScope.launch {
                    _uiEventsChannel.send(HomeScreenUiEvent.Navigate(Route.ServicesRoute(serviceId = event.serviceId)))
                }
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
                        cartAddress = _state.value.cartAddress
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

    private suspend fun updateLocation(address: Address?) {
        if (address != null) {
            updateState {
                copy(cartAddress = CartAddressState.LocationFetched(address))   // TODO : Replace with cart service

            }
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
                                    onPrimaryClick = { onEvent(HomeScreenEvent.UpdateLocation()) },
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
                    address = fetchedAddress.copy(title = fetchedAddress.addressLine1)
                )
            )
        }

    }

    private fun updateAvailability() {
        val cartAddress = _state.value.cartAddress
        if (cartAddress is CartAddressState.LocationFetched && cartAddress.availability is ServiceAvailability.Unassigned) {
            viewModelScope.launch {
                val address = cartAddress.address
                if (address.lat == null || address.long == null) return@launch
                val result =
                    locationAvailabilityRepository.isLocationServiceable(
                        currentLat = address.lat,
                        currentLon = address.long,
                        currentPincode = cartAddress.address.pinCode
                    )
                var error: String? = null
                if (result !is Result.Success) {
                    error = "Error Fetching Availability"
                } else if (!result.data) error = "Service Unavailable"
                if (error != null) {
                    updateState {
                        copy(
                            cartAddress = cartAddress.copy(
                                availability = ServiceAvailability.Unavailable(
                                    title = error,
                                    description = "Sorry, service is currently unavailable at your current location: \n${address.addressLine1 + " " + address.pinCode}",
                                    imageUrl = "https://assets-aac.pages.dev/assets/error_kitty.png",
                                    buttonText = "Promise You'll Be Back",
                                    error = error
                                )
                            )
                        )
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

    private fun sendEvent(event: HomeScreenUiEvent) {
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

