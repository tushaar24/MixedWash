package com.mixedwash.features.order_details.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.feature.orders.domain.model.Booking
import com.mixedwash.core.feature.orders.domain.model.calculateItemPrice
import com.mixedwash.core.feature.orders.domain.model.calculateTotalPrice
import com.mixedwash.core.presentation.components.OrderProgressRow
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.convertToDate
import com.mixedwash.core.presentation.util.formattedHourTime
import com.mixedwash.features.home.presentation.components.OrderProgressStage
import com.mixedwash.features.order_details.presentation.OrderDetailsScreenEvent
import com.mixedwash.features.order_details.presentation.StagingOperationType
import com.mixedwash.ui.theme.GreenDark
import com.mixedwash.ui.theme.dividerBlack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_drop
import mixedwash.composeapp.generated.resources.ic_pencil
import mixedwash.composeapp.generated.resources.ic_redirect_arrow
import mixedwash.composeapp.generated.resources.ic_verified
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummary(
    scope: CoroutineScope,
    onEvent: (OrderDetailsScreenEvent) -> Unit,
    booking: Booking,
    orderId: String,
    orderPickedUp: Boolean,
    serviceImageUrls: Map<String, String>,
    stagingOperations: List<StagingOperationType>,
    modifier: Modifier = Modifier
) {

    val stagingOperationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (stagingOperationSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { stagingOperationSheetState.hide() }
            },
            dragHandle = {},
            containerColor = BrandTheme.colors.gray.c100,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(bottom = 48.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFFFE8BF))
                        .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 32.dp)
                ) {
                    Text(
                        text = "Staging Operations",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    stagingOperations.forEach { type ->
                        Column(
                            modifier = Modifier.fillMaxWidth().clickable {
                                onEvent(
                                    when (type) {
                                        StagingOperationType.CANCEL -> OrderDetailsScreenEvent.OnCancelBooking(
                                            booking.id
                                        )

                                        StagingOperationType.DELETE -> OrderDetailsScreenEvent.OnDeleteOrder(
                                            orderId
                                        )

                                        StagingOperationType.SET_OUT_FOR_PICKUP -> OrderDetailsScreenEvent.OnSetOutForPickup(
                                            orderId
                                        )

                                        StagingOperationType.SET_PICKED_UP -> OrderDetailsScreenEvent.OnSetPickedUp(
                                            orderId
                                        )

                                        StagingOperationType.SET_OUT_FOR_DELIVERY -> OrderDetailsScreenEvent.OnSetOutForDelivery(
                                            booking.id
                                        )

                                        StagingOperationType.SET_DELIVERED -> OrderDetailsScreenEvent.OnSetDelivered(
                                            booking.id
                                        )

                                        StagingOperationType.SET_PAID -> OrderDetailsScreenEvent.OnSetPaid(
                                            booking.id
                                        )
                                    }
                                )
                                scope.launch {
                                    stagingOperationSheetState.hide()
                                }
                            },
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = type.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                            )

                            HorizontalDivider(color = dividerBlack, thickness = 1.dp)
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(BrandTheme.colors.gray.light)
    ) {
        Box(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Column {
                            Text(
                                text = "Booking #${booking.id.takeLast(6)}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Text(
                                text = "${booking.dropSlotSelected.startTimeStamp.convertToDate()}, ${
                                    formattedHourTime(
                                        booking.dropSlotSelected.startTimeStamp,
                                        booking.dropSlotSelected.endTimeStamp
                                    )
                                }",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = BrandTheme.colors.gray.c500
                            )
                        }

                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_pencil),
                            contentDescription = null,
                            tint = BrandTheme.colors.gray.c500,
                            modifier = Modifier.size(20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(BrandTheme.colors.gray.c200)
                                .padding(4.17.dp)
                                .noRippleClickable {
                                    scope.launch {
                                        stagingOperationSheetState.show()
                                    }
                                }
                        )
                    }

                    if (booking.deliveredSeconds != null) {
                        StatusChip(
                            text = "Delivered",
                            textColor = GreenDark,
                            backgroundColor = Color.Unspecified,
                            borderColor = GreenDark,
                        )
                    } else if (booking.outForDeliverySeconds != null) {
                        StatusChip(
                            text = "Out for delivery",
                            textColor = BrandTheme.colors.gray.c200,
                            backgroundColor = GreenDark,
                            borderColor = GreenDark,
                        )
                    } else if (booking.isCancelled) {
                        StatusChip(
                            text = "Cancelled",
                            textColor = BrandTheme.colors.gray.dark,
                            backgroundColor = BrandTheme.colors.gray.c300,
                            borderColor = Color.Transparent,
                        )
                    } else {
                        StatusChip(
                            text = "Processing",
                            textColor = BrandTheme.colors.gray.dark,
                            backgroundColor = BrandTheme.colors.gray.c300,
                            borderColor = Color.Transparent,
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column {
                        booking.bookingItems.forEach { item ->

                            val cost = item.calculateItemPrice()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalPlatformContext.current)
                                            .data(item.imageUrl ?: serviceImageUrls[item.serviceId])
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                        error = painterResource(Res.drawable.ic_drop),
                                        modifier = Modifier.size(20.dp)
                                    )

                                    Text(
                                        text = item.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Text(
                                    text = "₹ $cost",
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }

                    if (booking.outForDeliverySeconds != null) {
                        HorizontalDivider(color = dividerBlack)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Subtotal",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            )

                            Text(
                                text = "₹ ${booking.calculateTotalPrice()}",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                if (booking.deliveredSeconds != null || booking.outForDeliverySeconds != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Booking Total",
                                fontSize = 12.sp,
                                color = BrandTheme.colors.gray.dark,
                            )

                            Text(
                                text = "₹ ${booking.calculateTotalPrice()}/-",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BrandTheme.colors.gray.c800
                            )
                        }

                        if (booking.paymentId == null) {
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(6.dp))
                                    .background(BrandTheme.colors.gray.darker)
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .noRippleClickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = "Pay Now",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BrandTheme.colors.gray.light
                                    )

                                    Icon(
                                        imageVector = vectorResource(Res.drawable.ic_redirect_arrow),
                                        contentDescription = null,
                                        tint = BrandTheme.colors.gray.light
                                    )
                                }
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Paid",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 14.4.sp,
                                    color = GreenDark
                                )

                                Icon(
                                    imageVector = vectorResource(Res.drawable.ic_verified),
                                    contentDescription = null,
                                    tint = GreenDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                } else {
                    OrderProgressRow(stage = if (orderPickedUp) OrderProgressStage.WASH else OrderProgressStage.PICKUP)
                }
            }
        }

        if (booking.isCancelled) {
            Box(
                modifier = Modifier.matchParentSize().clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.6f))
            )
        }
    }
}

@Composable
fun StatusChip(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 14.4.sp
        )
    }
}