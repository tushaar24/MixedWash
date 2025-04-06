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
import com.mixedwash.features.home.presentation.model.OrderStatus
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_progress_completed
import mixedwash.composeapp.generated.resources.ic_progress_pending
import mixedwash.composeapp.generated.resources.ic_progress_processing
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderStatusWidget(
    orders: List<OrderStatus>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val stage = OrderProgressStage.PICKUP
    val pagerState = rememberPagerState(pageCount = { orders.size })
    val textColorPrimary = if (stage.completed) colors.gray.c200 else colors.gray.dark
    val textColorSecondary = if (stage.completed) colors.gray.c300 else colors.gray.c600
    val matteGreen = Color(0xFF8AAA6B)
    val gradientColors = if (stage.completed) {
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

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        HorizontalPager(pagerState) { page ->
            val order = orders[page]
            Box(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(colors = gradientColors)
                    )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
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
                                text = "#${order.orderId.takeLast(8).uppercase()}",
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

                                Box(
                                    modifier = Modifier.size(12.dp)
                                        .clip(CircleShape)
                                        .background(textColorPrimary).padding(2.dp)
                                        .clickable { onClick(order.orderId) }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = colors.gray.c100
                                    )
                                }
                            }

                            Text(
                                text = stage.catchPhrase,
                                minLines = 2,
                                lineHeight = 14.sp,
                                fontSize = 10.sp,
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OrderProgressStage.entries.forEach { entry ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = vectorResource(
                                        if (entry.ordinal < stage.ordinal) Res.drawable.ic_progress_completed
                                        else if (entry.ordinal > stage.ordinal) Res.drawable.ic_progress_pending
                                        else Res.drawable.ic_progress_processing
                                    ),
                                    contentDescription = null,
                                    tint = if (stage.completed) textColorPrimary else matteGreen,
                                    modifier = Modifier.size(18.dp)
                                )

                                Text(
                                    text = entry.displayName,
                                    lineHeight = 14.4.sp,
                                    fontSize = 12.sp,
                                    color = textColorPrimary
                                )
                            }
                        }
                    }
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