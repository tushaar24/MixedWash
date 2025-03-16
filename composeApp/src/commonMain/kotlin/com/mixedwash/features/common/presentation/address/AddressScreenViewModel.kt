package com.mixedwash.features.common.presentation.address

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.mixedwash.Route
import com.mixedwash.core.domain.validation.PinCodeValidationUseCase
import com.mixedwash.core.presentation.components.ButtonData
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.models.FieldID
import com.mixedwash.core.presentation.models.FieldID.ADDRESS_LINE_1
import com.mixedwash.core.presentation.models.FieldID.ADDRESS_LINE_2
import com.mixedwash.core.presentation.models.FieldID.ADDRESS_LINE_3
import com.mixedwash.core.presentation.models.FieldID.ADDRESS_TITLE
import com.mixedwash.core.presentation.models.FieldID.PIN_CODE
import com.mixedwash.core.presentation.models.FormField
import com.mixedwash.core.presentation.models.InputState
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.features.common.data.entities.AddressEntity
import com.mixedwash.features.common.data.service.local.LocationService
import com.mixedwash.features.common.domain.usecases.address.AddressUseCases
import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.common.presentation.address.model.toAddress
import com.mixedwash.features.common.presentation.address.model.toAddressEntity
import com.mixedwash.features.common.presentation.address.model.toFieldIDValueMap
import com.mixedwash.libs.loki.autocomplete.AutocompleteResult
import com.mixedwash.libs.loki.core.Place
import com.mixedwash.libs.loki.geocoder.GeocoderResult
import com.mixedwash.libs.loki.geolocation.GeolocatorResult
import com.mixedwash.ui.theme.RedDark
import com.mixedwash.ui.theme.Yellow400
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class AddressScreenViewModel(
    private val addressUseCases: AddressUseCases,
    private val locationService: LocationService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val addressRoute = savedStateHandle.toRoute<Route.AddressRoute>()
    private val addressFlow = addressUseCases.getAddressesFlow().onFailure {
            snackbarEvent(
                "Error fetching addresses", SnackBarType.ERROR
            )
    }.getOrDefault(emptyFlow())


    private val _state = MutableStateFlow(initialState())
    val state = addressFlow.combine(_state) { list : List<AddressEntity>, state: AddressScreenState ->
        updateState { state.copy(addressList = list.map { it.toAddress() }) }
        _state.value
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(3000), initialState())

    private val _uiEventsChannel = Channel<AddressScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    private var autocompleteJob: Job? = null

    private fun onScreenEvent(event: AddressScreenEvent) {
        when (event) {
            is AddressScreenEvent.OnAddAddress -> {
                updateState {
                    copy(formState = emptyCreateFormState.run {
                        event.address?.let {
                            this.copy(
                                fields = formFieldsFrom(
                                    address = event.address,
                                    baseFormFields = this.fields,
                                    excludeEmptyFields = false,
                                )
                            )
                        } ?: this@run
                    })
                }
            }

            is AddressScreenEvent.OnAddressEdit -> {
                updateState {
                    copy(formState = event.address.getFormViewState())
                }
            }

            is AddressScreenEvent.OnAddressSelect -> {
                state.value.typeParams.asSelect()?.let { selectParams ->
                    updateState {
                        copy(
                            typeParams = selectParams.copy(
                                selectedIndex = event.index
                            )
                        )
                    }
                }
            }

            AddressScreenEvent.OnScreenSubmit -> {
                viewModelScope.launch {
                    state.value.typeParams.asSelect()?.let { selectParams ->
                        state.value.run {
                            val uid =
                                addressList[selectParams.selectedIndex].uid
                            addressUseCases.setDefaultAddress(uid)
                        }.onSuccess {
                            _uiEventsChannel.send(AddressScreenUiEvent.NavigateOnSubmit)
                        }.onFailure { e ->
                            snackbarEvent(
                                e.message ?: "Error Setting Default Address", SnackBarType.ERROR
                            )
                            e.printStackTrace()
                        }
                    }
                }
            }

            is AddressScreenEvent.OnFormEvent -> {
                onFormEvent(event.formEvent)
            }

            is AddressScreenEvent.OnAddressDeleteConfirmation -> {
                viewModelScope.launch {
                    val address =
                        state.value.addressList.find { it.uid == event.addressId } ?: run {
                            _uiEventsChannel.send(AddressScreenUiEvent.CloseForm)
                            return@launch
                        }
                    addressUseCases.deleteAddress(address.toAddressEntity()).onFailure { e ->
                        snackbarEvent(e.message ?: "Error Deleting Address", SnackBarType.ERROR)
                        e.printStackTrace()
                    }.getOrNull() ?: return@launch
                    _uiEventsChannel.send(AddressScreenUiEvent.ClosePopup)
                    _uiEventsChannel.send(AddressScreenUiEvent.CloseForm)
                    snackbarEvent("Address deleted successfully", SnackBarType.SUCCESS)
                }
            }

        }
    }

    private fun onFormEvent(event: AddressFormEvent) {
        when (event) {
            is AddressFormEvent.OnFieldUpdate -> {
                val id = event.fieldId
                val value = event.value
                updateState {
                    copy(
                        formState = formState?.copy(fields = formState.fields.map { field ->
                            if (field.id == id) {
                                if (field.validationUseCase != null) {
                                    val result = field.validationUseCase.invoke(value)
                                    if (result.errorMessage != field.errorMessage) return@map field.copy(
                                        errorMessage = result.errorMessage, value = value
                                    )
                                }
                                field.copy(value = value)
                            } else field
                        })
                    )
                }
            }

            is AddressFormEvent.OnFormEditCancel -> {
                val address = state.value.formState!!.mode.let { it as FormMode.Edit }.address
                updateState {
                    copy(
                        formState = address.getFormViewState()
                    )
                }
            }

            is AddressFormEvent.OnFormCreate, is AddressFormEvent.OnFormEditSave -> {
                val address = state.value.formState!!.toAddress()
                if (!formSubmitValidation(newAddress = address)) return
                viewModelScope.launch {
                    updateState { copy(formState = formState?.copy(isLoading = true)) }
                    addressUseCases.upsertAddress(address = address.toAddressEntity())
                        .onFailure { e ->
                            snackbarEvent(
                                e.message ?: "Address Could Not Be Saved",
                                SnackBarType.ERROR
                            )
                            e.printStackTrace()
                        }.getOrNull() ?: return@launch

                    _uiEventsChannel.send(AddressScreenUiEvent.CloseForm)
                    snackbarEvent("Address updated successfully", SnackBarType.SUCCESS)
                    updateState { copy(formState = formState?.copy(isLoading = false)) }
                }
            }

            is AddressFormEvent.OnFormViewDelete -> {
                viewModelScope.launch {
                    val address = state.value.formState!!.mode.let { it as FormMode.View }.address
                    _uiEventsChannel.send(
                        AddressScreenUiEvent.ShowDialogPopup(
                            dialogPopupData = DialogPopupData(
                                title = "Are you sure you wish to delete the Home address?",
                                icon = Icons.Rounded.Warning,
                                iconColor = Yellow400,
                                primaryButton = ButtonData(
                                    text = "Delete",
                                    onClick = {
                                        onScreenEvent(
                                            AddressScreenEvent.OnAddressDeleteConfirmation(
                                                addressId = address.uid
                                            )
                                        )

                                    },
                                    iconBefore = Icons.Rounded.Delete,
                                    contentColor = RedDark,
                                    containerColor = RedDark
                                ),
                                secondaryButton = ButtonData(text = "Cancel", onClick = {
                                    viewModelScope.launch {
                                        _uiEventsChannel.send(
                                            AddressScreenUiEvent.ClosePopup
                                        )
                                    }

                                }),
                                onDismissRequest = {
                                    viewModelScope.launch {
                                        _uiEventsChannel.send(
                                            AddressScreenUiEvent.ClosePopup
                                        )
                                    }
                                }
                            )
                        )
                    )
                }
            }

            is AddressFormEvent.OnFormViewEdit -> {
                val address = state.value.formState!!.mode.let { it as FormMode.View }.address
                updateState {
                    copy(
                        formState = formState?.copy(
                            title = "Edit Address", fields = formFieldsFrom(
                                address = address,
                                baseFormFields = emptyCreateFormState.fields,
                                excludeFields = emptyList(),
                                inputState = _root_ide_package_.com.mixedwash.core.presentation.models.InputState.Enabled,
                                excludeEmptyFields = false
                            ), mode = FormMode.Edit(address = address, onSave = {

                                formEventCallBack(
                                    AddressFormEvent.OnFormEditSave
                                )
                            }, onCancel = {
                                formEventCallBack(AddressFormEvent.OnFormEditCancel)
                            })
                        )
                    )
                }
            }

            is AddressFormEvent.OnFormClosed -> {
                updateState {
                    copy(
                        formState = null
                    )
                }
            }
        }
    }

    private fun onSearchEvent(event: AddressSearchEvent) {
        when (event) {
            AddressSearchEvent.OnClear -> {
                onSearchEvent(AddressSearchEvent.OnValueChange(""))
            }

            AddressSearchEvent.OnLocationClick -> viewModelScope.launch {
                if (!locationService.isLocationEnabled()) {
                    snackbarEvent(
                        message = "Please turn on device location",
                        type = SnackBarType.WARNING
                    )
                    return@launch
                }

                updateSearchState {
                    copy(
                        fetchingLocation = true,
                        enabled = false
                    )
                }

                // Open Add Address Modal
                getCurrentAddressOrNull()?.let {
                    onScreenEvent(
                        AddressScreenEvent.OnAddAddress(it)
                    )
                }

                updateSearchState {
                    copy(
                        fetchingLocation = false,
                        enabled = true
                    )
                }
            }


            is AddressSearchEvent.OnPlaceSelected -> {

                viewModelScope.launch {
                    updateSearchState {
                        copy(
                            fetchingLocation = true
                        )
                    }
                    onSearchEvent(AddressSearchEvent.OnClear)

                    val result = locationService.getPlaceByPlaceId(event.place.placeId)

                    if (result !is GeocoderResult.Success) {
                        snackbarEvent(
                            message = "Error Fetching Location",
                            type = SnackBarType.ERROR
                        )
                    } else {
                        onScreenEvent(
                            AddressScreenEvent.OnAddAddress(result.data.first().toAddress())
                        )
                    }

                    updateSearchState {
                        copy(
                            fetchingLocation = false
                        )
                    }
                }

            }

            is AddressSearchEvent.OnValueChange -> {
                autocompleteJob?.cancel()
                if (event.value.isBlank()) {
                    updateSearchState {
                        copy(
                            query = event.value,
                            autocompleteResult = emptyList(),
                        )
                    }
                    return
                }
                updateSearchState {
                    copy(
                        query = event.value,
                    )
                }
                autocompleteJob = viewModelScope.launch {
                    delay(200)
                    val result = locationService.searchAutoComplete(event.value)
                    if (result !is AutocompleteResult.Success) {

                    } else {
                        updateSearchState {
                            copy(
                                autocompleteResult = result.data.map { it }
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun getCurrentAddressOrNull(): Address? {

        val result = locationService.getCurrentLocation()
        val address: Address? = if (result !is GeolocatorResult.Success) {
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
            null
        } else {
            val placeResult =
                locationService.getPlaceByCoordinates(result.data.coordinates)
            val addressResult: Address? =
                if (placeResult !is GeocoderResult.Success<Place> || placeResult.data.isEmpty()) {
                    snackbarEvent("Fetching location failed", SnackBarType.ERROR)
                    null
                } else {
                    placeResult.data.first().toAddress()
                }
            addressResult
        }
        return address
    }


    private suspend fun addressObserver() {
        addressFlow.collect { addressList ->
            updateState { copy(addressList = addressList.map { it.toAddress() }) }
        }
    }


    private fun updateSearchState(action: AddressSearchState.() -> AddressSearchState) {
        updateState {
            copy(searchState = searchState.action())
        }
    }

    private fun updateState(action: AddressScreenState.() -> AddressScreenState) {
        _state.update(action)
    }


    private fun Address.getFormViewState(): AddressFormState {
        return emptyCreateFormState.copy(
            title = title,
            mode = FormMode.View(
                onDelete = { onFormEvent(AddressFormEvent.OnFormViewDelete) },
                onEdit = { onFormEvent(AddressFormEvent.OnFormViewEdit) },
                address = this,
            ),
            isLoading = false,
            fields = formFieldsFrom(
                address = this,
                baseFormFields = emptyCreateFormState.fields,
                excludeFields = listOf(ADDRESS_TITLE),
                inputState = _root_ide_package_.com.mixedwash.core.presentation.models.InputState.ReadOnly,
                excludeEmptyFields = true
            ),
        )
    }

    /**
     * Handles form validation before submit.
     * */
    private fun formSubmitValidation(newAddress: Address): Boolean {
        val currFormState = state.value.formState!!

        if (currFormState.fields.any { it.asFieldState.isError }) return false

        if (requiredFields.all { fieldId -> currFormState.fields.find { it.id == fieldId }!!.value.isNotBlank() }) {

            if (currFormState.mode is FormMode.Edit && newAddress == currFormState.mode.address) {
                snackbarEvent("Address Not Changed", SnackBarType.WARNING)
                return false
            }

            return true

        } else {
            updateState {
                copy(formState = formState!!.copy(fields = formState.fields.map { field ->
                    field.copy(
                        showRequired = requiredFields.contains(field.id), showValidation = true
                    )
                }))
            }
            return false
        }

    }


    /**
     * Generates a list of [FormField] from [Address] using a base list of [FormField]
     * @param address Address to generate fields from
     * @param baseFormFields Base list of [FormField] to generate fields from
     * @param excludeFields List of [FieldID] to exclude from the generated fields
     * @param inputState [InputState] to apply to the generated fields
     *
     * */
    private fun formFieldsFrom(
        address: Address,
        baseFormFields: List<_root_ide_package_.com.mixedwash.core.presentation.models.FormField>,
        excludeFields: List<_root_ide_package_.com.mixedwash.core.presentation.models.FieldID> = emptyList(),
        excludeEmptyFields: Boolean,
        inputState: _root_ide_package_.com.mixedwash.core.presentation.models.InputState = _root_ide_package_.com.mixedwash.core.presentation.models.InputState.Enabled,
    ): List<_root_ide_package_.com.mixedwash.core.presentation.models.FormField> {
        val fieldIdValueMap = address.toFieldIDValueMap()
        return baseFormFields.mapNotNull { field ->
            if (excludeFields.contains(field.id) && field.id == ADDRESS_TITLE) return@mapNotNull null
            val value = fieldIdValueMap[field.id] ?: return@mapNotNull null
            if (value.isBlank() && excludeEmptyFields) return@mapNotNull null

            field.copy(value = value, inputState = inputState)
        }
    }


    @OptIn(ExperimentalUuidApi::class)
    private fun AddressFormState.toAddress(): Address {
        return Address(
            uid = when (mode) {
                is FormMode.Create -> Uuid.random().toHexString() // Generate random Uuid
                is FormMode.Edit -> mode.address.uid
                is FormMode.View -> mode.address.uid
            },
            title = fields.find { it.id == ADDRESS_TITLE }!!.value,
            addressLine1 = fields.find { it.id == ADDRESS_LINE_1 }!!.value,
            addressLine2 = fields.find { it.id == ADDRESS_LINE_2 }!!.value,
            addressLine3 = fields.find { it.id == ADDRESS_LINE_3 }!!.value,
            pinCode = fields.find { it.id == PIN_CODE }!!.value
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
                AddressScreenUiEvent.ShowSnackbar(
                    snackbarPayload = SnackbarPayload(
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

    private val requiredFields = listOf(ADDRESS_LINE_1, ADDRESS_TITLE, PIN_CODE)

    private fun initialState(): AddressScreenState {
        val screenType = when (addressRoute.screenType) {
            Route.AddressRoute.ScreenType.Edit -> AddressScreenState.TypeParams.Edit
            Route.AddressRoute.ScreenType.SelectAddress -> AddressScreenState.TypeParams.Select(
                onSubmit = { onScreenEvent(AddressScreenEvent.OnScreenSubmit) },
                submitText = addressRoute.submitText ?: "Submit",
                selectedIndex = -1,
                onAddressSelected = { onScreenEvent(AddressScreenEvent.OnAddressSelect(it)) }
            )
        }
        return AddressScreenState(
            title = addressRoute.title,
            addressList = emptyList(),
            isLoading = true,
            onAddressEdit = { onScreenEvent(AddressScreenEvent.OnAddressEdit(it)) },
            onAddAddress = { onScreenEvent(AddressScreenEvent.OnAddAddress()) },
            formEventCallBack = { onScreenEvent(AddressScreenEvent.OnFormEvent(it)) },
            formState = null,
            typeParams = screenType,
            searchState = AddressSearchState.initialState().copy(
                onEvent = { searchEvent -> onSearchEvent(searchEvent) }
            )
        )
    }


    private val emptyCreateFormState = AddressFormState(
        title = "Create Address",
        mode = FormMode.Create(onCreate = { onFormEvent(AddressFormEvent.OnFormCreate) }),
        fields = listOf(
            _root_ide_package_.com.mixedwash.core.presentation.models.FormField(
                value = "",
                id = ADDRESS_TITLE,
                label = "Address Title ${if (requiredFields.contains(ADDRESS_TITLE)) "*" else ""}",
                placeholder = "Home Address",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = {
                    onFormEvent(
                        AddressFormEvent.OnFieldUpdate(
                            fieldId = ADDRESS_TITLE, value = it
                        )
                    )
                },
            ), _root_ide_package_.com.mixedwash.core.presentation.models.FormField(
                value = "",
                id = ADDRESS_LINE_1,
                label = "Address Line 1${if (requiredFields.contains(ADDRESS_LINE_1)) "*" else ""}",
                placeholder = "Flat No, House, Building Name",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = {
                    onFormEvent(
                        AddressFormEvent.OnFieldUpdate(
                            fieldId = ADDRESS_LINE_1, value = it
                        )
                    )
                },
            ), _root_ide_package_.com.mixedwash.core.presentation.models.FormField(value = "",
                id = ADDRESS_LINE_2,
                label = "Address Line 2${if (requiredFields.contains(ADDRESS_LINE_2)) "*" else ""}",
                placeholder = "Landmark, Locality",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = {
                    onFormEvent(
                        AddressFormEvent.OnFieldUpdate(
                            fieldId = ADDRESS_LINE_2, value = it
                        )
                    )
                }), _root_ide_package_.com.mixedwash.core.presentation.models.FormField(value = "",
                id = ADDRESS_LINE_3,
                label = "Address Line 3${if (requiredFields.contains(ADDRESS_LINE_3)) "*" else ""}",
                placeholder = "City, State",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = {
                    onFormEvent(
                        AddressFormEvent.OnFieldUpdate(
                            fieldId = ADDRESS_LINE_3, value = it
                        )
                    )
                }), _root_ide_package_.com.mixedwash.core.presentation.models.FormField(
                value = "",
                id = PIN_CODE,
                label = "Pin Code${if (requiredFields.contains(PIN_CODE)) "*" else ""}",
                placeholder = "6 digit pin code",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    onFormEvent(
                        AddressFormEvent.OnFieldUpdate(
                            fieldId = PIN_CODE, value = it
                        )
                    )
                },
                validationUseCase = PinCodeValidationUseCase
            )
        )
    )
}