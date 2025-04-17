package com.mixedwash.features.history.presentation.components

import BrandTheme
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.formatTimestamp
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_drop
import mixedwash.composeapp.generated.resources.ic_processing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderSummaryCard(
    order: Order,
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
                        .background(BrandTheme.colors.gray.c200)
                        .padding(5.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_processing),
                        contentDescription = null,
                        tint = BrandTheme.colors.gray.c600
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Order #${order.id.takeLast(6)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = "${order.bookings.size} • Pending",   // todo
                        fontSize = 12.sp,
                        color = BrandTheme.colors.gray.dark
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
            )
        }

        order.bookings.forEach { booking ->
            val dateAndStatusText = buildAnnotatedString {
                "${formatTimestamp(order.createdAtSeconds)} • Processing"
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandTheme.colors.gray.dark
                    )
                ) {
                    append("${formatTimestamp(order.createdAtSeconds)} • ")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandTheme.colors.gray.c500
                    )
                ) {
                    append("Processing")    //todo
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
                            color = BrandTheme.colors.gray.dark
                        )
                    }

                    Text(
                        text = "TBD",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = BrandTheme.colors.gray.c500
                    )
                }
            }
        }
    }
}

@Composable
fun TimeTracker(
    action: String,
    datetime: String,
    textColor: Color,
    fontSize: TextUnit = 12.sp,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = action,
            color = textColor,
            fontSize = fontSize,
        )

        Text(
            text = datetime,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
    }
}