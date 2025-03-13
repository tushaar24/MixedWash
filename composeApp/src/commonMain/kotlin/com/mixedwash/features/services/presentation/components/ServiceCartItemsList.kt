package com.mixedwash.features.services.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.features.local_cart.presentation.model.CartItemPresentation
import com.mixedwash.features.services.presentation.ServicesScreenEvent
import com.mixedwash.features.services.presentation.model.PricingMetadataPresentation
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.dividerBlack

@Composable
fun ServiceCartItemsList(
    serviceCartItems: List<CartItemPresentation>,
    onEvent: (ServicesScreenEvent) -> Unit,
    service: ServicePresentation
) {
    HorizontalDivider(
        color = dividerBlack,
        thickness = 1.dp,
        modifier = Modifier.padding(top = 8.dp)
    )


    LazyColumn(
        modifier = Modifier.fillMaxWidth().heightIn(min = 36.dp, max = 2000.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (serviceCartItems.isNotEmpty()) {
            items(serviceCartItems, key = { item -> item.itemId }) { item ->
                CartItemEntry(
                    modifier = Modifier.animateItem(),
                    name = item.name,
                    quantity = item.quantity,
                    pricing = item.itemPricing,
                    onIncrement = { onEvent(ServicesScreenEvent.OnItemIncrement(item.itemId)) },
                    onDecrement = { onEvent(ServicesScreenEvent.OnItemDecrement(item.itemId)) },
                    onAdd = { onEvent(ServicesScreenEvent.OnItemAdd(item.itemId)) }
                )
            }
        }

        if (serviceCartItems.isEmpty() ||
            service.pricingMetadata is PricingMetadataPresentation.SubItemsPricingPresentation
        ) {
            service.pricingMetadata?.let { pricingMetadata ->
                item {
                    AddItemButton(onClick = {
                        when (pricingMetadata) {
                            is PricingMetadataPresentation.ServicePricingPresentation -> {
                                onEvent(
                                    ServicesScreenEvent.OnItemAdd(
                                        itemId = service.items?.first()?.itemId ?: ""
                                    )
                                )
                            }

                            is PricingMetadataPresentation.SubItemsPricingPresentation -> {
                                onEvent(
                                    ServicesScreenEvent.OnOpenSubItemsSheet(
                                        serviceId = service.serviceId
                                    )
                                )
                            }
                        }
                    })
                }
            }
        }
    }

    HorizontalDivider(
        color = dividerBlack,
        thickness = 1.dp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
