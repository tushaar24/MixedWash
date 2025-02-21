package com.mixedwash.features.createOrder.presentation.phone

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mixedwash.domain.validation.PhoneValidationUseCase
import com.mixedwash.presentation.components.fadeIn
import com.mixedwash.presentation.models.FieldID
import com.mixedwash.presentation.models.FormField
import com.mixedwash.presentation.models.InputState
import com.mixedwash.presentation.models.SnackbarHandler
import com.mixedwash.presentation.util.ObserveAsEvents
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.components.DefaultCircularProgressIndicator
import com.mixedwash.ui.theme.components.SecondaryButton
import com.mixedwash.ui.theme.minTextFieldHeight
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.screenVerticalPadding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Composable
fun PhoneScreen(
    modifier: Modifier = Modifier,
    state: PhoneScreenState,
    uiEventsFlow: Flow<PhoneScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    onSubmitSuccess: () -> Unit,
    onEvent: (PhoneScreenEvent) -> Unit
) {

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is PhoneScreenUiEvent.ShowSnackbar -> snackbarHandler(
                snackbarPayload = event.payload
            )

            is PhoneScreenUiEvent.OnSubmitSuccess -> {
                onSubmitSuccess.invoke()
            }
        }
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            DefaultCircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(
                horizontal = screenHorizontalPadding,
                vertical = screenVerticalPadding
            )
    ) {
        Column(
            modifier = Modifier.weight(0.7f).fadeIn(1000),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.title,
                modifier = Modifier.fillMaxWidth(),
                style = BrandTheme.typography.h5
            )

            Text(
                text = state.subtitle,
                modifier = Modifier.fillMaxWidth(),
                style = BrandTheme.typography.body3
            )

            Spacer(modifier = Modifier.height(60.dp))
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = BrandTheme.typography.subtitle1.fontSize,
                        fontWeight = BrandTheme.typography.subtitle1.fontWeight,
                    )

                ) { append("+91") }
                withStyle(
                    style = SpanStyle(
                        fontSize = BrandTheme.typography.subtitle1.fontSize,
                        fontWeight = FontWeight.W400,
                    )
                ) {
                    append("  |  ")
                }
            }
            val field = state.formField.asFieldState
            TextField(
                modifier = Modifier
                    .heightIn(min = minTextFieldHeight)
                    .width(317.dp),
                value = field.value,
                onValueChange = field.onValueChange,
                textStyle = BrandTheme.typography.subtitle1,
                prefix = {
                    Text(text = annotatedString)
                },
                keyboardOptions = field.keyboardOptions,
                readOnly = field.readOnly,
                placeholder = {
                    field.placeholder?.let {
                        Text(
                            text = it,
                            style = BrandTheme.typography.subtitle1,
                        )
                    }
                },
                enabled = field.enabled,
                supportingText = { Text(field.supportingText ?: " ") },
                singleLine = field.singleLine,
                isError = field.isError,
                colors = BrandTheme.colors.textFieldColors(),
                shape = BrandTheme.shapes.textField
            )
            Spacer(modifier = Modifier.height(40.dp))
            SecondaryButton(
                text = state.buttonText,
                modifier = Modifier.width(300.dp),
                onClick = { onEvent(PhoneScreenEvent.OnSubmit) },
                enabled = state.buttonEnabled
            )
        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@Composable
//@Preview(showSystemUi = true, showBackground = true)
fun PreviewPhoneNumberScreen() {
    MixedWashTheme {
        Scaffold { innerPadding ->
            var isLoading by remember {
                mutableStateOf(false)
            }/*
                        LaunchedEffect(Unit) {
                            while (true) {
                                val delay = if(isLoading) 200L else 5000L
                                delay(delay)
                                isLoading = !isLoading
                            }
                        }
            */

            val state = PhoneScreenState(
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
                        keyboardType = KeyboardType.Phone
                    ),
                    placeholder = "Mobile Number",
                    label = "Phone Number",
                    validationUseCase = PhoneValidationUseCase,
                    showValidation = false,
                    showRequired = false,
                    onValueChange = {},
                    requiredMessage = "Phone Number is Required"
                ),
                isLoading = isLoading
            )
            PhoneScreen(
                modifier = Modifier
                    .padding(innerPadding),
                state = state,
                uiEventsFlow = emptyFlow(),
                snackbarHandler = { _ -> },
                onSubmitSuccess = { },
                onEvent = {}
            )
        }
    }
}

