package com.mixedwash.features.common.presentation.services

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
import com.mixedwash.features.common.presentation.services.components.ServiceDetailsCard
import com.mixedwash.features.common.presentation.services.components.ServiceTab
import com.mixedwash.features.common.presentation.services.components.ServicesFooter

@Composable
fun ServicesScreen(
    state: ServicesScreenState,
    onEvent: (ServicesScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
            .statusBarsPadding().navigationBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(state.serviceItems) { index, item ->
                    ServiceTab(
                        serviceItem = item,
                        isOpted = state.selectedServices.contains(item.id),
                        isCurrent = index == state.currentServiceId,
                        onClick = { onEvent(ServicesScreenEvent.ToggleCurrentService(index)) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }

            ServiceDetailsCard(
                serviceItem = state.serviceItems[state.currentServiceId],
                onEvent = onEvent,
                currentVariant = state.currentVariant,
                minCartPrice = state.minCartPrice,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                serviceVariants = state.serviceItems[state.currentServiceId].variants,
                isOpted = state.selectedServices.contains(state.currentServiceId)
            )
        }

        ServicesFooter(
            selectedItemsSize = state.selectedServices.size,
            price = state.totalCost,
            onProceed = {},
        )
    }
}
