package com.mixedwash.features.order_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.orders.domain.model.Booking
import com.mixedwash.core.orders.domain.model.calculateItemPrice
import com.mixedwash.core.orders.domain.model.calculateTotalPrice
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.convertToDate
import com.mixedwash.core.presentation.util.formattedHourTime
import com.mixedwash.ui.theme.GreenDark
import com.mixedwash.ui.theme.dividerBlack
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_drop
import mixedwash.composeapp.generated.resources.ic_redirect_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BookingSummary(
    booking: Booking,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(BrandTheme.colors.gray.light)
            .padding(vertical = 24.dp, horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Booking #${booking.id.takeLast(6)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "${booking.dropSlotSelected.startTimeStamp.convertToDate()}, ${formattedHourTime(booking.dropSlotSelected.startTimeStamp, booking.dropSlotSelected.endTimeStamp)}", // todo
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrandTheme.colors.gray.c500
                    )
                }

                // TODO
                if (booking.isPaid) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .border(1.dp, GreenDark, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Paid",
                            color = GreenDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 14.4.sp
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .background(GreenDark)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Out for delivery",
                            color = BrandTheme.colors.gray.c200,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 14.4.sp
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
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
                                        .data(item.imageUrl)
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

                if (!booking.isPaid) {
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
                    }
                }
            }
        }
    }
}