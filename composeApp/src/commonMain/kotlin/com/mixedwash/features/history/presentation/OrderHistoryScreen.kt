package com.mixedwash.features.history.presentation

import BrandTheme
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.history.presentation.components.OrderSummaryCard
import com.mixedwash.features.history.presentation.components.StatisticCard
import com.mixedwash.ui.theme.components.HeaderIconButton
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalFoundationApi::class)
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
            ) {

                state.insights?.let { insights ->
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(insights) {
                                StatisticCard(
                                    value = it.value,
                                    metric = it.metric,
                                    unit = it.unit,
                                    icon = it.icon,
                                )

                            }
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }

                if (state.stagingEnabled) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(Color(0xFFFFE8BF))
                        ) {
                            Text(
                                text = "staging mode enabled",
                                fontSize = 12.sp,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = BrandTheme.colors.gray.dark
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = { onEvent(OrderHistoryScreenEvent.OnClearAllOrders) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = BrandTheme.colors.gray.light,
                            ),
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 16.dp, top = 12.dp, end = 16.dp),
                            shape = RectangleShape,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                )

                                Text(
                                    text = "Clear All Orders",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(48.dp))
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
                    itemsIndexed(state.orders) { index, orderPresentation ->
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Box {
                                var showDropdown by remember { mutableStateOf(false) }
                                val tapPosition by remember { mutableStateOf(Offset.Zero) }

                                OrderSummaryCard(
                                    order = orderPresentation.order,
                                    delivered = orderPresentation.delivered,
                                    serviceImageUrls = orderPresentation.serviceImageUrls,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = {
                                                onEvent(
                                                    OrderHistoryScreenEvent.OnOrderDetailsScreen(
                                                        orderPresentation.order.id
                                                    )
                                                )
                                            },
                                            onLongClick = { showDropdown = true },
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                        )
                                )

                                DropdownMenu(
                                    expanded = showDropdown,
                                    onDismissRequest = { showDropdown = false },
                                    offset = DpOffset(
                                        x = tapPosition.x.dp,
                                        y = tapPosition.y.dp
                                    )
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Delete Order") },
                                        onClick = {
                                            onEvent(
                                                OrderHistoryScreenEvent.OnDeleteOrder(
                                                    orderPresentation.order.id
                                                )
                                            )
                                            showDropdown = false
                                        }
                                    )
                                }
                            }

                            Spacer(Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}