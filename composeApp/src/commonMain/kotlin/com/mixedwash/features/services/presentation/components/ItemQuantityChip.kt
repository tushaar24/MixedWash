package com.mixedwash.features.services.presentation.components

import BrandTheme
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.ui.theme.Green

@Composable
fun ItemQuantityChip(
    minPrice: Int?,
    modifier: Modifier = Modifier,
    isServiceItem: Boolean,
    showAddLabel: Boolean = true,
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onAdd: () -> Unit,
) {
    val containerColor by animateColorAsState(if (quantity > 0) Green else BrandTheme.colors.gray.darker)
    val width by animateDpAsState(if (quantity > 0) 64.dp else 34.dp)
    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = modifier
                .width(width)
                .height(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(containerColor)
                .animateContentSize(),
            horizontalArrangement = Arrangement.spacedBy(
                space = if (isServiceItem) 4.dp else 10.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val contentColor = BrandTheme.colors.gray.lighter

            if (quantity == 0) {
                Text(
                    text = "+",
                    fontSize = 14.sp,
                    color = contentColor,
                    modifier = Modifier.clickable(onClick = onAdd)
                )
                if (showAddLabel) {
                    Text("ADD", fontSize = 12.sp, color = contentColor)
                }
            } else {

                if (!isServiceItem) {
                    Text(
                        "-",
                        modifier = Modifier.noRippleClickable(onClick = onDecrement),
                        fontSize = 12.sp,
                        color = contentColor
                    )
                    Text(
                        quantity.toString(),
                        fontSize = 12.sp,
                        color = contentColor,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "+",
                        modifier = Modifier.noRippleClickable(onClick = onIncrement),
                        fontSize = 12.sp,
                        color = contentColor
                    )
                } else {
                    Text(
                        "-",
                        modifier = Modifier.noRippleClickable(onClick = onDecrement),
                        fontSize = 12.sp,
                        color = contentColor,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "ADDED",
                        fontSize = 10.sp,
                        color = contentColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        minPrice?.let {
            Text(
                text = "₹${minPrice.div(100)}",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}