package com.mixedwash.features.common.presentation.home
/*

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.common.data.service.local.LocationService
import com.mixedwash.features.location_availability.domain.LocationAvailabilityRepository
import com.mixedwash.libs.loki.core.Place
import com.mixedwash.libs.loki.geocoder.GeocoderResult
import com.mixedwash.libs.loki.geolocation.GeolocatorResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    val locationService: LocationService,
    val locationAvailabilityRepository: LocationAvailabilityRepository
) : ViewModel() {

//    private val _state = MutableStateFlow(initialState())
//    val state = addressFlow.combine(_state) { list : List<AddressEntity>, state: AddressScreenState ->
//        updateState { state.copy(addressList = list.map { it.toAddress() }) }
//        _state.value
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), initialState())

    private val _uiEventsChannel = Channel<HomeScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            fetchAvailability()
        }
    }

    private suspend fun fetchAvailability(): Boolean? {
        val result = locationService.getCurrentLocation()
        if (result !is GeolocatorResult.Success) {
            if (result is GeolocatorResult.PermissionDenied) {
                if (result.forever) {
                    snackbarEvent(
                        message = "Please grant location permission",
                        type = SnackBarType.WARNING,
                        action = locationService::openSettings,
                        actionText = "Open Settings"
                    )
                } else {
                    snackbarEvent(
                        message = "Location Permission Needed. Try Again",
                        type = SnackBarType.WARNING,
                    )
                }
            } else {
                snackbarEvent(
                    message = "Error Fetching Location",
                    type = SnackBarType.ERROR
                )
            }
            return null
        }

        val placeResult: Place =
            locationService.getPlaceByCoordinates(result.data.coordinates).let { placeResult ->
                if (placeResult !is GeocoderResult.Success<Place> || placeResult.data.isEmpty()) {
                    snackbarEvent("Fetching location failed", SnackBarType.ERROR)
                    null
                } else {
                    placeResult.data.first()
                }
            } ?: return null

        val postalCode: String = if (placeResult.postalCode == null) {
            snackbarEvent("Fetching Pin Code Failed", SnackBarType.ERROR)
            return null
        } else {
            placeResult.postalCode
        }
        val locationResult = locationAvailabilityRepository.isLocationServiceable(
            currentLat = result.data.coordinates.latitude,
            currentLon = result.data.coordinates.longitude,
            currentPincode = postalCode
        )


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

}

*/
