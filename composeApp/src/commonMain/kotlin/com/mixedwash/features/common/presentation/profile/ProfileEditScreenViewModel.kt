package com.mixedwash.features.common.presentation.profile

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.data.UserMetadata
import com.mixedwash.core.data.UserService
import com.mixedwash.core.domain.models.Result
import com.mixedwash.core.domain.validation.EmailValidationUseCase
import com.mixedwash.core.domain.validation.PhoneValidationUseCase
import com.mixedwash.core.presentation.components.ButtonData
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.models.FieldID.EMAIL
import com.mixedwash.core.presentation.models.FieldID.NAME
import com.mixedwash.core.presentation.models.FieldID.PHONE
import com.mixedwash.core.presentation.models.FormField
import com.mixedwash.core.presentation.models.SnackBarType
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.ui.theme.RedDark
import com.mixedwash.ui.theme.Yellow400
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileEditScreenViewModel(
    private val userService: UserService
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditScreenState.initialState())
    val state = _state.onStart {
        refreshDetails()
    }.stateIn(viewModelScope, WhileSubscribed(5000), ProfileEditScreenState.initialState())

    private val _uiEventsChannel = Channel<ProfileEditScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()


    private val currentUser
        get() = userService.currentUser

    fun onEvent(event: ProfileEditScreenEvent) {
        when (event) {
            ProfileEditScreenEvent.OnBackClicked -> {
                if (hasChanges()) {
                    viewModelScope.launch {
                        _uiEventsChannel.send(
                            ProfileEditScreenUiEvent.ShowPopup(
                                dialogPopupData = DialogPopupData(
                                    title = "You have unsaved changes",
                                    icon = Icons.Rounded.Warning,
                                    iconColor = Yellow400,
                                    primaryButton = ButtonData(
                                        text = "Cancel",
                                        onClick = {
                                            Logger.d("TAG", "CANCEL CLICKED")
                                            viewModelScope.launch {
                                                Logger.d(
                                                    "TAG",
                                                    "CANCEL CLICKED IN VIEW-MODEL-SCOPE"
                                                )
                                                _uiEventsChannel.send(
                                                    ProfileEditScreenUiEvent.ClosePopup
                                                )
                                            }
                                        },
                                    ),
                                    secondaryButton = ButtonData(
                                        text = "Leave", onClick = {
                                            viewModelScope.launch {
                                                _uiEventsChannel.send(
                                                    ProfileEditScreenUiEvent.OnNavigateBack
                                                )
                                            }
                                        },
                                        contentColor = RedDark,
                                        containerColor = RedDark
                                    ),
                                    onDismissRequest = {
                                        viewModelScope.launch {
                                            _uiEventsChannel.send(
                                                ProfileEditScreenUiEvent.ClosePopup
                                            )
                                        }
                                    }
                                )
                            )
                        )
                    }
                } else {
                    viewModelScope.launch { _uiEventsChannel.send(ProfileEditScreenUiEvent.OnNavigateBack) }
                }
            }

            is ProfileEditScreenEvent.OnChangePicture -> {
                viewModelScope.launch {
                    val result = userService.updateMetadata(photoUrl = event.url)
                    if (result is Result.Success) {
                        updateState { copy(imageUrl = event.url) }
                    } else {
                        snackbarEvent("Error updating photo url", SnackBarType.ERROR)
                    }
                }
            }

            is ProfileEditScreenEvent.OnFieldUpdate -> {
                val id = event.fieldId
                val value = event.value
                updateState {
                    copy(
                        fields = fields.map { field ->
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

                }
            }

            ProfileEditScreenEvent.OnSave -> {
                val valid = state.value.fields.all { it.errorMessage == null }
                if (!valid) {
                    snackbarEvent("Please enter valid details", SnackBarType.ERROR)
                    return
                }

                if (!hasChanges()) {
                    snackbarEvent("You haven't made any changes", SnackBarType.DEFAULT)
                    return
                }

                val metadata = state.value.toUserMetadata()
                if (metadata == null) {
                    snackbarEvent("Couldn't fetch metadata from details", type = SnackBarType.ERROR)
                    return
                }

                viewModelScope.launch {
                    updateState { copy(isLoading = true) }
                    if (userService.updateMetadata(
                            name = metadata.name,
                            email = metadata.email,
                            phoneNumber = metadata.phoneNumber
                        ) !is Result.Error
                    ) {
                        snackbarEvent("Profile has been updated successfully", SnackBarType.SUCCESS)
                        refreshDetails()
                    } else {
                        snackbarEvent("Profile update failed", SnackBarType.ERROR)
                    }
                    updateState { copy(isLoading = false) }
                }
            }
        }
    }


    private fun ProfileEditScreenState.toUserMetadata(): UserMetadata? {
        return currentUser?.userMetadata?.copy(
            name = fields.find { it.id == NAME }?.value,
            email = fields.find { it.id == EMAIL }?.value,
            phoneNumber = fields.find { it.id == PHONE }?.value,
        )
    }

    private fun hasChanges(): Boolean {
        // compute a simple has of name email phone and photo url
        return state.value.toUserMetadata()?.hashCode() != currentUser?.userMetadata?.hashCode()
    }

    private fun updateState(action: ProfileEditScreenState.() -> ProfileEditScreenState) {
        _state.update { it.action() }
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
                ProfileEditScreenUiEvent.ShowSnackbar(
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


    private fun refreshDetails() {
        val metadata = currentUser?.userMetadata
        updateState {
            copy(
                imageUrl = metadata?.photoUrl,
                fields = emptyFieldS().map { field ->
                    val value = when (field.id) {
                        NAME -> metadata?.name
                        EMAIL -> metadata?.email
                        PHONE -> metadata?.phoneNumber
                        else -> null
                    }
                    field.copy(value = value ?: "")
                }
            )
        }
    }

    private fun emptyFieldS(): List<_root_ide_package_.com.mixedwash.core.presentation.models.FormField> = listOf(
        _root_ide_package_.com.mixedwash.core.presentation.models.FormField(
            value = "",
            id = NAME,
            label = "Name",
            placeholder = "John Watson",
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            onValueChange = {
                onEvent(
                    ProfileEditScreenEvent.OnFieldUpdate(
                        fieldId = NAME, value = it
                    )
                )
            },
        ),
        _root_ide_package_.com.mixedwash.core.presentation.models.FormField(
            value = "",
            id = PHONE,
            label = "Phone Number",
            placeholder = "10 digit phone number",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            validationUseCase = PhoneValidationUseCase,
            showValidation = true,
            onValueChange = {
                onEvent(
                    ProfileEditScreenEvent.OnFieldUpdate(
                        fieldId = PHONE,
                        value = it
                    )
                )
            }
        ),
        _root_ide_package_.com.mixedwash.core.presentation.models.FormField(
            value = "",
            id = EMAIL,
            label = "Email",
            placeholder = "shinysheets@mixedwashcustomer.com",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            validationUseCase = EmailValidationUseCase,
            showValidation = true,
            onValueChange = {
                onEvent(
                    ProfileEditScreenEvent.OnFieldUpdate(
                        fieldId = EMAIL,
                        value = it
                    )
                )
            },
        )
    )


}