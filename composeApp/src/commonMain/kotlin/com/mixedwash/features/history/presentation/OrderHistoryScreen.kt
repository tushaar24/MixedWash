package com.mixedwash.features.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mixedwash.features.history.presentation.components.OrderSummaryCard
import com.mixedwash.features.history.presentation.components.StatisticCard
import com.mixedwash.core.presentation.components.dump.TitleWithIcon
import com.mixedwash.core.presentation.util.formatTimestamp
import com.mixedwash.ui.theme.dividerBlack
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_clothes_hanger
import mixedwash.composeapp.generated.resources.ic_insights

@Composable
fun OrderHistoryScreen(state: OrderHistoryState, modifier: Modifier = Modifier) {
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
                        value = 29,
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

        items(state.orders) { order ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OrderSummaryCard(
                    orderId = order.orderId,
                    titles = order.serviceItems.map { it.title },
                    ordered = formatTimestamp(order.orderedTimestamp),
                    delivery = if (order.deliveryTimestamp != null) formatTimestamp(order.deliveryTimestamp) else null,
                    status = order.orderDeliveryStatusDto,
                    cost = order.price,
                    onDetails = {},
                )

                HorizontalDivider(color = dividerBlack)
            }
        }
    }
}