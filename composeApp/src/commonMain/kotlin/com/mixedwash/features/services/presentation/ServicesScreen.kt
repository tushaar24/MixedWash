package com.mixedwash.features.services.presentation

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
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.services.presentation.components.ServiceDetail
import com.mixedwash.features.services.presentation.components.ServiceTab
import com.mixedwash.features.services.presentation.components.ServicesFooter
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.windowInsetsContainer
import kotlinx.coroutines.flow.Flow

@Composable
fun ServicesScreen(
    state: ServicesScreenState,
    onEvent: (ServicesScreenEvent) -> Unit,
    uiEventsFlow: Flow<ServicesScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    modifier: Modifier = Modifier,
) {

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            ServicesScreenUiEvent.OpenProcessingDetailsBottomSheet -> {

            }
            is ServicesScreenUiEvent.OpenServiceItemsBottomSheet -> {

            }
            is ServicesScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.payload)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().windowInsetsContainer()) {

        DefaultHeader(title = "Select Services",
            headingSize = HeadingSize.Subtitle1,
            headingAlign = HeadingAlign.Start,
            navigationButton = {
                HeaderIconButton(imageVector = Icons.Rounded.KeyboardArrowLeft, onClick = {})
            }
        )
        Spacer(modifier = Modifier.height(headerContentSpacing))
        Column(
            modifier = modifier
                .fillMaxSize(),
        ) {

            Row(modifier = Modifier.weight(1f)) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    itemsIndexed(state.services) { index, service ->
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
                onProceed = { },
            )
        }
    }
}
