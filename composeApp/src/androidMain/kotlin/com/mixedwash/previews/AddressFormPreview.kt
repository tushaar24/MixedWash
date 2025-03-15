package com.mixedwash.previews

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.mixedwash.core.domain.validation.PinCodeValidationUseCase
import com.mixedwash.features.common.presentation.address.components.AddressForm
import com.mixedwash.features.common.presentation.address.AddressFormState
import com.mixedwash.features.common.presentation.address.FormMode
import com.mixedwash.core.presentation.models.FieldID
import com.mixedwash.core.presentation.models.FormField
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.screenVerticalPadding
import com.mixedwash.ui.theme.MixedWashTheme

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun AddressFormPreview() {
    val baseCreateFormState = AddressFormState(
        title = "Create Address", mode = FormMode.Create(onCreate = { }), fields = listOf(
            com.mixedwash.core.presentation.models.FormField(
                value = "",
                id = com.mixedwash.core.presentation.models.FieldID.ADDRESS_TITLE,
                label = "Address Title",
                placeholder = "Home Address",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = {},
            ), com.mixedwash.core.presentation.models.FormField(
                value = "",
                id = com.mixedwash.core.presentation.models.FieldID.ADDRESS_LINE_1,
                label = "Address Line 1",
                placeholder = "Flat No, House, Building Name",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = {},
            ), com.mixedwash.core.presentation.models.FormField(value = "",
                id = com.mixedwash.core.presentation.models.FieldID.ADDRESS_LINE_2,
                label = "Address Line 2",
                placeholder = "Landmark, Locality",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = {

                }), com.mixedwash.core.presentation.models.FormField(value = "",
                id = com.mixedwash.core.presentation.models.FieldID.ADDRESS_LINE_3,
                label = "Address Line 3",
                placeholder = "City, State",
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                onValueChange = { }), com.mixedwash.core.presentation.models.FormField(
                value = "",
                id = com.mixedwash.core.presentation.models.FieldID.PIN_CODE,
                label = "Pin Code",
                placeholder = "6 digit pin code",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { },
                validationUseCase = PinCodeValidationUseCase
            )
        )
    )

    MixedWashTheme {
        AddressForm(
            modifier = Modifier
                .padding(
                    horizontal = screenHorizontalPadding,
                    vertical = screenVerticalPadding
                )
                .background(BrandTheme.colors.gray.lighter),
            state = baseCreateFormState
        )
    }
}
