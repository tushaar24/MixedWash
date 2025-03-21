package com.mixedwash.features.slot_selection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.booking.domain.model.toBookingItem
import com.mixedwash.core.booking.domain.model.toBookingTimeSlot
import com.mixedwash.core.booking.domain.repository.BookingsRepository
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.address.domain.repository.AddressRepository
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import com.mixedwash.features.local_cart.domain.model.toDomain
import com.mixedwash.features.slot_selection.domain.model.response.SlotSelectionMapper.toDomain
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot
import com.mixedwash.features.slot_selection.domain.repository.SlotsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SlotSelectionScreenViewModel(
    private val slotsRepository: SlotsRepository,
    private val localCartRepository: LocalCartRepository,
    private val addressRepository: AddressRepository,
    private val bookingsRepository: BookingsRepository
) : ViewModel() {

    private val initialState = SlotSelectionScreenState(
        isLoading = true,
        title = "Book your slots",
        pickupSlots = emptyList(),
        dropSlots = emptyList(),
        commonOffers = emptyList(),
        screenEvent = { onEvent(it) }
    )

    private val processingDurationInHrs: StateFlow<Int> = localCartRepository
        .getMinimumProcessingDurationHrs()
        .getOrDefault(emptyFlow())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 24)

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<SlotSelectionScreenState> = _state.onStart {
        reloadScreenState()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialState
    )

    private val _uiEventsChannel = Channel<SlotSelectionScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()


    private fun onEvent(event: SlotSelectionScreenEvent) {
        when (event) {
            is SlotSelectionScreenEvent.OnPickupDateSelected -> {
                updateState {
                    copy(
                        pickupDateSelectedId = event.dateSlot.id,
                        pickupTimeSelectedId = null
                    )
                }
            }

            is SlotSelectionScreenEvent.OnDropDateSelect -> {
                updateState {
                    copy(
                        dropDateSelectedId = event.dateSlot.id,
                        dropTimeSelectedId = null
                    )
                }
            }

            is SlotSelectionScreenEvent.OnDropTimeSelected -> {
                updateState {
                    copy(
                        dropTimeSelectedId = event.timeSlot.id,
                        selectedOfferCode = if (state.value.getOffers()
                                .any { it.code == selectedOfferCode }
                        ) selectedOfferCode else null
                    )
                }
            }

            is SlotSelectionScreenEvent.OnOfferSelected -> {
                updateState { copy(selectedOfferCode = event.offer.code) }
            }

            is SlotSelectionScreenEvent.OnPickupTimeSelected -> {
                val pickupDateTime = Instant.fromEpochSeconds(event.timeSlot.endTimeStamp)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                val pickupFormatted =
                    "${pickupDateTime.date}, ${pickupDateTime.time}"
                val dropDateTime = Instant.fromEpochSeconds(
                    event.timeSlot.endTimeStamp.plusHours(processingDurationInHrs.value)
                ).toLocalDateTime(TimeZone.currentSystemDefault())
                val dropFormatted = "${dropDateTime.date}, ${dropDateTime.time}"

                Logger.d(
                    "SlotSelectionScreenViewModel",
                    "PickupSlotEnd: $pickupFormatted | Expected DropSlotStart: $dropFormatted | Gap Hrs: ${processingDurationInHrs.value}"
                )

                updateState {
                    copy(
                        pickupDateSelectedId = state.value.pickupSlots.first { dateSlot -> dateSlot.timeSlots.any { it.id == event.timeSlot.id } }.id,
                        pickupTimeSelectedId = event.timeSlot.id,
                        dropTimeSelectedId = dropTimeSelectedId?.let { dropId ->
                            if (getTimeSlotById(dropId)!!.startTimeStamp >=
                                event.timeSlot.endTimeStamp.plusHours(processingDurationInHrs.value)
                            ) {
                                dropTimeSelectedId
                            } else {
                                null
                            }
                        },
                        dropSlots = dropSlots.map { dateSlot ->
                            dateSlot.copy(
                                timeSlots = dateSlot.timeSlots.map { dropTimeSlot ->
                                    val isAvailable =
                                        dropTimeSlot.startTimeStamp >= event.timeSlot.endTimeStamp.plusHours(
                                            processingDurationInHrs.value
                                        )
                                    val dropSlotDateTime = Instant.fromEpochSeconds(dropTimeSlot.startTimeStamp).toLocalDateTime(TimeZone.currentSystemDefault())
                                    val dropSlotFormatted = "${dropSlotDateTime.date}, ${dropSlotDateTime.time}"
                                    Logger.d("SlotSelectionScreenViewModel", "DropSlotStart $dropSlotFormatted isAvailable: $isAvailable")
                                    dropTimeSlot.copy(
                                        isAvailable = isAvailable,
                                    )
                                }
                            )
                        }
                    )
                }
                runCatching {
                    val result = state.value.dropSlots.firstOrNull { dateSlot ->
                        dateSlot.isAvailable()
                    }
                    return@runCatching result
                }.getOrNull()?.let {
                    onEvent(SlotSelectionScreenEvent.OnDropDateSelect(it))
                } ?: updateState { copy(dropDateSelectedId = null) }
            }

            is SlotSelectionScreenEvent.OnDeliveryNotesChange -> {
                if (event.value.length > 400) return
                updateState {
                    copy(
                        deliveryNotes = event.value
                    )
                }
            }

            SlotSelectionScreenEvent.OnSubmit -> {
                viewModelScope.launch {
                    Logger.d("SlotSelectionScreenViewModel", "OnSubmit")
                    runCatching {
                        if (!submitValidation()) {
                            throw IllegalStateException("Invalid Slots")
                        }
                        val address = addressRepository.run {
//                            val defaultAddress = getDefaultAddress().getOrThrow()
//                            getAddressByUid(defaultAddress).getOrThrow()
                            getAddresses().getOrThrow().first()
                        }
                        val cartItems =
                            localCartRepository.getCartItems().getOrThrow().map { it.toDomain() }
                        bookingsRepository.setBookingDraft(
                            pickupSlot = getTimeSlotById(state.value.pickupTimeSelectedId!!)!!.toBookingTimeSlot(),
                            dropSlot = getTimeSlotById(state.value.dropTimeSelectedId!!)!!.toBookingTimeSlot(),
                            offer = state.value.selectedOfferCode,
                            deliveryNotes = state.value.deliveryNotes,
                            bookingItems = cartItems.map { it.toBookingItem() },
                            address = address
                        ).onSuccess {
                            localCartRepository.clearCartItems().onFailure { e ->
                                snackbarEvent("Error clearing cart", type = SnackBarType.ERROR)
                                Logger.e("SlotSelectionScreenViewModel", "Error clearing cart")
                                e.printStackTrace()
                            }
                            _uiEventsChannel.send(SlotSelectionScreenUiEvent.NavigateToReview(it))
                        }.onFailure {
                            Logger.d("SlotSelectionScreenViewModel", "Error Booking")
                            _uiEventsChannel.send(
                                SlotSelectionScreenUiEvent.ShowSnackbar(
                                    it.message ?: "Error Booking", type = SnackBarType.ERROR
                                )
                            )
                        }
                    }.onFailure { e ->
                        val message = e.message ?: "Error Booking"
                        snackbarEvent(message, type = SnackBarType.ERROR)
                        Logger.e("SlotSelectionScreenViewModel", message)
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    private fun getTimeSlotById(id: Int): TimeSlot? {
        state.value.run {
            for (dateSlot in pickupSlots) {
                for (timeSlot in dateSlot.timeSlots) {
                    if (timeSlot.id == id) return timeSlot
                }
            }
            for (dateSlot in dropSlots) {
                for (timeSlot in dateSlot.timeSlots) {
                    if (timeSlot.id == id) return timeSlot
                }
            }
        }
        return null
    }

    private fun submitValidation(): Boolean {
        state.value.apply {
            if (pickupTimeSelectedId == null || dropTimeSelectedId == null) {
                snackbarEvent("Please select both Pick and Drop slots", type = SnackBarType.WARNING)
                return false
            }
            // check if drop selected is atleast 24 after pickup
            if (
                getTimeSlotById(dropTimeSelectedId)!!.startTimeStamp < getTimeSlotById(
                    pickupTimeSelectedId
                )!!.endTimeStamp.plusHours(processingDurationInHrs.value)
            ) {
                snackbarEvent(
                    "Pick and drop slots must be 24 hours apart",
                    type = SnackBarType.WARNING
                )
                return false
            }
        }
        return true
    }

    private fun snackbarEvent(message: String, type: SnackBarType) {
        viewModelScope.launch {
            _uiEventsChannel.send(
                SlotSelectionScreenUiEvent.ShowSnackbar(
                    value = message, type = type
                )
            )
        }
    }

    /**
     * Add duration in hours to UTC in seconds
     * */
    private fun Long.plusHours(durationInHours: Int): Long {
        return this + (durationInHours * 60 * 60)
    }

    private fun reloadScreenState() {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true
                )
            }

            slotsRepository.fetchAvailableSlots().onSuccess { slotsDto ->
                val slots = slotsDto.toDomain()
                updateState {
                    copy(
                        isLoading = false,
                        pickupSlots = slots.pickupSlots,
                        dropSlots = slots.dropSlots,
                        commonOffers = slots.commonOffers ?: emptyList(),
                        selectedOfferCode = null,
                        dropDateSelectedId = null,
                        dropTimeSelectedId = null,
                        pickupDateSelectedId = null,
                        pickupTimeSelectedId = null,
                    )
                }

                onEvent(event = SlotSelectionScreenEvent.OnPickupDateSelected(slots.pickupSlots.first { it.isAvailable() }))
                onEvent(event = SlotSelectionScreenEvent.OnDropDateSelect(slots.dropSlots.first { it.isAvailable() }))
            }.onFailure { e ->
                snackbarEvent(
                    message = e.message ?: "Error Fetching Available Slots",
                    type = SnackBarType.ERROR
                )
                e.printStackTrace()
                updateState {
                    copy(isLoading = false)
                }
            }
        }
    }

    private fun updateState(action: SlotSelectionScreenState.() -> SlotSelectionScreenState) {
        _state.update(action)
    }
}