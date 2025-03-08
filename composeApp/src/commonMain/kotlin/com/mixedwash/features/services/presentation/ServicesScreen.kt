package com.mixedwash.features.services.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.features.services.presentation.components.ServiceDetail
import com.mixedwash.features.services.presentation.components.ServiceTab
import com.mixedwash.features.services.presentation.components.ServicesFooter

@Composable
fun ServicesScreen(
    state: ServicesScreenState,
    onEvent: (ServicesScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(state.services) { index, service ->
                    ServiceTab(
                        service = service,
                        addedToCart = state.cartItems.any { it.serviceId == service.serviceId },
                        isSelected = service.serviceId == state.selectedServiceId,
                        onClick = { onEvent(ServicesScreenEvent.OnServiceClick(service.serviceId)) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }

            state.selectedServiceId?.let { serviceId ->
                val service = state.services.first { service -> service.serviceId == serviceId }
                ServiceDetail(
                    service = service,
                    onEvent = onEvent,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    isItemAdded = state.cartItems.any { it.serviceId == serviceId }
                )
            }
        }

        ServicesFooter(
            selectedItemsSize = state.cartItems.fold(initial = 0) { acc, cartItem -> acc + cartItem.quantity },
            onProceed = { },
        )
    }
}
