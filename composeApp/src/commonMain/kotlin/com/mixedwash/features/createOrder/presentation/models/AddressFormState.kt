package com.mixedwash.features.createOrder.presentation.models

import com.mixedwash.presentation.models.FormField

data class AddressFormState(
    val title: String,
    val fields: List<FormField>,
    val mode: FormMode,
    val isLoading: Boolean = false
)

sealed class FormMode {
    data class Create(val onCreate: () -> Unit) : FormMode()
    data class View(val address: Address, val onEdit: () -> Unit, val onDelete: () -> Unit) :
        FormMode()
    data class Edit(val address: Address, val onSave: () -> Unit, val onCancel: () -> Unit) :
        FormMode()
}


