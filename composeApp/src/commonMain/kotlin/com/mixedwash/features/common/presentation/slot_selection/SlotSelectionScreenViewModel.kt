package com.mixedwash.features.common.presentation.slot_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.domain.models.Result
import com.mixedwash.features.common.domain.usecases.slots.LoadSlotsWithOffersUseCase
import com.mixedwash.features.common.domain.usecases.slots.SelectSlotAndOffersUseCase
import com.mixedwash.features.common.data.entities.SlotAndOfferSelectionRequestEntity
import com.mixedwash.presentation.models.SnackBarType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SlotSelectionScreenViewModel  constructor(
    private val loadSlotsWithOffersUseCase: LoadSlotsWithOffersUseCase,
    private val selectSlotAndOffersUseCase: SelectSlotAndOffersUseCase,
) : ViewModel() {

    private val initialState = SlotSelectionScreenState(
        isLoading = true,
        title = "Book your slots",
        pickupSlots = emptyList(),
        dropSlots = emptyList(),
        commonOffers = emptyList(),
        screenEvent = { onEvent(it) }
    )

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
                updateState {
                    copy(
                        pickupDateSelectedId = state.value.pickupSlots.first { dateSlot -> dateSlot.timeSlots.any { it.id == event.timeSlot.id } }.id,
                        pickupTimeSelectedId = event.timeSlot.id,
                        dropTimeSelectedId = dropTimeSelectedId?.let {
                            if (getTimeSlotById(it)!!.startTimeStamp >
                                event.timeSlot.endTimeStamp.plusHours(24)
                            ) {
                                dropTimeSelectedId
                            } else {
                                null
                            }
                        },
                        dropSlots = dropSlots.map { dateSlot ->
                            dateSlot.copy(
                                timeSlots = dateSlot.timeSlots.map { timeSlot ->
                                    timeSlot.copy(
                                        isAvailable =
                                        timeSlot.startTimeStamp > event.timeSlot.endTimeStamp.plusHours(
                                            24
                                        ),
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

            SlotSelectionScreenEvent.OnSubmit -> {
                viewModelScope.launch {
                    if (!submitValidation()) return@launch
                    selectSlotAndOffersUseCase(
                        SlotAndOfferSelectionRequestEntity(
                            pickupSlot = getTimeSlotById(state.value.pickupTimeSelectedId!!)!!,
                            dropSlot = getTimeSlotById(state.value.dropTimeSelectedId!!)!!,
                            offer = state.value.selectedOfferCode,
                            deliveryNotes = state.value.deliveryNotes
                        )
                    )
                }
            }

            is SlotSelectionScreenEvent.OnDeliveryNotesChange -> {
                if (event.value.length > 400) return
                updateState {
                    copy(
                        deliveryNotes = event.value
                    )
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
                getTimeSlotById(dropTimeSelectedId)!!.startTimeStamp > getTimeSlotById(
                    pickupTimeSelectedId
                )!!.endTimeStamp.plusHours(24)
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
    private fun Long.plusHours(durationInHours: Long): Long {
        return this + durationInHours * 60 * 60
    }

    private fun reloadScreenState() {
        viewModelScope.launch {
            updateState {
                copy(
                    isLoading = true
                )
            }
            when (val result = loadSlotsWithOffersUseCase.invoke()) {
                is Result.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            pickupSlots = result.data.pickupSlots,
                            dropSlots = result.data.dropSlots,
                            commonOffers = result.data.commonOffers,
                            selectedOfferCode = null,
                            dropDateSelectedId = null,
                            dropTimeSelectedId = null,
                            pickupDateSelectedId = null,
                            pickupTimeSelectedId = null,
                        )
                    }

                    onEvent(event = SlotSelectionScreenEvent.OnPickupDateSelected(result.data.pickupSlots.first { it.isAvailable() }))
                    onEvent(event = SlotSelectionScreenEvent.OnDropDateSelect(result.data.dropSlots.first { it.isAvailable() }))

                }

                is Result.Error -> {
                    snackbarEvent(
                        message = result.error.toString(),
                        type = SnackBarType.ERROR
                    )
                    updateState {
                        copy(isLoading = false)
                    }
                }
            }
        }
    }

    private fun updateState(action: SlotSelectionScreenState.() -> SlotSelectionScreenState) {
        _state.update(action)
    }

}