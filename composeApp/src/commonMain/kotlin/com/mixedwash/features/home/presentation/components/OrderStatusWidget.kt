package com.mixedwash.features.home.presentation.components

import BrandTheme.colors
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.core.presentation.components.OrderProgressRow
import com.mixedwash.features.home.presentation.model.OrderStatusWidgetData

@Composable
fun OrderStatusWidget(
    orders: List<OrderStatusWidgetData>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { orders.size })
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 24.dp,
            userScrollEnabled = orders.size > 1
        ) { page ->
            val order = orders[page]
            val completedTill = if (order.outForDelivery) OrderProgressStage.WASH
                                else if (order.pickedUp) OrderProgressStage.PICKUP
                                else OrderProgressStage.PLACED
            val currentActive: OrderProgressStage? = if (order.outForDelivery) OrderProgressStage.DELIVERY
                                    else if (order.pickedUp) OrderProgressStage.WASH
                                    else if (order.outForPickup) OrderProgressStage.PICKUP
                                    else null
            val textColorPrimary =
                if (currentActive?.completed == true) colors.gray.c200 else colors.gray.dark
            val textColorSecondary =
                if (currentActive?.completed == true) colors.gray.c300 else colors.gray.c600
            val matteGreen = Color(0xFF8AAA6B)
            val gradientColors = if (currentActive?.completed == true) {
                listOf(
                    matteGreen,
                    matteGreen
                )
            } else {
                listOf(
                    colors.gray.c100,
                    colors.gray.c300
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onClick(order.orderId) }
                    .background(
                        Brush.linearGradient(colors = gradientColors)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.padding(end = 32.dp).weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "#${order.orderId.takeLast(6).uppercase()}",
                                fontSize = 10.sp,
                                lineHeight = 14.4.sp,
                                color = textColorSecondary
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = order.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 18.sp,
                                    color = textColorPrimary
                                )

                                Box(contentAlignment = Alignment.Center) {
                                    Box(
                                        modifier = Modifier.size(10.dp)
                                            .clip(CircleShape)
                                            .background(textColorPrimary).padding(2.dp)
                                    )
                                    Icon(
                                        modifier = Modifier.size(10.dp),
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = gradientColors[0]
                                    )
                                }
                            }

                            Text(
                                text = currentActive?.catchPhrase ?: completedTill.catchPhrase,
                                minLines = 2,
                                lineHeight = 16.sp,
                                fontSize = 12.sp,
                                color = textColorSecondary
                            )
                        }

                        AsyncImage(
                            model = "https://assets-aac.pages.dev/assets/delivery_scooter.png",
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Fit,
                            contentDescription = null
                        )
                    }

                    OrderProgressRow(lastCompletedStage = completedTill, currentlyActiveStage = currentActive)
                }
            }
        }

        if (orders.size > 1) {
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(pagerState.pageCount) { idx ->
                    val color =
                        if (pagerState.currentPage == idx) colors.gray.dark
                        else colors.gray.c400
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(4.dp)
                    )
                }
            }
        }
    }
}

enum class OrderProgressStage(
    val displayName: String,
    val imageUrl: String,
    val catchPhrase: String,
    val completed: Boolean = false,
) {
    PLACED(
        "Placed",
        "https://assets-aac.pages.dev/assets/delivery_scooter.png",
        "Our captain is on his way to pickup your order"
    ),
    PICKUP(
        "Pickup",
        "https://assets-aac.pages.dev/assets/delivery_scooter.png",
        "Our captain is on his way to pickup your order"
    ),
    WASH(
        "Wash",
        "https://assets-aac.pages.dev/assets/washing_machine.png",
        "Your order is currently being washed at our facility."
    ),
    DELIVERY(
        "Delivery",
        "https://assets-aac.pages.dev/assets/delivery_scooter.png",
        "Your order is out for delivery. Our captain will deliver it shortly.",
        true
    ),
}