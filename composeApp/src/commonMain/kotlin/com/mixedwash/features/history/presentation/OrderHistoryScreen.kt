package com.mixedwash.features.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.core.booking.domain.model.BookingState
import com.mixedwash.core.presentation.components.dump.TitleWithIcon
import com.mixedwash.core.presentation.util.formatTimestamp
import com.mixedwash.features.history.domain.model.OrderDeliveryStatus
import com.mixedwash.features.history.presentation.components.OrderSummaryCard
import com.mixedwash.features.history.presentation.components.StatisticCard
import com.mixedwash.ui.theme.dividerBlack
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_clothes_hanger
import mixedwash.composeapp.generated.resources.ic_insights

@Composable
fun OrderHistoryScreen(
    state: OrderHistoryState,
    onEvent: (OrderHistoryEvent) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            TitleWithIcon(title = "Insights", icon = Res.drawable.ic_insights)
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.insights) {
                    StatisticCard(
                        value = it.value,
                        metric = it.metric,
                        unit = it.unit,
                        icon = it.icon,
                    )

                }
            }
        }

        item {
            TitleWithIcon(title = "Orders", icon = Res.drawable.ic_clothes_hanger)
        }

        if (state.orders.isEmpty()) {
            item {
                Text(
                    text = "Nothing to show!",
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(state.orders) { order ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OrderSummaryCard(
                        orderId = order.id,
                        titles = order.bookingItems.map { it.name },
                        ordered = formatTimestamp(order.pickupSlotSelected.startTimeStamp),
                        delivery = formatTimestamp(order.dropSlotSelected.startTimeStamp),
                        status = when (order.state) {
                            is BookingState.Cancelled -> OrderDeliveryStatus.CANCELLED
                            is BookingState.Delivered -> OrderDeliveryStatus.DELIVERED
                            else -> OrderDeliveryStatus.PROCESSING
                        },
                        cost = 1024,
                        onDetails = { onEvent(OrderHistoryEvent.OnOrderDetails(order.id)) },
                    )

                    HorizontalDivider(color = dividerBlack)
                }
            }
        }
    }
}