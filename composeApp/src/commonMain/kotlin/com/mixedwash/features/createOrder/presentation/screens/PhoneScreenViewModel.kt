package com.mixedwash.features.createOrder.presentation.screens

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.domain.models.Resource
import com.mixedwash.domain.validation.PhoneValidationUseCase
import com.mixedwash.features.createOrder.domain.FindUserByPhoneUseCase
import com.mixedwash.features.createOrder.presentation.models.PhoneScreenEvent
import com.mixedwash.features.createOrder.presentation.models.PhoneScreenState
import com.mixedwash.features.createOrder.presentation.models.PhoneScreenUiEvent
import com.mixedwash.presentation.models.FieldID
import com.mixedwash.presentation.models.FormField
import com.mixedwash.presentation.models.InputState
import com.mixedwash.presentation.models.SnackBarType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PhoneScreenViewModel  constructor(
    val findUserByPhoneUseCase: FindUserByPhoneUseCase
) : ViewModel() {
    private val initialState = PhoneScreenState(
        title = "Welcome 👋",
        subtitle = "Log-in to begin doing your laundry!",
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
        onSubmit = { onEvent(PhoneScreenEvent.OnSubmit) },
        isLoading = false
    )

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<PhoneScreenState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<PhoneScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    private fun onEvent(event: PhoneScreenEvent) {
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
                    updateState {
                        copy(
                            formField = formField.copy(inputState = InputState.ReadOnly),
                            buttonEnabled = false
                        )
                    }
                    when (val result = findUserByPhoneUseCase.invoke(state.value.formField.value)) {
                        is Resource.Error -> {
                            _uiEventsChannel.send(
                                PhoneScreenUiEvent.ShowSnackbar(
                                    result.error.toString(),
                                    SnackBarType.ERROR
                                )
                            )
                        }

                        is Resource.Success -> {
                            _uiEventsChannel.send(
                                PhoneScreenUiEvent.ShowSnackbar(
                                    "Successful",
                                    SnackBarType.SUCCESS
                                )
                            )
                        }
                    }
                    updateState {
                        copy(
                            formField = formField.copy(inputState = InputState.Enabled),
                            buttonEnabled = true
                        )
                    }
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

    private fun updateState(action: PhoneScreenState.() -> PhoneScreenState) {
        _state.update(action)
    }
}