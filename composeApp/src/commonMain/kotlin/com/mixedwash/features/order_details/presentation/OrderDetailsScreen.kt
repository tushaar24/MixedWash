package com.mixedwash.features.order_details.presentation

import BrandTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.util.convertToDateAndTime
import com.mixedwash.features.order_details.presentation.components.BookingSummary
import com.mixedwash.features.order_details.presentation.components.DetailsScreenHeaderContent
import com.mixedwash.ui.theme.components.HeaderIconButton
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_location_pin
import mixedwash.composeapp.generated.resources.ic_pickup_scooter
import mixedwash.composeapp.generated.resources.ic_processing
import org.jetbrains.compose.resources.vectorResource

@Composable
fun OrderDetailsScreen(
    state: OrderDetailsScreenState,
    onEvent: (OrderDetailsScreenEvent) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    WindowInsetsContainer {
        Column(modifier = modifier) {
            DefaultHeader(
                title = "",
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        onClick = { navController.navigateUp() }
                    )
                },
            )

            state.order?.let { order ->
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 36.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .size(44.dp)
                                        .background(BrandTheme.colors.gray.darker)
                                        .padding(5.5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.ic_processing),
                                        contentDescription = null,
                                        tint = BrandTheme.colors.gray.c200
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "Order #${order.id.takeLast(6)}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BrandTheme.colors.gray.dark
                                    )

                                    Text(
                                        text = "placed ${order.createdAtSeconds.convertToDateAndTime()}",
                                        fontSize = 12.sp,
                                        color = BrandTheme.colors.gray.c500,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Column {
                                DetailsScreenHeaderContent(
                                    icon = Res.drawable.ic_location_pin,
                                    title = order.address.title,
                                    text = order.address.addressLine1 + ", " + order.address.addressLine2
                                )

                                Spacer(Modifier.height(2.dp))

                                DetailsScreenHeaderContent(
                                    icon = Res.drawable.ic_pickup_scooter,
                                    title = (order.bookings[0].pickedUpSeconds ?: 0L).convertToDateAndTime(),
                                    text = "Picked Up"
                                )
                            }
                        }
                    }

                    items(order.bookings) { booking ->
                        BookingSummary(
                            booking = booking,
                        )
                    }
                }
            }
        }
    }
}