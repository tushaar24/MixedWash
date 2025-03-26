package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.features.services.presentation.model.ItemPricing

@Composable
fun CartItemEntry(
    modifier: Modifier = Modifier,
    name: String,
    quantity: Int,
    pricing: ItemPricing,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onAdd: () -> Unit
) {

    val description = when (pricing) {
        is ItemPricing.ServiceItemPricingPresentation -> {
            "1${pricing.unit} • ₹${pricing.pricePerUnit.div(100)}"
        }

        is ItemPricing.SubItemFixedPricingPresentation -> {
            "1pc • ₹${pricing.fixedPrice.div(100)}"
        }

        is ItemPricing.SubItemRangedPricingPresentation -> {
            "1pc • ₹${pricing.minPrice.div(100)} - ₹${pricing.maxPrice.div(100)}"
        }
    }

    val itemPrice = when (pricing) {
        is ItemPricing.ServiceItemPricingPresentation -> {
            ""
        }

        is ItemPricing.SubItemFixedPricingPresentation -> {
            "₹${pricing.fixedPrice.div(100) * quantity}"
        }

        is ItemPricing.SubItemRangedPricingPresentation -> {
            "₹${pricing.minPrice.div(100) * quantity} - ₹${pricing.maxPrice.div(100) * quantity}"
        }
    }



    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp)
            Text(
                text = description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = BrandTheme.colors.gray.normalDark,
                lineHeight = 16.sp
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End
        ) {
            ItemQuantityChip(
                isServiceItem = pricing is ItemPricing.ServiceItemPricingPresentation,
                quantity = quantity,
                showAddLabel = true,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                onAdd = onAdd,
            )
            Text(
                text = itemPrice,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = BrandTheme.colors.gray.dark
            )
        }
    }
}
