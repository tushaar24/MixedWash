package com.mixedwash.features.common.presentation.address

import BrandTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.DialogPopup
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.components.ElevatedShape
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.address.presentation.AddressFormEvent
import com.mixedwash.features.address.presentation.AddressScreenState
import com.mixedwash.features.address.presentation.AddressScreenUiEvent
import com.mixedwash.features.address.presentation.AddressSearchState
import com.mixedwash.features.address.presentation.components.AddressForm
import com.mixedwash.features.address.presentation.components.AddressList
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.components.PrimaryButton
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.ui.theme.screenHorizontalPadding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(
    modifier: Modifier = Modifier,
    state: AddressScreenState,
    uiEventsFlow: Flow<AddressScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    navController: NavController,
) {

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

            is AddressScreenUiEvent.NavigateOnSubmit -> {
                navController.navigate(event.route) {
                    popUpTo(navController.currentDestination?.route ?: return@navigate) {
                        inclusive = true
                    }
                }
            }

        }
    }

    val listState = rememberLazyListState()

    WindowInsetsContainer {
        Column(
            modifier = modifier.fillMaxSize()
        ) {

            DefaultHeader(
                title = state.title,
                headingSize = HeadingSize.Subtitle1,
                headingAlign = HeadingAlign.Start,
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        onClick = { navController.navigateUp() }
                    )
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .padding(horizontal = screenHorizontalPadding).fillMaxSize()
                ) {

                    Spacer(modifier = Modifier.height(headerContentSpacing))

                    AddressList(
                        modifier = Modifier.fillMaxWidth(),
                        listState = listState,
                        addresses = state.addressList,
                        onAddressClicked = state.typeParams.asSelect()?.onAddressSelected,
                        selectedAddressId = state.typeParams.asSelect()?.selectedId,
                        onAddressEdit = state.onAddressEdit,
                        addressSearchState = state.searchState,
                    )

                    /*
                                    Spacer(modifier = Modifier.height(16.dp))
                                    OutlinedButton(
                                        iconBefore = Icons.Rounded.Add,
                                        text = "Add Address",
                                        modifier = Modifier.fillMaxWidth(),
                                        contentColor = BrandTheme.colors.gray.dark,
                                        outlineColor = BrandTheme.colors.gray.dark,
                                        onClick = state.onAddAddress
                                    )
                    */

                    Spacer(modifier = Modifier.height(40.dp))

                }

                state.typeParams.asSelect()?.run {
                    ElevatedShape(
                        modifier = Modifier.align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        listState = listState
                    ) {
                        PrimaryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = screenHorizontalPadding),
                            text = submitText,
                            onClick = onSubmit,
                            enabled = !selectedId.isNullOrBlank()
                        )
                    }
                }
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


//@Preview(showSystemUi = true, device = Devices.PIXEL)
@Composable
private fun PreviewAddressScreen() {
    MixedWashTheme {

        var selectedId by remember { mutableStateOf("") }
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
                onAddressSelected = { }
            ),
            searchState = AddressSearchState.initialState()
        )

        val state = emptyState.copy(
            title = "Pickup and Delivery Address",
            addressList = dummyAddress,
            typeParams = (emptyState.typeParams as AddressScreenState.TypeParams.Select).copy(
                submitText = "Continue",
                onAddressSelected = { selectedId = it },
            ),
            isLoading = false
        )

    }
}

