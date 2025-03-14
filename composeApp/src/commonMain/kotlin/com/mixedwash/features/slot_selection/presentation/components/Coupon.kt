package com.mixedwash.features.slot_selection.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.MixedWashTheme

@Composable
fun Coupon(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector = Icons.Rounded.ShoppingCart
) {
    val containerColor = if (selected) Green else Color.Transparent
    val strokeColor =
        if (selected) Green else BrandTheme.colors.gray.normal

    val contentColor = if (selected) BrandTheme.colors.gray.light else BrandTheme.colors.gray.dark

    val iconColor = if (selected) BrandTheme.colors.gray.light else Green

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = BrandTheme.shapes.card)
            .border(
                border = BorderStroke(0.5.dp, strokeColor), shape = BrandTheme.shapes.card
            )
            .background(containerColor)
            .clickable(enabled = !selected, onClick = onClick)
            .padding(
                start = 16.dp, end = 16.dp, top = 18.dp, bottom = 18.dp
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Coupon Icon",
            modifier = Modifier.size(34.dp),
            tint = iconColor
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
            modifier = Modifier.weight(weight = 1f)
        ) {
            Text(
                text = title,
                style = BrandTheme.typography.subtitle3,
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = BrandTheme.typography.caption1.copy(lineHeight = 1.em),
                color = contentColor
            )
        }
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.size(14.dp),
            colors = RadioButtonColors(
                selectedColor = iconColor,
                unselectedColor = BrandTheme.colors.gray.normal,
                disabledSelectedColor = BrandTheme.colors.gray.light,
                disabledUnselectedColor = BrandTheme.colors.gray.light
            )
        )
    }
}


//@Preview(widthDp = 370, heightDp = 72)
@Composable
private fun CouponPreview() {
    MixedWashTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                8.dp, alignment = Alignment.CenterVertically
            )
        ) {
            var selectedIndex by remember {
                mutableIntStateOf(0)
            }
            Coupon(title = "30₹ Cashback on SBI Credit Card",
                subtitle = "On all orders above 3kg",
                selected = selectedIndex == 0,
                onClick = { selectedIndex = 0 })
            Coupon(title = "30₹ Cashback on SBI Credit Card",
                subtitle = "On all orders above 3kg",
                selected = selectedIndex == 1,
                onClick = { selectedIndex = 1 })

        }
    }
}