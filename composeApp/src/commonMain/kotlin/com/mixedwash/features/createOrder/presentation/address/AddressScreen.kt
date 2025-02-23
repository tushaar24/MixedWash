package com.mixedwash.features.createOrder.presentation.address

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
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.features.createOrder.presentation.address.components.AddressForm
import com.mixedwash.features.createOrder.presentation.address.components.AddressList
import com.mixedwash.features.createOrder.presentation.address.model.Address
import com.mixedwash.presentation.components.DefaultHeader
import com.mixedwash.presentation.components.DialogPopup
import com.mixedwash.presentation.components.DialogPopupData
import com.mixedwash.presentation.components.HeadingAlign
import com.mixedwash.presentation.components.HeadingSize
import com.mixedwash.presentation.models.SnackbarHandler
import com.mixedwash.presentation.util.ObserveAsEvents
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.bottomButtonPadding
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.components.OutlinedButton
import com.mixedwash.ui.theme.components.PrimaryButton
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.screenVerticalPadding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    state: AddressScreenState,
    uiEventsFlow: Flow<AddressScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    onSubmitNavigate: (() -> Unit)? = null
) {
    state.typeParams.asSelect()
        ?.let { requireNotNull(onSubmitNavigate) { "onSubmitNavigate cannot be null when typeParam is SelectAddress" } }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var dialogPopupData by remember { mutableStateOf<DialogPopupData?>(null) }

    dialogPopupData?.let { DialogPopup(data = it, onDismissRequest = { dialogPopupData = null }) }

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is AddressScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(
                    snackbarPayload = event.snackbarPayload
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

            AddressScreenUiEvent.NavigateOnSubmit -> {
                onSubmitNavigate!!.invoke()
            }

        }
    }

    WindowInsetsContainer {
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
                    onAddressClicked = state.typeParams.asSelect()?.onAddressSelected,
                    selectedIndex = state.typeParams.asSelect()?.selectedIndex ?: -1,
                    onAddressEdit = state.onAddressEdit,
                    addressSearchState = state.searchState,
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


                state.typeParams.asSelect()?.run {
                    PrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = bottomButtonPadding),
                        text = submitText,
                        onClick = onSubmit,
                        enabled = selectedIndex != -1
                    )
                }

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
}



//@Preview(showSystemUi = true, device = Devices.PIXEL)
@Composable
private fun PreviewAddressScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val dummyAddress = listOf(
        Address(
            uid = "alkka",
            title = "Home",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            uid = "lsasd",
            title = "Office",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
        Address(
            uid = "lkmk",
            title = "Dadi",
            addressLine1 = "2342, Electronic City Phase 2",
            addressLine3 = "Silicon Town, Bengaluru",
            pinCode = "560100"
        ),
    )
    val emptyState = AddressScreenState(
        title = "",
        addressList = listOf(),
        isLoading = true,
        onAddressEdit = {},
        onAddAddress = {},
        formEventCallBack = {},
        formState = null,
        typeParams = AddressScreenState.TypeParams.Select(
            onSubmit = { },
            submitText = "Save",
            selectedIndex = 0,
            onAddressSelected = { }
        ),
        searchState = AddressSearchState.initialState()
    )

    val state = emptyState.copy(
        title = "Pickup and Delivery Address",
        addressList = dummyAddress,
        typeParams = (emptyState.typeParams as AddressScreenState.TypeParams.Select).copy(
            submitText = "Continue",
            onAddressSelected = { selectedIndex = it },
        ),
        isLoading = false
    )
    MixedWashTheme {
        AddressScreen(
            modifier = Modifier.padding(vertical = screenVerticalPadding),
            state = state,
            uiEventsFlow = emptyFlow(),
            snackbarHandler = { _ -> })
    }
}

