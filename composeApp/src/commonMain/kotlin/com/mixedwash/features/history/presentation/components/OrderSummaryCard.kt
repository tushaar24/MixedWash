package com.mixedwash.features.history.presentation.components

import BrandTheme
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.presentation.components.dump.IndicationChip
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.features.history.domain.model.OrderDeliveryStatus
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray50
import com.mixedwash.ui.theme.Gray700
import com.mixedwash.ui.theme.Gray800
import com.mixedwash.ui.theme.Gray900
import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.Yellow
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_verified
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderSummaryCard(
    orderId: String,
    titles: List<String>,
    ordered: String,
    delivery: String,
    status: OrderDeliveryStatus,
    cost: String?,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                IndicationChip(
                    text = orderId,
                    textColor = Gray100,
                    backgroundColor = Gray900,
                    borderColor = Gray800,
                    leadingIcon = '#'
                )

                IndicationChip(
                    text = status.toString().lowercase().replaceFirstChar { it.uppercase() },
                    textColor = when (status) {
                        OrderDeliveryStatus.PROCESSING -> Yellow
                        OrderDeliveryStatus.DELIVERED -> Gray100
                        OrderDeliveryStatus.CANCELLED -> Gray800
                    },
                    backgroundColor = when (status) {
                        OrderDeliveryStatus.DELIVERED -> Green
                        else -> Gray50
                    },
                    borderColor = when (status) {
                        OrderDeliveryStatus.PROCESSING -> Yellow
                        OrderDeliveryStatus.DELIVERED -> Green
                        OrderDeliveryStatus.CANCELLED -> Gray800
                    },
                    leadingIcon = '•'
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "₹ " + when (status) {
                        OrderDeliveryStatus.PROCESSING -> "TBD"
                        OrderDeliveryStatus.DELIVERED -> cost
                        OrderDeliveryStatus.CANCELLED -> "-"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = when (status) {
                        OrderDeliveryStatus.DELIVERED -> Green
                        else -> Gray700
                    }
                )

                if (status == OrderDeliveryStatus.DELIVERED) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_verified),
                        contentDescription = null,
                        tint = Green,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Text(
            text = titles.joinToString(" • "),
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(0.75f),
            color = Gray800
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                TimeTracker(
                    action = "Ordered",
                    datetime = ordered,
                    textColor = Gray800
                )

                TimeTracker(
                    action = if (status == OrderDeliveryStatus.DELIVERED) "Delivered" else "Est. Delivery",
                    datetime = delivery ,
                    textColor = Gray800,
                )
            }

            Text(
                text = "Details",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = BrandTheme.colors.gray.c800,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, BrandTheme.colors.gray.c900, RoundedCornerShape(12.dp))
                    .padding(horizontal = 18.dp, vertical = 8.dp)
                    .noRippleClickable { onDetails() }
            )
        }
    }
}

@Composable
fun TimeTracker(
    action: String,
    datetime: String,
    textColor: Color,
    fontSize: TextUnit = 12.sp,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = action,
            color = textColor,
            fontSize = fontSize,
        )

        Text(
            text = datetime,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
    }
}