package com.mixedwash.features.booking_details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.Route
import com.mixedwash.core.booking.domain.repository.BookingsRepository
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.util.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "BookingDetailsScreenViewModel"

class BookingDetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val bookingsRepository: BookingsRepository
) : ViewModel() {

    val route = savedStateHandle.toRoute<Route.BookingDetailsRoute>()

    private val _uiEventsChannel = Channel<BookingDetailsScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    private val _state: MutableStateFlow<BookingDetailsScreenState> = MutableStateFlow(
        BookingDetailsScreenState(
            items = emptyList(),
            pickupSlot = null,
            dropSlot = null,
            deliveryAddress = null,
            title = "",
        )
    )

    val state: StateFlow<BookingDetailsScreenState> = _state.asStateFlow().onStart {
        viewModelScope.launch {
            when (route.destinationType) {
                Route.BookingDetailsRoute.DestinationType.CONFIRM_DRAFT_BOOKING -> bookingsRepository.getDraftBooking()
                Route.BookingDetailsRoute.DestinationType.VIEW_BOOKING_BY_ID -> {
                    if (route.bookingId == null) {
                        showSnackbar(SnackbarPayload("No Booking Id Provided", SnackBarType.ERROR))
                        return@launch
                    }
                    bookingsRepository.getBookingById(route.bookingId)
                }
            }.onSuccess { booking ->
                _state.update {
                    it.copy(
                        items = booking.bookingItems,
                        pickupSlot = booking.pickupSlotSelected,
                        dropSlot = booking.dropSlotSelected,
                        deliveryAddress = booking.address,
                        title = if (route.destinationType == Route.BookingDetailsRoute.DestinationType.CONFIRM_DRAFT_BOOKING) "Review Your Booking" else "Booking Details",
                        screenType = if (route.destinationType == Route.BookingDetailsRoute.DestinationType.CONFIRM_DRAFT_BOOKING) BookingDetailsScreenType.CONFIRMATION else BookingDetailsScreenType.BOOKING_DETAILS,
                        note = if (route.destinationType == Route.BookingDetailsRoute.DestinationType.CONFIRM_DRAFT_BOOKING) "*final order cost will be calculated based on the exact contents of your order confirmed after processing." else null
                    )
                }
            }.onFailure {
                showSnackbar(SnackbarPayload("Error Loading Booking", SnackBarType.ERROR))
                Logger.e(TAG, it.message ?: "Error Loading Booking")
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = BookingDetailsScreenState(
            items = emptyList(),
            pickupSlot = null,
            dropSlot = null,
            deliveryAddress = null,
            title = "",
        )
    )

    fun onEvent(event: BookingDetailsScreenEvent) {
        when (event) {
            BookingDetailsScreenEvent.OnConfirmBooking -> {
                viewModelScope.launch {
                    bookingsRepository.placeDraftBooking().onSuccess {
                        sendEvent(BookingDetailsScreenUiEvent.NavigateToBookingConfirmation(it.id))
                    }.onFailure {
                        showSnackbar(SnackbarPayload("Error Placing Booking", SnackBarType.ERROR))
                        Logger.e(TAG, it.message ?: "Error Placing Booking")
                    }
                }
            }
        }
    }

    private fun showSnackbar(snackbarPayload: SnackbarPayload) {
        sendEvent(BookingDetailsScreenUiEvent.ShowSnackbar(snackbarPayload))
    }

    private fun sendEvent(event: BookingDetailsScreenUiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(event)
        }
    }
}