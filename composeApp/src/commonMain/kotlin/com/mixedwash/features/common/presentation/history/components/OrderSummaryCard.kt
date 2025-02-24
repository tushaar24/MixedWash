package com.mixedwash.features.common.presentation.history.components

import BrandTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.features.common.presentation.history.model.OrderDeliveryStatus
import com.mixedwash.presentation.components.dump.AppOutlinedButton
import com.mixedwash.presentation.components.dump.IndicationChip
import com.mixedwash.ui.theme.Gray100
import com.mixedwash.ui.theme.Gray50
import com.mixedwash.ui.theme.Gray800

import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.Yellow

/**
 * @param status 0 - Processing, 1 - Delivered, 2 - Cancelled, else - Unknown
 *
 */
@Composable
fun OrderSummaryCard(
    orderId: Long,
    titles: List<String>,
    ordered: String,
    delivery: String?,
    status: OrderDeliveryStatus,
    cost: Int?,
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IndicationChip(
                    text = orderId.toString(),
                    textColor = Gray100,
                    backgroundColor = Gray800,
                    borderColor = Gray800,
                    leadingIcon = '#'
                )

                IndicationChip(
                    text = status.toString() ,
                    textColor = when (status) {
                        OrderDeliveryStatus.Processing -> Yellow
                        OrderDeliveryStatus.Delivered -> Gray100
                        OrderDeliveryStatus.Cancelled -> Gray800
                    },
                    backgroundColor = when (status) {
                        OrderDeliveryStatus.Delivered -> Green
                        else -> Gray50
                    },
                    borderColor = when (status) {
                        OrderDeliveryStatus.Processing -> Yellow
                        OrderDeliveryStatus.Delivered -> Green
                        OrderDeliveryStatus.Cancelled -> Gray800
                    },
                    leadingIcon = '•'
                )
            }

            Text(
                text = "₹${cost ?: "-"}",
                style = BrandTheme.typography.h5
            )
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
                    action = if (status == OrderDeliveryStatus.Delivered) "Delivered" else "Est. Delivery",
                    datetime = delivery ?: "-",
                    textColor = Gray800,
                )
            }

            AppOutlinedButton(
                onClick = onDetails,
                fontSize = 14.sp,
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                fontWeight = FontWeight.Medium,
                buttonTitle = "Details"
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
            style = BrandTheme.typography.subtitle1
        )

        Text(
            text = datetime,
            color = textColor,
            fontSize = fontSize,
            style = BrandTheme.typography.subtitle1
        )
    }
}