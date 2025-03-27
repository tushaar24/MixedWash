package com.mixedwash.features.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.booking.domain.model.BookingState
import com.mixedwash.core.booking.domain.model.calculateTotalPrice
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.dump.TitleWithIcon
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.core.presentation.util.formatTimestamp
import com.mixedwash.features.history.domain.model.OrderDeliveryStatus
import com.mixedwash.features.history.presentation.components.OrderSummaryCard
import com.mixedwash.features.history.presentation.components.StatisticCard
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.dividerBlack
import kotlinx.coroutines.flow.Flow
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_clothes_hanger

@Composable
fun OrderHistoryScreen(
    state: OrderHistoryScreenState,
    onEvent: (OrderHistoryScreenEvent) -> Unit,
    uiEventsFlow: Flow<OrderHistoryScreenUiEvent>,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is OrderHistoryScreenUiEvent.Navigate -> {
                navController.navigate(event.route)
            }
        }
    }

    WindowInsetsContainer {
        Column {
            DefaultHeader(
                title = "Order History",
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        onClick = { navController.navigateUp() }
                    )
                }
            )

            LazyColumn(
                modifier = modifier.fillMaxSize().padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
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

                item { Spacer(Modifier.height(16.dp)) }

                item {
                    TitleWithIcon(
                        title = "Orders",
                        icon = Res.drawable.ic_clothes_hanger,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
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
                    items(items = state.orders) { order ->
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Logger.d("fcuk", "${order.pickupSlotSelected.startTimeStamp}")
                            OrderSummaryCard(
                                orderId = order.id,
                                titles = order.bookingItems.map { it.serviceName },
                                ordered = formatTimestamp(order.pickupSlotSelected.startTimeStamp),
                                delivery = formatTimestamp(order.dropSlotSelected.startTimeStamp),
                                status = when (order.state) {
                                    is BookingState.Cancelled -> OrderDeliveryStatus.CANCELLED
                                    is BookingState.Delivered -> OrderDeliveryStatus.DELIVERED
                                    else -> OrderDeliveryStatus.PROCESSING
                                },
                                cost = if (order.state is BookingState.Delivered)
                                    formatIndianCurrency(order.calculateTotalPrice())
                                else null,
                                onDetails = {
                                    onEvent(OrderHistoryScreenEvent.OnOrderDetailsScreen(order.id))
                                },
                            )

                            HorizontalDivider(color = dividerBlack)
                        }
                    }
                }
            }
        }
    }
}