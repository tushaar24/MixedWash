package com.mixedwash.features.common.presentation.phone

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.data.UserService
import com.mixedwash.core.domain.models.Result
import com.mixedwash.core.domain.validation.PhoneValidationUseCase
import com.mixedwash.core.presentation.models.FieldID
import com.mixedwash.core.presentation.models.FormField
import com.mixedwash.core.presentation.models.InputState
import com.mixedwash.core.presentation.models.SnackbarPayload
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PhoneScreenViewModel(
    private val userService: UserService,
    userAuthService: UserService
) : ViewModel() {

    private val initialState = PhoneScreenState(
        title = "Hi ${userService.currentUser?.userMetadata?.name ?: ""} 👋",
        subtitle = "Enter your phone number to get started",
        phoneNumber = "",
        phoneNumberError = null,
        buttonText = "Next",
        buttonEnabled = true,
        formField = FormField(
            value = "",
            id = FieldID.PHONE,
            inputState = InputState.Enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Go
            ),
            placeholder = "Mobile Number",
            label = "Phone Number",
            validationUseCase = PhoneValidationUseCase,
            showValidation = false,
            showRequired = false,
            onValueChange = { onEvent(PhoneScreenEvent.UpdatePhone(it)) },
            requiredMessage = "Phone Number is Required"
        ),
        isLoading = false
    )

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<PhoneScreenState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<PhoneScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    fun onEvent(event: PhoneScreenEvent) {
        when (event) {
            PhoneScreenEvent.OnSubmit -> {
                // enabled showValidation and showRequired
                state.value.formField.apply {
                    if (!showValidation || !showRequired) {
                        updateState {
                            copy(
                                formField = formField.copy(
                                    showValidation = true,
                                    showRequired = true
                                )
                            )
                        }
                    }
                }

                if (state.value.formField.asFieldState.isError) return

                viewModelScope.launch {
                    allowFormInput(false)
                    val result =
                        userService.updateMetadata {
                            it.copy(
                                phoneNumber = state.value.formField.value,
                                defaultAddressId = null
                            )
                        }
                    when (result) {
                        is Result.Success -> {
                            sendUiEvent(PhoneScreenUiEvent.OnSubmitSuccess)
                        }

                        is Result.Error -> snackbarEvent(SnackbarPayload("Updating Phone Number Failed"))
                    }
                    allowFormInput(true)
                }
            }

            is PhoneScreenEvent.UpdatePhone -> {
                updateState {
                    val errorMessage =
                        if (formField.validationUseCase != null) formField.validationUseCase.invoke(
                            event.value
                        ).errorMessage else null
                    copy(
                        formField = formField.copy(
                            value = event.value,
                            errorMessage = errorMessage
                        )
                    )
                }
            }

        }
    }

    private fun allowFormInput(enable: Boolean) = updateState {
        copy(
            formField = formField.copy(inputState = if (enable) _root_ide_package_.com.mixedwash.core.presentation.models.InputState.ReadOnly else _root_ide_package_.com.mixedwash.core.presentation.models.InputState.Enabled),
            buttonEnabled = enable
        )

    }

    private fun sendUiEvent(event: PhoneScreenUiEvent) {
        viewModelScope.launch {
            _uiEventsChannel.send(event)
        }
    }

    private fun snackbarEvent(payload: SnackbarPayload) {
        viewModelScope.launch {
            _uiEventsChannel.send(
                PhoneScreenUiEvent.ShowSnackbar(
                    payload
                )
            )
        }

    }

    private fun updateState(action: PhoneScreenState.() -> PhoneScreenState) {
        _state.update(action)
    }
}