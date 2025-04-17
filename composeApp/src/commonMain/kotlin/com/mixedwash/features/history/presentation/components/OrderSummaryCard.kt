package com.mixedwash.features.history.presentation.components

import BrandTheme.colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.orders.domain.model.BookingItemPricing
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.convertToDate
import com.mixedwash.ui.theme.GreenDark
import com.mixedwash.ui.theme.dividerBlack
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_drop
import mixedwash.composeapp.generated.resources.ic_processing
import mixedwash.composeapp.generated.resources.ic_progress_completed
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderSummaryCard(
    order: Order,
    delivered: Boolean,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.noRippleClickable {
            onDetails()
        },
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .size(44.dp)
                        .background(colors.gray.c200)
                        .padding(5.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = vectorResource(if (delivered) Res.drawable.ic_progress_completed else Res.drawable.ic_processing),
                        contentDescription = null,
                        tint = if (delivered) GreenDark else colors.gray.c600
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Order #${order.id.takeLast(6)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = "${order.bookings.size} ${if (order.bookings.size > 1) "bookings" else "booking"} • ${if (delivered) "Completed" else "Pending"}",
                        fontSize = 12.sp,
                        color = colors.gray.dark
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            order.bookings.forEach { booking ->
                val bookingDelivered = booking.deliveredSeconds != null
                val dateAndStatusText = buildAnnotatedString {
                    "${order.createdAtSeconds.convertToDate()} • ${if (bookingDelivered) "Delivered" else "Processing"}"
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.gray.dark
                        )
                    ) {
                        append("${order.createdAtSeconds.convertToDate()} • ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (bookingDelivered) GreenDark else colors.gray.c500
                        )
                    ) {
                        append(if (bookingDelivered) "Delivered" else "Processing")
                    }
                }
                Text(
                    modifier = Modifier.padding(start = 60.dp),
                    text = dateAndStatusText,
                )

                booking.bookingItems.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(item.imageUrl).crossfade(true)
                                    .build(),
                                contentDescription = null,
                                error = painterResource(Res.drawable.ic_drop),
                                modifier = Modifier.size(20.dp)
                            )

                            Text(
                                text = item.serviceName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.gray.dark
                            )
                        }

                        val unit = when (item.itemPricing) {
                            is BookingItemPricing.ServiceItemPricing -> item.itemPricing.unit
                            is BookingItemPricing.SubItemFixedPricing -> "pc"
                            is BookingItemPricing.SubItemRangedPricing -> "pc"
                        }

                        Text(
                            text = if (bookingDelivered) "${item.quantity} $unit" else "TBD",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (delivered) colors.gray.dark else colors.gray.c500
                        )
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = dividerBlack, modifier = Modifier.padding(start = 60.dp))
                }
            }
        }
    }
}

//@Composable
//fun TimeTracker(
//    action: String,
//    datetime: String,
//    textColor: Color,
//    fontSize: TextUnit = 12.sp,
//) {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(2.dp)
//    ) {
//        Text(
//            text = action,
//            color = textColor,
//            fontSize = fontSize,
//        )
//
//        Text(
//            text = datetime,
//            color = textColor,
//            fontSize = fontSize,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}