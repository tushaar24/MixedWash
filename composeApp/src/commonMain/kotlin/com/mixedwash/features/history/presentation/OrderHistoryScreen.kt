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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.history.presentation.components.OrderSummaryCard
import com.mixedwash.features.history.presentation.components.StatisticCard
import com.mixedwash.ui.theme.components.HeaderIconButton
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    state: OrderHistoryScreenState,
    onEvent: (OrderHistoryScreenEvent) -> Unit,
    uiEventsFlow: Flow<OrderHistoryScreenUiEvent>,
    navController: NavController,
    modifier: Modifier = Modifier,
    snackbarHandler: SnackbarHandler
) {
    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is OrderHistoryScreenUiEvent.Navigate -> {
                navController.navigate(event.route)
            }

            is OrderHistoryScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.payload)
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                onEvent(OrderHistoryScreenEvent.OnRefresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    WindowInsetsContainer {
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = state.isRefreshing,
                    containerColor = BrandTheme.colors.gray.darker,
                    color = BrandTheme.colors.gray.light,
                    state = pullToRefreshState,
                )
            },
            state = pullToRefreshState,
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(OrderHistoryScreenEvent.OnRefresh) }
        ) {
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

                    if (state.stagingEnabled) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(bottom = 8.dp)
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
                    }

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
                        }
                    }

                    item {
                        Spacer(Modifier.height(32.dp))
                    }

                    if (state.stagingEnabled) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .padding(16.dp)
                                    .noRippleClickable { onEvent(OrderHistoryScreenEvent.OnClearAllOrders) }
                                    .background(Color(0xFFFFE8BF))
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Clear All Orders",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                        item {
                            Spacer(Modifier.height(8.dp))
                        }
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
                        itemsIndexed(state.orders) { _, orderPresentation ->
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Box {

                                    OrderSummaryCard(
                                        order = orderPresentation.order,
                                        delivered = orderPresentation.delivered,
                                        cancelled = orderPresentation.cancelled,
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
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null,
                                            )
                                    )
                                }

                                Spacer(Modifier.height(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}