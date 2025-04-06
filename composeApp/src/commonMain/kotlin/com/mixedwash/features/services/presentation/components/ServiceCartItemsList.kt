package com.mixedwash.features.services.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.features.local_cart.domain.model.CartItem
import com.mixedwash.features.services.presentation.ServicesScreenEvent
import com.mixedwash.features.services.presentation.model.ItemPricing
import com.mixedwash.features.services.presentation.model.PricingMetadataPresentation
import com.mixedwash.features.services.presentation.model.ServicePresentation
import com.mixedwash.ui.theme.dividerBlack

@Composable
fun ServiceCartItemsList(
    serviceCartItems: List<CartItem>,
    onEvent: (ServicesScreenEvent) -> Unit,
    service: ServicePresentation
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HorizontalDivider(
            color = dividerBlack,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 0.dp, max = 2000.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(serviceCartItems, key = { item -> item.itemId }) { item ->
                CartItemEntry(
                    modifier = Modifier.animateItem(),
                    name = item.name,
                    quantity = item.quantity,
                    pricing = item.itemPricing,
                    onIncrement = { onEvent(ServicesScreenEvent.OnItemIncrement(item.itemId)) },
                    onDecrement = { onEvent(ServicesScreenEvent.OnItemDecrement(item.itemId)) },
                    onAdd = { onEvent(ServicesScreenEvent.OnItemAdd(item.itemId)) },
                    minPrice = if (item.itemPricing is ItemPricing.ServiceItemPricingPresentation) item.itemPricing.minimumPrice else null
                )
            }
        }

// Use AnimatedContent for smooth transitions
        AnimatedContent(
            targetState = serviceCartItems.isEmpty() || service.pricingMetadata is PricingMetadataPresentation.SubItemsPricingPresentation,
            transitionSpec = {
                if (targetState) {

                    // When Add button should appear
                    (slideInVertically(animationSpec = tween(durationMillis = 300)) { height -> height } +
                            fadeIn(animationSpec = tween(durationMillis = 300))) togetherWith
                            (slideOutVertically(animationSpec = tween(durationMillis = 300)) { height -> -height } +
                                    fadeOut(animationSpec = tween(durationMillis = 300)))
                } else {
                    // When Add button should disappear
                    (slideInVertically(animationSpec = tween(durationMillis = 300)) { height -> -height } +
                            fadeIn(animationSpec = tween(durationMillis = 300))) togetherWith
                            (slideOutVertically(animationSpec = tween(durationMillis = 300)) { height -> height } +
                                    fadeOut(animationSpec = tween(durationMillis = 300)))
                } using SizeTransform(clip = false)
            },
        ) { showAddButton ->
            if (showAddButton) {
                service.pricingMetadata?.let { pricingMetadata ->
                    AddItemButton(
                        onClick = {
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
                        },
                        subItems = service.pricingMetadata is PricingMetadataPresentation.SubItemsPricingPresentation
                    )
                }
            }
        }

        HorizontalDivider(
            color = dividerBlack,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}
