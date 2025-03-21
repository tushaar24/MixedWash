package com.mixedwash.features.services.presentation

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.services.presentation.components.ServiceDetail
import com.mixedwash.features.services.presentation.components.ServiceTab
import com.mixedwash.features.services.presentation.components.ServicesFooter
import com.mixedwash.features.services.presentation.components.SubItemsList
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.windowInsetsContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    state: ServicesScreenState,
    onEvent: (ServicesScreenEvent) -> Unit,
    uiEventsFlow: Flow<ServicesScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val processingDetailsSheetState = rememberModalBottomSheetState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    state.subItemsListState?.let { subItemsListState ->
        ModalBottomSheet(
            onDismissRequest = { onEvent(ServicesScreenEvent.OnClosedSubItemsSheet) },
            sheetState = sheetState,
            dragHandle = {},
            containerColor = BrandTheme.colors.background,
            contentColor = LocalContentColor.current,
            shape = BrandTheme.shapes.bottomSheet
        ) {
            SubItemsList(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 36.dp)
                    .fillMaxHeight(0.85f),
                state = subItemsListState,
                onQuery = { onEvent(ServicesScreenEvent.OnSubItemsQuery(it)) },
                onItemIncrement = { onEvent(ServicesScreenEvent.OnItemIncrement(it)) },
                onItemDecrement = { onEvent(ServicesScreenEvent.OnItemDecrement(it)) },
                onItemAdd = { onEvent(ServicesScreenEvent.OnItemAdd(it)) },
                onFilterClick = { onEvent(ServicesScreenEvent.OnFilterClicked(it)) },
                onClose = { onEvent(ServicesScreenEvent.OnCloseSubItemsSheet) }
            )
        }
    }

    if (processingDetailsSheetState.isVisible && state.selectedServiceId != null) {
        val service = state.services.first { it.serviceId == state.selectedServiceId }
        ModalBottomSheet(
            onDismissRequest = {},
            sheetState = processingDetailsSheetState
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Inclusions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = service.inclusions ?: "none",
                    fontSize = 12.sp,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Exclusions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = service.exclusions,
                    fontSize = 12.sp
                )
            }
        }
    }

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            ServicesScreenUiEvent.OpenProcessingDetailsBottomSheet -> {
                scope.launch {
                    if (!processingDetailsSheetState.isVisible) {
                        processingDetailsSheetState.show()
                    }
                }
            }

            is ServicesScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.payload)
            }

            ServicesScreenUiEvent.CloseSubItemsSheet -> {
                scope.launch {
                    if (sheetState.isVisible) {
                        sheetState.hide()
                    }
                }.invokeOnCompletion {
                    onEvent(ServicesScreenEvent.OnClosedSubItemsSheet)
                }
            }

            is ServicesScreenUiEvent.NavigateToRoute -> {
                navController.navigate(event.route)
            }
        }
    }



    Column(modifier = modifier.fillMaxSize().windowInsetsContainer()) {

        DefaultHeader(title = "Select Services",
            headingSize = HeadingSize.Subtitle1,
            headingAlign = HeadingAlign.Start,
            navigationButton = {
                HeaderIconButton(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    onClick = { navController.navigateUp() }
                )
            }
        )
        Spacer(modifier = Modifier.height(headerContentSpacing))
        Column(
            modifier = modifier
                .fillMaxSize(),
        ) {

            Row(modifier = Modifier.weight(1f)) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    itemsIndexed(state.services) { _, service ->
                        ServiceTab(
                            service = service,
                            addedToCart = state.cartItems.any { it.serviceId == service.serviceId },
                            isSelected = service.serviceId == state.selectedServiceId,
                            onClick = { onEvent(ServicesScreenEvent.OnServiceClick(service.serviceId)) },
                            modifier = Modifier.padding(horizontal = 8.dp),
                        )
                    }
                }

                state.selectedServiceId?.let { serviceId ->
                    val service = state.services.first { service -> service.serviceId == serviceId }
                    ServiceDetail(
                        service = service,
                        onEvent = onEvent,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        serviceCartItems = state.cartItems.filter { it.serviceId == serviceId }
                    )
                }
            }

            ServicesFooter(
                selectedItemsSize = state.cartItems.fold(initial = 0) { acc, cartItem -> acc + cartItem.quantity },
                onProceed = { onEvent(ServicesScreenEvent.OnSubmit) },
                allowProceed = state.cartItems.isNotEmpty()
            )
        }
    }
}
