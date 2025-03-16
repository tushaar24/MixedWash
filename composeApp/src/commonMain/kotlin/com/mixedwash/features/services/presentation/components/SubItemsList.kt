package com.mixedwash.features.services.presentation.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.features.services.presentation.ServiceSubItemsListState
import com.mixedwash.features.services.presentation.model.Gender
import com.mixedwash.features.services.presentation.model.ItemPricing
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_female
import mixedwash.composeapp.generated.resources.ic_male

@Composable
fun SubItemsList(
    modifier: Modifier = Modifier,
    state: ServiceSubItemsListState,
    onQuery: (String) -> Unit,
    onItemIncrement: (String) -> Unit,
    onItemDecrement: (String) -> Unit,
    onItemAdd: (String) -> Unit,
    onFilterClick: (Gender) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = state.title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Text(text = state.description, fontSize = 14.sp, fontWeight = FontWeight.Normal)
        DefaultSearchBar(
            query = state.query,
            placeHolder = state.placeHolder,
            onValueChange = onQuery
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ItemsFilterChip(
                leadingIcon = Res.drawable.ic_male,
                text = "men",
                isSelected = state.filters.contains(Gender.MALE),
                onClick = { onFilterClick(Gender.MALE) }
            )
            ItemsFilterChip(
                leadingIcon = Res.drawable.ic_female,
                text = "women",
                isSelected = state.filters.contains(Gender.FEMALE),
                onClick = { onFilterClick(Gender.FEMALE) }
            )
        }

        Spacer(Modifier.height(16.dp))
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Adaptive(104.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 36.dp)
        ) {
            items(state.items, key = {item -> item.itemId}) { item ->
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
        Spacer(Modifier.height(36.dp))
    }
}


