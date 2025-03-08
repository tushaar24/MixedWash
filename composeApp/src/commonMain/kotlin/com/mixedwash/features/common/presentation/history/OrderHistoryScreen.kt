package com.mixedwash.features.common.presentation.history

/*
@Composable
fun OrderHistoryScreen(state: OrderHistoryState, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
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
                    status = order.orderDeliveryStatus,
                    cost = order.price,
                    onDetails = {},
                )

                Divider(color = dividerBlack)
            }
        }
    }
}
*/
