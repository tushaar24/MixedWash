package com.mixedwash.features.booking_details.presentation.components

import BrandTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mixedwash.core.booking.domain.model.BookingTimeSlot
import com.mixedwash.core.domain.util.capitalize
import com.mixedwash.core.presentation.util.formatTime
import com.mixedwash.core.presentation.util.getDayAndDate
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.ui.theme.dividerBlack

@Composable
fun BookingDetailsSummary(
    modifier: Modifier = Modifier,
    pickupSlot: BookingTimeSlot,
    dropSlot: BookingTimeSlot,
    deliveryAddress: Address,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(BrandTheme.shapes.card)
            .background(color = BrandTheme.colors.gray.light)
            .padding(
                horizontal = 16.dp, vertical = 18.dp
            )

    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp, bottom = 2.dp
                )
        ) {
            Text(
                text = "Slot Details",
                style = BrandTheme.typography.subtitle3.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(weight = 1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                modifier = Modifier.weight(weight = 0.5f)
            ) {
                Text(
                    text = "Pickup ",
                    style = BrandTheme.typography.body3.copy(fontWeight = FontWeight.Medium)
                )
                val (day, date) = pickupSlot.startTimeStamp.getDayAndDate()
                val startTime = pickupSlot.startTimeStamp.formatTime().run {
                    "$first $second"
                }
                val endTime = pickupSlot.endTimeStamp.formatTime().run {
                    "$first $second"
                }

                Text(
                    text = """
                        ${day.capitalize()}, $date 
                        $startTime - $endTime
                    """.trimIndent(), lineHeight = 1.5.em,
                        fontSize = 12.sp
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                modifier = Modifier.weight(weight = 0.5f)
            ) {
                Text(
                    text = "Drop",
                    style = BrandTheme.typography.body3.copy(fontWeight = FontWeight.Medium)
                )
                val (day, date) = dropSlot.startTimeStamp.getDayAndDate()
                val startTime = dropSlot.startTimeStamp.formatTime().run {
                    "$first - $second"
                }
                val endTime = dropSlot.endTimeStamp.formatTime().run {
                    "$first $second"
                }

                Text(
                    text = """
                        ${day.capitalize()}, $date 
                        $startTime - $endTime
                        """.trimIndent(), lineHeight = 1.5.em, fontSize = 12.sp
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = dividerBlack)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp, bottom = 2.dp
                )
        ) {
            Text(
                text = "Delivery address",
                style = BrandTheme.typography.subtitle3.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(weight = 1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp, Alignment.CenterHorizontally
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Location Pin Icon",
                        modifier = Modifier.requiredSize(size = 14.dp)
                    )
                    Text(
                        text = deliveryAddress.title, style = BrandTheme.typography.caption2
                    )
                }
                val deliveryText = deliveryAddress.run {
                    addressLine1 + (if (addressLine1.isNotBlank()) "\n" else "") +
                            addressLine2 + (if (addressLine2.isNotBlank()) "\n" else "") +
                            addressLine3 + (if (addressLine3.isNotBlank()) "\n" else "") +
                            pinCode
                }
                Text(
                    text = deliveryText,
                    lineHeight = 1.5.em,
                    fontSize = 12.sp
                )
            }
        }
    }
}
