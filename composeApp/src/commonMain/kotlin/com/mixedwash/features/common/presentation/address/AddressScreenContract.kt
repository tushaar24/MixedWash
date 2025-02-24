package com.mixedwash.features.common.presentation.address

import androidx.compose.runtime.Immutable
import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.presentation.components.DialogPopupData
import com.mixedwash.presentation.models.FieldID
import com.mixedwash.presentation.models.FormField
import com.mixedwash.presentation.models.SnackbarPayload
import com.mixedwash.services.loki.autocomplete.AutocompletePlace


@Immutable
data class AddressScreenState(
    val title: String,
    val addressList: List<Address>,
    val isLoading: Boolean,
    val onAddressEdit: (Address) -> Unit,
    val onAddAddress: () -> Unit,
    val formEventCallBack: (AddressFormEvent) -> Unit,
    val formState: AddressFormState?,
    val searchState :AddressSearchState,
    val typeParams: TypeParams
){
    sealed class TypeParams {
        data object Edit : TypeParams()
        data class Select (
            val onSubmit : () -> Unit,
            val submitText : String,
            val selectedIndex: Int,
            val onAddressSelected : (index: Int) -> Unit
        ) : TypeParams()

        fun asSelect() = if(this is Select) this else null
    }
}

data class AddressFormState(
    val title: String,
    val fields: List<FormField>,
    val mode: FormMode,
    val isLoading: Boolean = false
)

data class AddressSearchState (
    val query: String,
    val placeHolder: String,
    val enabled: Boolean,
    val autocompleteResult: List<AutocompletePlace>,
    val fetchingLocation : Boolean,
    val onEvent: (AddressSearchEvent) -> Unit
)  {
    companion object {
        fun initialState() = AddressSearchState(
            query = "",
            placeHolder = "Enter your area, street",
            enabled = true,
            autocompleteResult = emptyList(),
            fetchingLocation = false,
            onEvent = {},
        )
    }
}

sealed class FormMode {
    data class Create(val onCreate: () -> Unit) : FormMode()
    data class View(val address: Address, val onEdit: () -> Unit, val onDelete: () -> Unit) :
        FormMode()
    data class Edit(val address: Address, val onSave: () -> Unit, val onCancel: () -> Unit) :
        FormMode()
}




sealed class AddressScreenEvent {
    data class OnAddressSelect(val index: Int) : AddressScreenEvent()
    data class OnAddressEdit(val address: Address) : AddressScreenEvent()
    data class OnAddAddress(val address : Address? = null) : AddressScreenEvent()
    data object OnScreenSubmit : AddressScreenEvent()
    data class OnFormEvent(val formEvent: AddressFormEvent) : AddressScreenEvent()
    data class OnAddressDeleteConfirmation(val addressId: String) : AddressScreenEvent()
}

sealed class AddressFormEvent {
    data class OnFieldUpdate(val fieldId: FieldID, val value: String) : AddressFormEvent()
    data object OnFormEditSave : AddressFormEvent()
    data object OnFormEditCancel : AddressFormEvent()
    data object OnFormCreate : AddressFormEvent()
    data object OnFormViewDelete : AddressFormEvent()
    data object OnFormViewEdit : AddressFormEvent()
    data object OnFormClosed : AddressFormEvent()
}

sealed class AddressSearchEvent {
    data class OnValueChange(val value : String) : AddressSearchEvent()
    data object OnClear : AddressSearchEvent()
    data object OnLocationClick : AddressSearchEvent()
    data class OnPlaceSelected(val place: AutocompletePlace) : AddressSearchEvent()
}



sealed class AddressScreenUiEvent {
    data class ShowSnackbar(val snackbarPayload: SnackbarPayload) : AddressScreenUiEvent()
    data object CloseForm : AddressScreenUiEvent()
    data class ShowDialogPopup(val dialogPopupData: DialogPopupData) : AddressScreenUiEvent()
    data object ClosePopup : AddressScreenUiEvent()
    data object NavigateOnSubmit : AddressScreenUiEvent()
}

