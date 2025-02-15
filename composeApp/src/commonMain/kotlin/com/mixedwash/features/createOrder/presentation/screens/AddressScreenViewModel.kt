package com.mixedwash.features.createOrder.presentation.screens

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.domain.models.Resource
import com.mixedwash.domain.validation.PinCodeValidationUseCase
import com.mixedwash.features.createOrder.domain.DeleteAddressUseCase
import com.mixedwash.features.createOrder.domain.GetAddressScreenUseCase
import com.mixedwash.features.createOrder.domain.SelectAddressUseCase
import com.mixedwash.features.createOrder.domain.UpsertAddressUseCase
import com.mixedwash.features.createOrder.presentation.models.Address
import com.mixedwash.features.createOrder.presentation.models.AddressFormEvent
import com.mixedwash.features.createOrder.presentation.models.AddressFormState
import com.mixedwash.features.createOrder.presentation.models.AddressId
import com.mixedwash.features.createOrder.presentation.models.AddressScreenEvent
import com.mixedwash.features.createOrder.presentation.models.AddressScreenState
import com.mixedwash.features.createOrder.presentation.models.AddressScreenUiEvent
import com.mixedwash.features.createOrder.presentation.models.FormMode
import com.mixedwash.features.createOrder.presentation.models.toAddressEntity
import com.mixedwash.features.createOrder.presentation.models.toFieldIDValueMap
import com.mixedwash.presentation.components.ButtonData
import com.mixedwash.presentation.components.DialogPopupData
import com.mixedwash.presentation.models.FieldID
import com.mixedwash.presentation.models.FieldID.ADDRESS_LINE_1
import com.mixedwash.presentation.models.FieldID.ADDRESS_LINE_2
import com.mixedwash.presentation.models.FieldID.ADDRESS_LINE_3
import com.mixedwash.presentation.models.FieldID.ADDRESS_TITLE
import com.mixedwash.presentation.models.FieldID.PIN_CODE
import com.mixedwash.presentation.models.FormField
import com.mixedwash.presentation.models.InputState
import com.mixedwash.presentation.models.SnackBarType
import com.mixedwash.ui.theme.RedDark
import com.mixedwash.ui.theme.Yellow400
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AddressScreenViewModel constructor(
    private val getAddressScreenUseCase: GetAddressScreenUseCase,
    private val upsertAddressUseCase: UpsertAddressUseCase,
    private val selectAddressUseCase: SelectAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(initialState())
    val state = _state.onStart {
        loadScreenState()
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialState())

    private val _uiEventsChannel = Channel<AddressScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    private fun onScreenEvent(event: AddressScreenEvent) {
        when (event) {
            AddressScreenEvent.OnAddAddress -> {
                updateState {
                    copy(formState = baseCreateFormState)
                }
            }

            is AddressScreenEvent.OnAddressEdit -> {
                updateState {
                    copy(formState = event.address.getFormViewState())
                }
            }

            is AddressScreenEvent.OnAddressSelect -> {
                updateState {
                    copy(
                        selectedIndex = event.index
                    )
                }
            }

            AddressScreenEvent.OnScreenSubmit -> {
                viewModelScope.launch {
                    val result =
                        selectAddressUseCase(state.value.addressList[state.value.selectedIndex].id)
                    when (result) {
                        is Resource.Error -> snackbarEvent(
                            result.error.toString(), SnackBarType.ERROR
                        )

                        is Resource.Success -> snackbarEvent(
                            "Selection Successful", SnackBarType.SUCCESS
                        )
                    }
                }

            }

            is AddressScreenEvent.OnFormEvent -> {
                onFormEvent(event.formEvent)
            }

            is AddressScreenEvent.OnAddressDeleteConfirmation -> {
                viewModelScope.launch {
                    val result = deleteAddressUseCase(
                        state.value.addressList[state.value.selectedIndex].toAddressEntity()
                    )
                    _uiEventsChannel.send(AddressScreenUiEvent.ClosePopup)

                    if (result is Resource.Error) {
                        snackbarEvent(result.error.toString(), SnackBarType.ERROR)
                    } else {
                        _uiEventsChannel.send(AddressScreenUiEvent.CloseForm)
                        snackbarEvent("Address has been deleted successfully", SnackBarType.SUCCESS)
                    }
                }
            }

        }
    }

    // TODO : AddressFormState should have separate sub-states for collapsed, view or edit mode. :)
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
                    val result = upsertAddressUseCase(addressEntity = address.toAddressEntity())
                    if (result is Resource.Error) {
                        snackbarEvent(result.error.toString(), SnackBarType.ERROR)
                    } else {
                        _uiEventsChannel.send(AddressScreenUiEvent.CloseForm)
                        snackbarEvent("Address has been updated successfully", SnackBarType.SUCCESS)
                    }
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
                                                addressId = address.id.value!!
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

                                })
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
                                baseFormFields = baseCreateFormState.fields,
                                excludeFields = emptyList(),
                                inputState = InputState.Enabled,
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


    private fun loadScreenState() {
        val response = getAddressScreenUseCase()
        updateState {
            copy(
                title = response.title,
                addressList = response.addresses.map { it.toAddress() },
                selectedIndex = response.selectedIndex,
                buttonText = response.buttonText,
                isLoading = false,
            )
        }
    }

    private fun updateState(action: AddressScreenState.() -> AddressScreenState) {
        _state.update(action)
    }

    private fun Address.getFormViewState(): AddressFormState {
        return baseCreateFormState.copy(
            title = title,
            mode = FormMode.View(
                onDelete = { onFormEvent(AddressFormEvent.OnFormViewDelete) },
                onEdit = { onFormEvent(AddressFormEvent.OnFormViewEdit) },
                address = this,
            ),
            isLoading = false,
            fields = formFieldsFrom(
                address = this,
                baseFormFields = baseCreateFormState.fields,
                excludeFields = listOf(ADDRESS_TITLE),
                inputState = InputState.ReadOnly,
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
        baseFormFields: List<FormField>,
        excludeFields: List<FieldID> = emptyList(),
        excludeEmptyFields: Boolean,
        inputState: InputState = InputState.Enabled,
    ): List<FormField> {
        val fieldIdValueMap = address.toFieldIDValueMap()
        return baseFormFields.mapNotNull { field ->
            if (excludeFields.contains(field.id) && field.id == ADDRESS_TITLE) return@mapNotNull null
            val value = fieldIdValueMap[field.id] ?: return@mapNotNull null
            if (value.isBlank() && excludeEmptyFields) return@mapNotNull null

            field.copy(value = value, inputState = inputState)
        }
    }


    private fun AddressFormState.toAddress(): Address {
        return Address(
            id = when (mode) {
                is FormMode.Create -> AddressId(null)
                is FormMode.Edit -> mode.address.id
                is FormMode.View -> mode.address.id
            },
            title = fields.find { it.id == ADDRESS_TITLE }!!.value,
            addressLine1 = fields.find { it.id == ADDRESS_LINE_1 }!!.value,
            addressLine2 = fields.find { it.id == ADDRESS_LINE_2 }!!.value,
            addressLine3 = fields.find { it.id == ADDRESS_LINE_3 }!!.value,
            pinCode = fields.find { it.id == PIN_CODE }!!.value
        )
    }

    private fun snackbarEvent(message: String, type: SnackBarType) {
        viewModelScope.launch {
            _uiEventsChannel.send(
                AddressScreenUiEvent.ShowSnackbar(
                    value = message, type = type
                )
            )
        }
    }

    private val requiredFields = listOf(ADDRESS_LINE_1, ADDRESS_TITLE, PIN_CODE)
    private fun initialState() = AddressScreenState(
        title = "",
        addressList = listOf(),
        selectedIndex = -1,
        isLoading = true,
        buttonText = "",
        onAddressSelected = { onScreenEvent(AddressScreenEvent.OnAddressSelect(it)) },
        onAddressEdit = { onScreenEvent(AddressScreenEvent.OnAddressEdit(it)) },
        onAddAddress = { onScreenEvent(AddressScreenEvent.OnAddAddress) },
        submitScreenCallBack = { onScreenEvent(AddressScreenEvent.OnScreenSubmit) },
        formEventCallBack = { onScreenEvent(AddressScreenEvent.OnFormEvent(it)) },
        formState = null
    )


    private val baseCreateFormState = AddressFormState(
        title = "Create Address",
        mode = FormMode.Create(onCreate = { onFormEvent(AddressFormEvent.OnFormCreate) }),
        fields = listOf(
            FormField(
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
            ), FormField(
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
            ), FormField(value = "",
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
                }), FormField(value = "",
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
                }), FormField(
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