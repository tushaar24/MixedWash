package com.mixedwash.features.createOrder.presentation.screens

import BrandTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.bottomButtonPadding
import com.mixedwash.features.createOrder.presentation.components.AddressForm
import com.mixedwash.features.createOrder.presentation.models.Address
import com.mixedwash.features.createOrder.presentation.models.AddressFormEvent
import com.mixedwash.features.createOrder.presentation.models.AddressId
import com.mixedwash.features.createOrder.presentation.models.AddressScreenState
import com.mixedwash.features.createOrder.presentation.models.AddressScreenUiEvent
import com.mixedwash.headerContentSpacing
import com.mixedwash.presentation.components.AddressList
import com.mixedwash.presentation.components.DefaultHeader
import com.mixedwash.presentation.components.DialogPopup
import com.mixedwash.presentation.components.DialogPopupData
import com.mixedwash.presentation.components.HeadingAlign
import com.mixedwash.presentation.components.HeadingSize
import com.mixedwash.presentation.models.SnackbarHandler
import com.mixedwash.presentation.util.ObserveAsEvents
import com.mixedwash.screenHorizontalPadding
import com.mixedwash.screenVerticalPadding
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.defaults.HeaderIconButton
import com.mixedwash.ui.theme.defaults.OutlinedButton
import com.mixedwash.ui.theme.defaults.PrimaryButton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    state: AddressScreenState,
    uiEventsFlow: Flow<AddressScreenUiEvent>,
    snackbarHandler: SnackbarHandler
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var dialogPopupData by remember { mutableStateOf<DialogPopupData?>(null) }

    dialogPopupData?.let { DialogPopup(data = it, onDismissRequest = { dialogPopupData = null }) }

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is AddressScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(
                    event.value, event.type, SnackbarDuration.Short
                )
            }

            AddressScreenUiEvent.CloseForm -> {
                scope.launch {
                    if (sheetState.isVisible) {
                        sheetState.hide()
                    }

                }.invokeOnCompletion {
                    state.formEventCallBack(AddressFormEvent.OnFormClosed)
                }
            }

            AddressScreenUiEvent.ClosePopup -> {
                dialogPopupData = null
            }

            is AddressScreenUiEvent.ShowDialogPopup -> {
                dialogPopupData = event.dialogPopupData
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        DefaultHeader(title = state.title,
            headingSize = HeadingSize.Subtitle1,
            headingAlign = HeadingAlign.Start,
            navigationButton = {
                HeaderIconButton(imageVector = Icons.Rounded.KeyboardArrowLeft, onClick = {})
            }
        )
        Column(
            Modifier.padding(horizontal = screenHorizontalPadding)
        ) {

            Spacer(modifier = Modifier.height(headerContentSpacing))
            AddressList(
                modifier = Modifier.fillMaxWidth(),
                addresses = state.addressList,
                onAddressClicked = state.onAddressSelected,
                selectedIndex = state.selectedIndex,
                onAddressEdit = state.onAddressEdit,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                iconBefore = Icons.Rounded.Add,
                text = "Add Address",
                modifier = Modifier.fillMaxWidth(),
                contentColor = BrandTheme.colors.gray.dark,
                outlineColor = BrandTheme.colors.gray.dark,
                onClick = state.onAddAddress
            )

            Spacer(modifier = Modifier.weight(1f))


            PrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = bottomButtonPadding),
                text = state.buttonText,
                onClick = state.submitScreenCallBack
            )

        }
    }

    if (state.formState != null) {

        ModalBottomSheet(
            onDismissRequest = {
                state.formEventCallBack(AddressFormEvent.OnFormClosed)
            },
            sheetState = sheetState,
            shape = BrandTheme.shapes.rectangle,
            dragHandle = {},
            containerColor = BrandTheme.colors.background,
            contentColor = LocalContentColor.current,
            modifier = Modifier.fillMaxWidth()
        ) {
            AddressForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 24.dp, end = 24.dp, bottom = 54.dp, top = 24.dp),
                state = state.formState,
                onCancel = {
                    scope.launch {
                        if (sheetState.isVisible) {
                            sheetState.hide()
                        }
                    }.invokeOnCompletion {
                        state.formEventCallBack(AddressFormEvent.OnFormClosed)
                    }
                }
            )
        }
    }
}


//@Preview(showSystemUi = true, device = Devices.PIXEL)
@Composable
private fun PreviewAddressScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val dummyAddress = listOf(
        Address(
            id = AddressId("alkka"),
            title = "Home",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            id = AddressId("lsasd"),
            title = "Office",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            id = AddressId("lkmk"),
            title = "Dadi",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
    )
    val emptyState = AddressScreenState(
        title = "",
        addressList = listOf(),
        selectedIndex = -1,
        isLoading = true,
        buttonText = "",
        onAddressSelected = {},
        onAddressEdit = {},
        onAddAddress = {},
        submitScreenCallBack = {},
        formEventCallBack = {},
        formState = null
    )

    val state = emptyState.copy(
        title = "Pickup and Delivery Address",
        addressList = dummyAddress,
        buttonText = "Continue",
        onAddressSelected = { selectedIndex = it },
        isLoading = false
    )
    MixedWashTheme {
        AddressScreen(
            modifier = Modifier.padding(vertical = screenVerticalPadding),
            state = state,
            uiEventsFlow = emptyFlow(),
            snackbarHandler = { _, _, _ -> })
    }
}

