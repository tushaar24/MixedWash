package com.mixedwash.features.profile_edit

import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.models.SnackbarPayload

data class ProfileEditScreenState(
    val imageUrl: String?,
    val saveEnabled: Boolean = true,
    val fields: List<_root_ide_package_.com.mixedwash.core.presentation.models.FormField>,
    val isLoading: Boolean = false
) {
    companion object {
        fun initialState() = ProfileEditScreenState(
            imageUrl = null,
            saveEnabled = false,
            fields = emptyList()
        )
    }
}

sealed interface ProfileEditScreenEvent {
    data object OnBackClicked : ProfileEditScreenEvent
    data class OnChangePicture(val url: String) : ProfileEditScreenEvent
    data object OnSave : ProfileEditScreenEvent
    data class OnFieldUpdate(val fieldId: _root_ide_package_.com.mixedwash.core.presentation.models.FieldID, val value: String) :
        ProfileEditScreenEvent
}

sealed interface ProfileEditScreenUiEvent {
    data object OnNavigateBack : ProfileEditScreenUiEvent
    data class ShowPopup(val dialogPopupData: DialogPopupData) : ProfileEditScreenUiEvent
    data object ClosePopup : ProfileEditScreenUiEvent
    data class ShowSnackbar(val snackbarPayload: SnackbarPayload) : ProfileEditScreenUiEvent
}
