package com.mixedwash.features.slot_selection.presentation.components

import BrandTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.util.formattedHourTime
import com.mixedwash.core.presentation.util.getDayOfWeekAbbrev
import com.mixedwash.features.slot_selection.domain.model.response.DateSlot
import com.mixedwash.features.slot_selection.domain.model.response.TimeSlot
import com.mixedwash.features.slot_selection.presentation.model.BookingSlotState
import com.mixedwash.features.slot_selection.presentation.model.PickupSlotState
import com.mixedwash.ui.theme.GreenDark
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_washing_machine
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BookingSlotContainer(
    modifier: Modifier = Modifier,
    bookingSlotState: BookingSlotState,
    onDateSelected: (DateSlot) -> Unit,
    onTimeSelected: (DateSlot, TimeSlot) -> Unit,
    onToggleBookingExpanded: (Int) -> Unit,
) {
    val title = bookingSlotState.bookingServices.joinToString(" · ") { it.serviceName }
    val serviceImages = bookingSlotState.bookingServices.map { it.imageUrl }

    GenericSlotContainer(
        modifier = modifier,
        dateSlots = bookingSlotState.dateSlots,
        isExpanded = bookingSlotState.isExpanded,
        dateSlotSelectedId = bookingSlotState.dateSlotSelectedId,
        timeSlotSelectedId = bookingSlotState.timeSlotSelectedId,
        title = title,
        serviceDurationInHrs = bookingSlotState.serviceDurationInHrs,
        serviceImages = serviceImages,
        onDateSelected = onDateSelected,
        onTimeSelected = onTimeSelected,
        onToggleExpanded = { onToggleBookingExpanded(bookingSlotState.id) }
    )
}

@Composable
fun PickupSlotContainer(
    modifier: Modifier = Modifier,
    pickupSlotState: PickupSlotState,
    onDateSelected: (DateSlot) -> Unit,
    onTimeSelected: (DateSlot, TimeSlot) -> Unit,
    onTogglePickupExpanded: () -> Unit,
) {
    GenericSlotContainer(
        modifier = modifier,
        dateSlots = pickupSlotState.slots,
        isExpanded = pickupSlotState.isExpanded,
        dateSlotSelectedId = pickupSlotState.dateSlotSelectedId,
        timeSlotSelectedId = pickupSlotState.timeSlotSelectedId,
        title = "Order Pickup",
        serviceDurationInHrs = null,
        serviceImages = null,
        onDateSelected = onDateSelected,
        onTimeSelected = onTimeSelected,
        onToggleExpanded = onTogglePickupExpanded
    )
}

@Composable
private fun GenericSlotContainer(
    modifier: Modifier = Modifier,
    dateSlots: List<DateSlot>,
    isExpanded: Boolean,
    dateSlotSelectedId: Int?,
    timeSlotSelectedId: Int?,
    title: String,
    serviceDurationInHrs: Int?,
    serviceImages: List<String>?,
    onDateSelected: (DateSlot) -> Unit,
    onTimeSelected: (DateSlot, TimeSlot) -> Unit,
    onToggleExpanded: () -> Unit
) {
    val slotsAvailable = dateSlots.any { it.isAvailable() }
    val isExpandedState by remember(
        isExpanded,
        slotsAvailable
    ) { derivedStateOf { isExpanded && slotsAvailable } }

    val arrowRotationState by animateFloatAsState(targetValue = if (isExpandedState) 0f else 180f)
    val containerColor by animateColorAsState(if (isExpandedState) BrandTheme.colors.gray.light else BrandTheme.colors.gray.lighter)
    val strokeColor = if (!isExpandedState) BrandTheme.colors.gray.c200 else Color.Transparent

    val titleFontWeight = if (isExpandedState) FontWeight.SemiBold else FontWeight.Medium
    val titleColor = if (slotsAvailable) BrandTheme.colors.gray.dark else BrandTheme.colors.gray.normalDark
    val dateTimeTextColor = if (slotsAvailable && timeSlotSelectedId != null) {
        GreenDark
    } else {
        BrandTheme.colors.gray.normalDark
    }

    Column(
        modifier = modifier
            .border(1.dp, strokeColor, BrandTheme.shapes.card)
            .clip(BrandTheme.shapes.card)
            .background(containerColor)
            .noRippleClickable(onClick = {
                if (slotsAvailable) onToggleExpanded()
            })
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Column(
                modifier = Modifier
            ) {
                if (serviceDurationInHrs != null || !serviceImages.isNullOrEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (serviceDurationInHrs != null) {
                            Row(
                                modifier = Modifier
                                    .height(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BrandTheme.colors.gray.c200)
                                    .padding(horizontal = 6.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.ic_washing_machine),
                                    contentDescription = "Washing Machine Icon",
                                    modifier = Modifier.size(16.dp),
                                    tint = BrandTheme.colors.gray.c700
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    "$serviceDurationInHrs hrs",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = BrandTheme.colors.gray.c700
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        if (!serviceImages.isNullOrEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                serviceImages.forEach { imageUrl ->
                                    AsyncImage(
                                        modifier = Modifier.size(24.dp),
                                        model = ImageRequest.Builder(LocalPlatformContext.current)
                                            .data(imageUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = null,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = titleFontWeight,
                    lineHeight = 24.sp,
                    color = titleColor
                )

                val selectedTimeSlot =
                    dateSlots.firstOrNull { it.id == dateSlotSelectedId }?.timeSlots?.firstOrNull { it.id == timeSlotSelectedId }

                val dateTimeText = run {
                    if (!slotsAvailable) return@run "No available time slots"
                    if (selectedTimeSlot == null) return@run "Please select a time slot"
                    val timeText = formattedHourTime(
                        selectedTimeSlot.startTimeStamp,
                        selectedTimeSlot.endTimeStamp
                    )
                    val dateText = getDayOfWeekAbbrev(selectedTimeSlot.startTimeStamp)
                    "$dateText | $timeText"
                }

                val alpha by animateFloatAsState(if (isExpandedState) 0f else 1f)
                Text(
                    modifier = Modifier.alpha(alpha),
                    text = dateTimeText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 18.sp,
                    color = dateTimeTextColor
                )
            }

            if (slotsAvailable) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = if (isExpandedState) "Collapse" else "Expand",
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(arrowRotationState),
                    tint = BrandTheme.colors.gray.normalDark
                )
            }

        }

        // Expandable content
        AnimatedVisibility(
            visible = isExpandedState,
            enter = fadeIn(animationSpec = tween(300, delayMillis = 100)) +
                    expandVertically(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200)) +
                    shrinkVertically(animationSpec = tween(300, delayMillis = 100)),
        ) {
            Spacer(Modifier.height(8.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(8.dp))

                // Date row
                DateRow(
                    slots = dateSlots,
                    onClick = onDateSelected,
                    selectedDateId = dateSlotSelectedId,
                )

                Spacer(Modifier.height(16.dp))

                // Time slots
                val selectedDateSlot = dateSlots.firstOrNull {
                    it.id == dateSlotSelectedId
                }

                TimeSlotGrid(
                    modifier = Modifier.fillMaxWidth(),
                    dateSlot = selectedDateSlot,
                    selectedTimeId = timeSlotSelectedId,
                    onSlotSelected = { timeSlot ->
                        selectedDateSlot?.let { dateSlot ->
                            onTimeSelected(dateSlot, timeSlot)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}