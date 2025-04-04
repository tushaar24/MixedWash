package com.mixedwash.features.slot_selection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.features.local_cart.data.model.CartItemEntity
import com.mixedwash.features.local_cart.domain.LocalCartRepository
import com.mixedwash.features.local_cart.domain.model.toDomain
import com.mixedwash.features.slot_selection.domain.model.error.onErrorOrderDraftCreation
import com.mixedwash.features.slot_selection.domain.model.response.DateSlot
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot
import com.mixedwash.features.slot_selection.domain.repository.SlotsRepository
import com.mixedwash.features.slot_selection.domain.usecase.CreateOrderDraftUseCase
import com.mixedwash.features.slot_selection.presentation.model.BookingServiceMetadata
import com.mixedwash.features.slot_selection.presentation.model.BookingSlotState
import com.mixedwash.features.slot_selection.presentation.model.PickupSlotState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SlotSelectionScreenViewModel(
    private val slotsRepository: SlotsRepository,
    private val localCartRepository: LocalCartRepository,
    private val createOrderDraftUseCase: CreateOrderDraftUseCase
) : ViewModel() {

    private val initialState = SlotSelectionScreenState(
        isLoading = true,
        screenTitle = "Book Slots",
        pickupSlotState = PickupSlotState(),
        bookingsSlotStates = emptyList(),
        deliveryNotes = "",
        screenEvent = { onEvent(it) }
    )

    private val _state = MutableStateFlow(initialState)

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), initialState)

    private val _uiEventsChannel = Channel<SlotSelectionScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    init {
        triggerScreenLoad()
        viewModelScope.launch {
        }
    }


    private fun onEvent(event: SlotSelectionScreenEvent) {
        when (event) {
            is SlotSelectionScreenEvent.OnPickupDateSelected -> {
                event.dateSlot.timeSlots.firstOrNull { it.isAvailable }
                    ?.let { firstAvailableTimeSlot ->
                        onEvent(
                            SlotSelectionScreenEvent.OnPickupTimeSelected(
                                event.dateSlot,
                                firstAvailableTimeSlot
                            )
                        )
                    }
            }

            is SlotSelectionScreenEvent.OnPickupTimeSelected -> {
                updateState {
                    copy(
                        pickupSlotState = pickupSlotState.copy(
                            timeSlotSelectedId = event.timeSlot.id,
                            dateSlotSelectedId = event.dateSlot.id
                        ),

                        bookingsSlotStates = bookingsSlotStates.setAvailabilityAndEarliestSelection { startTimeStamp, _, bookingDurationInHrs, _ ->
                            startTimeStamp >= event.timeSlot.startTimeStamp.plusHours(
                                bookingDurationInHrs
                            )
                        }
                    )
                }

            }


            is SlotSelectionScreenEvent.OnBookingDateSelected -> {
                event.dateSlot.timeSlots.firstOrNull { it.isAvailable }
                    ?.let { firstAvailableTimeSlot ->
                        onEvent(
                            SlotSelectionScreenEvent.OnBookingTimeSelected(
                                bookingId = event.bookingId,
                                dateSlot = event.dateSlot,
                                timeSlot = firstAvailableTimeSlot
                            )
                        )
                }
            }

            is SlotSelectionScreenEvent.OnBookingTimeSelected -> {
                updateState {
                    copy(
                        bookingsSlotStates = bookingsSlotStates.map { bookingSlotState ->
                            if (bookingSlotState.id == event.bookingId) {
                                bookingSlotState.copy(
                                    timeSlotSelectedId = event.timeSlot.id,
                                    dateSlotSelectedId = event.dateSlot.id
                                )

                            } else {
                                bookingSlotState
                            }
                        }
                    )
                }
            }


            is SlotSelectionScreenEvent.OnDeliveryNotesUpdate -> {
                if (event.value.length > 400) return
                updateState {
                    copy(
                        deliveryNotes = event.value
                    )
                }
            }

            SlotSelectionScreenEvent.OnSubmit -> {
                if (state.value.isLoading) return
                viewModelScope.launch {
                    if (submitValidation()) {
                        runCatching {
                            updateState {
                                copy(
                                    isLoading = true
                                )
                            }
                            val pickupSlot =
                                state.value.pickupSlotState.timeSlotSelectedId?.let { pickupSlotId ->
                                    findTimeSlotById(
                                        state.value.pickupSlotState.slots,
                                        pickupSlotId
                                    )
                                } ?: run { throw Exception("Time slot not found for pickup") }

                            val cartItems = localCartRepository.getCartItems()
                                .getOrElse { throw (Exception("Failed to retrieve cart items")) }

                            // Convert cart items to domain models and group by service ID
                            val itemsByServiceId = cartItems.map { it.toDomain() }
                                .groupBy { it.serviceId }

                            // Create a map of service ID to selected drop time slot
                            val dropSlotsByServiceId = mutableMapOf<String, TimeSlot>()
                            state.value.bookingsSlotStates.forEach { bookingState ->
                                if (bookingState.timeSlotSelectedId != null) {
                                    val dropSlot = findTimeSlotById(
                                        bookingState.dateSlots,
                                        bookingState.timeSlotSelectedId
                                    )
                                    if (dropSlot != null) {
                                        // Associate this drop slot with all services in this booking group
                                        bookingState.bookingServices.forEach { service ->
                                            dropSlotsByServiceId[service.serviceId] = dropSlot
                                        }
                                    }
                                }
                            }
                            createOrderDraftUseCase(
                                pickupTimeSlot = pickupSlot,
                                cartItemsByServiceId = itemsByServiceId,
                                dropTimeSlotsByServiceId = dropSlotsByServiceId,
                                deliveryNotes = state.value.deliveryNotes
                            ).onErrorOrderDraftCreation(
                                addressNotFound = {
                                    snackbarEvent(
                                        "No address has been selected",
                                        type = SnackBarType.ERROR
                                    )
                                },
                                addressNotServiceable = {
                                    snackbarEvent(
                                        "Selected address is not serviceable",
                                        type = SnackBarType.ERROR
                                    )
                                },
                                emptyCart = {
                                    snackbarEvent(
                                        "Your cart is empty",
                                        type = SnackBarType.ERROR
                                    )
                                },
                                noSlotsSelected = {
                                    snackbarEvent(
                                        "Please select all slots",
                                        type = SnackBarType.WARNING
                                    )
                                },
                                invalidSlots = {
                                    snackbarEvent(
                                        "Invalid slots selected",
                                        type = SnackBarType.WARNING
                                    )
                                },
                                other = { e ->
                                    snackbarEvent(
                                        e.message ?: "An unexpected error occurred",
                                        type = SnackBarType.ERROR
                                    )
                                    e.printStackTrace()
                                }
                            ).onSuccess { _ ->
                                _uiEventsChannel.send(SlotSelectionScreenUiEvent.NavigateToReview)
                            }

                        }.onFailure { e ->
                            snackbarEvent(
                                e.message ?: "Submit Failed: Unexpected Error",
                                SnackBarType.ERROR
                            )
                            e.printStackTrace()
                        }.also {
                            updateState {
                                copy(isLoading = false)
                            }
                        }
                    }
                }
            }

            is SlotSelectionScreenEvent.OnToggleBookingExpanded -> {
                updateState {
                        copy(bookingsSlotStates = bookingsSlotStates.map { booking ->
                            if (booking.id == event.bookingId) {
                                booking.copy(isExpanded = !booking.isExpanded)
                            } else booking
                        }
                    )
                }
            }

            SlotSelectionScreenEvent.OnTogglePickupExpanded -> {
                updateState {
                    copy(pickupSlotState = pickupSlotState.copy(isExpanded = !pickupSlotState.isExpanded))
                }
            }
        }
    }

    /**
     * Updates availability status for each time slot in a list of BookingSlotStates and selects the first valid slot.
     *
     * @param validSlotPredicate Function that determines if a slot is valid/available based on:
     *        - startTimeStamp: The start time of the slot
     *        - endTimeStamp: The end time of the slot
     *        - bookingDurationInHrs: The service duration required for the booking
     *        - isAvailable: The current availability status of the slot
     * @return List of BookingSlotStates with updated availability and earliest valid selection
     */
    private inline fun List<BookingSlotState>.setAvailabilityAndEarliestSelection(
        crossinline validSlotPredicate: (startTimeStamp: Long, endTimeStamp: Long, bookingDurationInHrs: Int, isAvailable: Boolean) -> Boolean
    ): List<BookingSlotState> = map { bookingSlot ->
        var firstValidDateTimeSlotIds: Pair<Int, Int>? = null
        bookingSlot.copy(
            dateSlots = bookingSlot.dateSlots.map { dateSlot ->
                dateSlot.copy(
                    timeSlots = dateSlot.timeSlots.map { timeSlot ->
                        val isValidSlot = validSlotPredicate(
                            timeSlot.startTimeStamp,
                            timeSlot.endTimeStamp,
                            bookingSlot.serviceDurationInHrs,
                            timeSlot.isAvailable
                        )
                        if (isValidSlot && firstValidDateTimeSlotIds == null) firstValidDateTimeSlotIds =
                            dateSlot.id to timeSlot.id
                        timeSlot.copy(
                            isAvailable = isValidSlot
                        )
                    }
                )
            },
            timeSlotSelectedId = firstValidDateTimeSlotIds?.second,
            dateSlotSelectedId = firstValidDateTimeSlotIds?.first
        )
    }

    private fun submitValidation(): Boolean {
        state.value.apply {
            if (pickupSlotState.timeSlotSelectedId == null) {
                snackbarEvent("Please select pickup slot", type = SnackBarType.WARNING)
                return false
            }

            if (bookingsSlotStates.none { it.timeSlotSelectedId != null }) {
                snackbarEvent("No delivery slots selected", type = SnackBarType.WARNING)
                return false
            }

            bookingsSlotStates.filter{it.timeSlotSelectedId != null}.forEach { bookingSlot ->
                val dropTimeSlot =
                    findTimeSlotById(bookingSlot.dateSlots, bookingSlot.timeSlotSelectedId!!)
                val pickupTimeSlot =
                    findTimeSlotById(pickupSlotState.slots, pickupSlotState.timeSlotSelectedId)

                if (dropTimeSlot != null && pickupTimeSlot != null) {
                    if (dropTimeSlot.startTimeStamp < pickupTimeSlot.startTimeStamp.plusHours(
                            bookingSlot.serviceDurationInHrs
                        )
                    ) {
                        val serviceNames =
                            bookingSlot.bookingServices.joinToString(", ") { it.serviceName }
                        snackbarEvent(
                            "Pick and drop slots for $serviceNames must be at least ${bookingSlot.serviceDurationInHrs} hours apart",
                            type = SnackBarType.WARNING
                        )
                        return false
                    }
                }
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
     * Helper function to find a TimeSlot by its ID within a list of DateSlots
     */
    private fun findTimeSlotById(
        dateSlots: List<DateSlot>,
        timeSlotId: Int
    ): TimeSlot? {
        for (dateSlot in dateSlots) {
            for (timeSlot in dateSlot.timeSlots) {
                if (timeSlot.id == timeSlotId) {
                    return timeSlot
                }
            }
        }
        return null
    }

    /**
     * Add duration in hours to seconds
     * */
    private fun Long.plusHours(durationInHours: Int): Long {
        return this + (durationInHours * 60 * 60)
    }

    private fun triggerScreenLoad() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }

            runCatching {
                val cartItems = localCartRepository.getCartItems()
                    .getOrElse { throw Exception("Fetch cart items failed") }
                val slotsResponse = slotsRepository.fetchAvailableSlots()
                    .getOrElse { throw Exception("Fetch cart items failed") }

                val bookingSlots: List<BookingSlotState> =
                    generateBookingSlots(cartItems, slotsResponse.dropSlots).run {
                        val pickupStartTimeStamp =
                            slotsResponse.pickupSlots.firstOrNull()?.timeSlots?.firstOrNull()?.startTimeStamp
                        return@run if (pickupStartTimeStamp != null) {
                            setAvailabilityAndEarliestSelection { startTimeStamp, _, bookingDurationInHrs, _ ->
                                startTimeStamp >= pickupStartTimeStamp.plusHours(
                                    bookingDurationInHrs
                                )
                            }
                        } else this
                    }

                updateState {
                    copy(
                        isLoading = false,
                        pickupSlotState = initialState.pickupSlotState.copy(
                            slots = slotsResponse.pickupSlots,
                            dateSlotSelectedId = slotsResponse.pickupSlots.firstOrNull()?.id,
                            timeSlotSelectedId = slotsResponse.pickupSlots.firstOrNull()?.timeSlots?.firstOrNull()?.id,
                            isExpanded = bookingSlots.size <= 1
                        ),
                        bookingsSlotStates = bookingSlots,
                    )
                }
            }

        }
    }

    private fun generateBookingSlots(
        items: List<CartItemEntity>,
        dropSlots: List<DateSlot>
    ): List<BookingSlotState> = items.map {
        BookingServiceMetadata(
            serviceId = it.serviceId,
            serviceName = it.serviceName,
            imageUrl = it.serviceImageUrl,
            durationInHrs = it.deliveryTimeMaxInHrs ?: it.deliveryTimeMinInHrs
        )
    }.distinctBy {
        it.serviceId
    }.groupBy {
        it.durationInHrs
    }.map { (durationInHours, bookingServiceMetadata) ->
        BookingSlotState(
            bookingServices = bookingServiceMetadata,
            serviceDurationInHrs = durationInHours,
            dateSlots = dropSlots,
            isExpanded = false
        )
    }

    private fun updateState(action: SlotSelectionScreenState.() -> SlotSelectionScreenState) {
        _state.update(action)
    }

}