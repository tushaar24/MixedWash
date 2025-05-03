package com.mixedwash.features.history.presentation.components

import BrandTheme.colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.mixedwash.core.feature.orders.domain.model.Order
import com.mixedwash.core.presentation.util.convertToDate
import com.mixedwash.core.presentation.util.convertToFullDate
import com.mixedwash.ui.theme.GreenDark
import com.mixedwash.ui.theme.dividerBlack
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_drop
import mixedwash.composeapp.generated.resources.ic_processing
import mixedwash.composeapp.generated.resources.ic_progress_cancelled_large
import mixedwash.composeapp.generated.resources.ic_progress_completed_large
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderSummaryCard(
    order: Order,
    serviceImageUrls: Map<String, String>,
    imageUrls: List<List<String?>>,
    delivered: Boolean,
    cancelled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {

        Column(
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
                            imageVector = vectorResource(
                                if (delivered) Res.drawable.ic_progress_completed_large
                                else if (cancelled) Res.drawable.ic_progress_cancelled_large
                                else Res.drawable.ic_processing
                            ),
                            contentDescription = null,
                            tint = if (delivered) GreenDark else colors.gray.c600
                        )
                    }

                    Column {
                        Text(
                            text = "Order ${order.id.takeLast(6)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Text(
                            text = "placed ${order.createdAtSeconds.convertToFullDate()}",
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

            order.bookings.forEachIndexed { outerIdx, booking ->
                val progress =
                    if (booking.deliveredSeconds != null) Pair("Delivered", colors.gray.dark)
                    else if (booking.isCancelled) Pair("Cancelled", colors.gray.dark)
                    else if (booking.outForDeliverySeconds != null) Pair(
                        "Out For Delivery",
                        GreenDark
                    )
                    else if (order.pickedUpSeconds != null) Pair("Processing", colors.gray.c500)
                    else if (order.outForPickupSeconds != null) Pair("Out For Pickup", GreenDark)
                    else Pair("Processing", colors.gray.c500)

//                val bookingDelivered = booking.deliveredSeconds != null
                val dateAndStatusText = buildAnnotatedString {
                    "${order.createdAtSeconds.convertToDate()} • ${progress.first}"
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
                            color = progress.second
                        )
                    ) {
                        append(progress.first)
                    }
                }

                Column {
                    Text(
                        modifier = Modifier.padding(start = 60.dp),
                        text = dateAndStatusText,
                    )

                    Spacer(Modifier.height(8.dp))

                    booking.bookingItems.forEachIndexed { innerIdx, item ->

                        Column {
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
                                            .data(imageUrls[outerIdx][innerIdx] ?: serviceImageUrls[item.serviceId]).crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        error = painterResource(Res.drawable.ic_drop),
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Text(
                                        text = item.name + if (item.serviceName != item.name) " • ${item.serviceName}" else "",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = colors.gray.dark
                                    )
                                }

//                                val unit = when (item.itemPricing) {
//                                    is BookingItemPricing.ServiceItemPricing -> item.itemPricing.unit
//                                    is BookingItemPricing.SubItemFixedPricing -> "pc"
//                                    is BookingItemPricing.SubItemRangedPricing -> "pc"
//                                }
//
//                                Text(
//                                    text = if (bookingDelivered) "${item.quantity} $unit" else "TBD",
//                                    fontSize = 12.sp,
//                                    fontWeight = FontWeight.Medium,
//                                    color = if (delivered) colors.gray.dark else colors.gray.c500
//                                )
                            }

                            if (innerIdx != booking.bookingItems.lastIndex) {
                                Spacer(modifier = Modifier.height(6.dp))
                                HorizontalDivider(
                                    color = colors.gray.light,
                                    modifier = Modifier.padding(start = 60.dp, end = 8.dp)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }

                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = dividerBlack,
            )
        }

        if (cancelled) Box(
            modifier = Modifier.matchParentSize().background(colors.gray.c50.copy(alpha = 0.6f))
        )
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