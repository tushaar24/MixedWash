package com.mixedwash.features.common.presentation.address.components

import BrandTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.features.common.presentation.address.AddressFormState
import com.mixedwash.features.common.presentation.address.FormMode
import com.mixedwash.presentation.components.clearFocusOnKeyboardDismiss
import com.mixedwash.ui.theme.smallPadding
import com.mixedwash.ui.theme.RedDark
import com.mixedwash.ui.theme.components.IconButton
import com.mixedwash.ui.theme.components.OutlinedButton
import com.mixedwash.ui.theme.components.SecondaryButton

@Composable
fun AddressForm(
    modifier: Modifier, state: AddressFormState, onCancel: (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = state.title, style = BrandTheme.typography.subtitle1)
            onCancel?.let {
                IconButton(
                    iconSize = 20.dp,
                    imageVector = Icons.Rounded.Close,
                    onClick = it,
                    buttonColors = BrandTheme.colors.iconButtonColors()
                        .copy(containerColor = Color.Transparent)
                )
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            state.fields.forEach { formField ->
                val field = formField.asFieldState

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clearFocusOnKeyboardDismiss(),
                    value = field.value,
                    onValueChange = field.onValueChange,
                    label = {
                        Text(
                            text = field.label,
                            style = BrandTheme.typography.body4.copy(
                                color = Color.Unspecified,
                                fontSize = 12.sp
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    },
                    placeholder = {
                        field.placeholder?.let {
                            Text(
                                text = it,
                                style = BrandTheme.typography.body4.copy(color = Color.Unspecified)
                            )
                        }
                    },
                    singleLine = field.singleLine,
                    colors = BrandTheme.colors.textFieldColors().copy(),
                    readOnly = field.readOnly,
                    shape = BrandTheme.shapes.textField,
                    supportingText = { field.supportingText?.let { Text(it) } },
                    keyboardOptions = field.keyboardOptions,
                    enabled = field.enabled && !state.isLoading,
                    isError = field.isError,
                    textStyle = BrandTheme.typography.body4.copy(
                        fontSize = 15.sp,
                        color = BrandTheme.colors.gray.darker
                    )
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                smallPadding, alignment = Alignment.End
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
        ) {
            val isSubmitEnabled by remember { derivedStateOf { state.fields.all { !it.asFieldState.isError } } }
            when (state.mode) {
                is FormMode.Create -> {
                    SecondaryButton(
                        text = "Create",
                        onClick = state.mode.onCreate,
                        enabled = isSubmitEnabled && !state.isLoading
                    )
                }

                is FormMode.View -> {
                    OutlinedButton(
                        text = "Delete",
                        iconBefore = Icons.Rounded.Delete,
                        contentColor = RedDark,
                        outlineColor = RedDark,
                        onClick = state.mode.onDelete
                    )
                    OutlinedButton(
                        text = "Edit", iconBefore = Icons.Rounded.Edit, onClick = state.mode.onEdit
                    )
                }

                is FormMode.Edit -> {
                    OutlinedButton(
                        text = "Cancel", onClick = state.mode.onCancel, enabled = !state.isLoading
                    )
                    SecondaryButton(
                        text = "Save",
                        onClick = state.mode.onSave,
                        enabled = isSubmitEnabled && !state.isLoading
                    )
                }

            }
        }


    }
}

