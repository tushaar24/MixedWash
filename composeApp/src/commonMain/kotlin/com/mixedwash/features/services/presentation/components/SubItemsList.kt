package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mixedwash.core.presentation.components.dropShadow
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.services.presentation.ServiceSubItemsListState
import com.mixedwash.features.services.presentation.model.Gender
import com.mixedwash.features.services.presentation.model.ItemPricing
import com.mixedwash.ui.theme.components.IconButton


@Composable
fun SubItemsList(
    modifier: Modifier = Modifier,
    state: ServiceSubItemsListState,
    onQuery: (String) -> Unit,
    onClose: () -> Unit,
    onItemIncrement: (String) -> Unit,
    onItemDecrement: (String) -> Unit,
    onItemAdd: (String) -> Unit,
    onFilterClick: (Gender?) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = state.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            IconButton(
                iconSize = 20.dp,
                imageVector = Icons.Rounded.Close,
                onClick = onClose,
                buttonColors = BrandTheme.colors.iconButtonColors()
                    .copy(containerColor = Color.Transparent)
            )

        }
        Text(text = state.description, fontSize = 14.sp, fontWeight = FontWeight.Normal)
        DefaultSearchBar(
            query = state.query,
            placeHolder = state.placeHolder,
            onValueChange = onQuery
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ItemsFilterChip(
                text = "all",
                isSelected = state.genderFilter == null,
                onClick = { onFilterClick(null) }
            )
            ItemsFilterChip(
                text = "men",
                isSelected = state.genderFilter == Gender.MALE,
                onClick = { onFilterClick(Gender.MALE) }
            )
            ItemsFilterChip(
                text = "women",
                isSelected = state.genderFilter == Gender.FEMALE,
                onClick = { onFilterClick(Gender.FEMALE) }
            )
        }
        val lazyGridState = rememberLazyGridState()

        val showElevation by remember(lazyGridState) {
            derivedStateOf { lazyGridState.firstVisibleItemScrollOffset != 0 }
        }
        val elevation by animateDpAsState(
            if (showElevation) 4.dp else 0.dp,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )

        Column {
            Spacer(
                Modifier.height(8.dp).fillMaxWidth().dropShadow(
                    shape = RectangleShape,
                    offsetY = elevation,
                    color = Color.Black.copy(0.02f),
                    spread = 0.dp,
                    blur = elevation
                ).background(BrandTheme.colors.background).zIndex(1f)
            )
            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Adaptive(104.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 36.dp)
            ) {
                items(state.items, key = { item -> item.itemId }) { item ->
                    val pricingString = item.itemPricing.run {
                        when (this) {
                            is ItemPricing.ServiceItemPricingPresentation -> "₹${
                                this.minimumPrice.div(
                                    100
                                )
                            } (₹${this.pricePerUnit.div(100)}/${this.unit})"

                            is ItemPricing.SubItemFixedPricingPresentation -> "₹${
                                this.fixedPrice.div(
                                    100
                                )
                            }"

                            is ItemPricing.SubItemRangedPricingPresentation -> "₹${
                                this.minPrice.div(
                                    100
                                )
                            } - ₹${this.maxPrice.div(100)}"
                        }
                    }
                    ServiceSubItem(
                        modifier = Modifier.animateItem(),
                        imageUrl = item.metadata?.imageUrl ?: "",
                        title = item.name,
                        pricing = pricingString,
                        quantity = item.quantity,
                        onIncrement = { onItemIncrement(item.itemId) },
                        onDecrement = { onItemDecrement(item.itemId) },
                        onAdd = { onItemAdd(item.itemId) }
                    )
                }
            }
        }
        Spacer(Modifier.height(36.dp))
    }
}


