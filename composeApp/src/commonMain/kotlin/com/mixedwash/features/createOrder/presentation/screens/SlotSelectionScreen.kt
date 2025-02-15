package com.mixedwash.features.createOrder.presentation.screens

import BrandTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil3.Uri
import coil3.compose.AsyncImage
import com.mixedwash.domain.util.capitalize
import com.mixedwash.features.createOrder.presentation.models.SlotSelectionScreenEvent
import com.mixedwash.features.createOrder.presentation.models.SlotSelectionScreenUiEvent
import com.mixedwash.headerContentSpacing
import com.mixedwash.presentation.components.DefaultHeader
import com.mixedwash.presentation.components.HeadingAlign
import com.mixedwash.presentation.components.HeadingSize
import com.mixedwash.presentation.components.clearFocusOnKeyboardDismiss
import com.mixedwash.presentation.models.SnackbarHandler
import com.mixedwash.presentation.util.ObserveAsEvents
import com.mixedwash.presentation.util.formatTime
import com.mixedwash.presentation.util.getDayAndDate
import com.mixedwash.presentation.util.getMonth
import com.mixedwash.screenHorizontalPadding
import com.mixedwash.screenVerticalPadding
import com.mixedwash.ui.theme.Green
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.defaults.HeaderIconButton
import com.mixedwash.ui.theme.defaults.PrimaryButton
import kotlinx.coroutines.flow.Flow
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.camera
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SlotSelectionScreen(
    modifier: Modifier = Modifier,
    state: SlotSelectionScreenState,
    uiEventsFlow: Flow<SlotSelectionScreenUiEvent>,
    snackbarHandler: SnackbarHandler
) {

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is SlotSelectionScreenUiEvent.ShowSnackbar -> snackbarHandler(
                event.value, event.type, SnackbarDuration.Short
            )
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        DefaultHeader(
            title = state.title,
            headingSize = HeadingSize.Subtitle1,
            headingAlign = HeadingAlign.Start,
            navigationButton = {
                HeaderIconButton(
                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                    onClick = {}
                )
            }
        )
        Spacer(modifier = Modifier.height(headerContentSpacing))


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(
                    start = screenHorizontalPadding,
                    end = screenHorizontalPadding,
                    bottom = screenVerticalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(
                32.dp, alignment = Alignment.CenterVertically
            )
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Text("Select Pickup Slot", style = BrandTheme.typography.subtitle1)

                val onPickupDateClick: (DateSlot) -> Unit = remember(state.screenEvent) {
                    { dateSlot ->
                        state.screenEvent(SlotSelectionScreenEvent.OnPickupDateSelected(dateSlot))
                    }
                }

                DateRow(
                    slots = state.pickupSlots, onClick = onPickupDateClick,
                    selectedDateId = state.pickupDateSelectedId
                )
                TimeSlotGrid(
                    modifier = Modifier.fillMaxWidth(),
                    dateSlot = state.pickupSlots.firstOrNull { it.id == state.pickupDateSelectedId },
                    selectedTimeId = state.pickupTimeSelectedId,
                    onSlotSelected = {
                        state.screenEvent(SlotSelectionScreenEvent.OnPickupTimeSelected(it))
                    }
                )


            }
            HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Select Drop Slot", style = BrandTheme.typography.subtitle1)

                val onDropDateClick: (DateSlot) -> Unit = remember(state.screenEvent) {
                    { dateSlot ->
                        state.screenEvent(SlotSelectionScreenEvent.OnDropDateSelect(dateSlot))
                    }
                }

                DateRow(
                    slots = state.dropSlots, onClick = onDropDateClick,
                    selectedDateId = state.dropDateSelectedId
                )


                TimeSlotGrid(modifier = Modifier.fillMaxWidth(),
                    dateSlot = state.dropSlots.firstOrNull { it.id == state.dropDateSelectedId },
                    selectedTimeId = state.dropTimeSelectedId,
                    onSlotSelected = {
                        state.screenEvent(SlotSelectionScreenEvent.OnDropTimeSelected(it))
                    })

            }

            HorizontalDivider(color = Color.Black.copy(alpha = 0.05f))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Pick offers available", style = BrandTheme.typography.subtitle1)

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.animateContentSize()
                ) {
                    state.getOffers().forEach { offer ->
                        Coupon(
                            title = offer.title,
                            subtitle = offer.subtitle,
                            selected = offer.code == state.selectedOfferCode,
                            onClick = {
                                state.screenEvent(
                                    SlotSelectionScreenEvent.OnOfferSelected(
                                        offer
                                    )
                                )
                            },
                            icon = offer.icon
                        )
                    }
                }

            }

            val onDeliveryNotesChange: (String) -> Unit = remember(state.screenEvent) {
                { value ->
                    state.screenEvent(SlotSelectionScreenEvent.OnDeliveryNotesChange(value))
                }
            }

            DeliveryNotes(
                modifier = Modifier.padding(top = 8.dp),
                text = state.deliveryNotes,
                onValueChange = onDeliveryNotesChange
            )


            val isEnabled by remember(state.pickupTimeSelectedId, state.dropTimeSelectedId) {
                derivedStateOf {
                    state.pickupTimeSelectedId != null && state.dropTimeSelectedId != null
                }
            }
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = "BOOK ORDER",
                enabled = isEnabled
            )

        }


    }
}

@Composable
fun DateRow(slots: List<DateSlot>, onClick: (DateSlot) -> Unit, selectedDateId: Int? = null) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val datesByMonth = slots.groupBy { it.timeStamp.getMonth() }
        datesByMonth.forEach { (month, dates) ->
            item {
                Month(month = month)
            }
            items(dates.size, key = { index -> dates[index].timeStamp }) { index ->
                val dateSlot = dates[index]
                val (day, date) = dateSlot.timeStamp.getDayAndDate()
                DateSlotButton(day = day,
                    date = date,
                    selected = dateSlot.id == selectedDateId,
                    disabled = !dateSlot.isAvailable(),
                    onClick = { onClick(dateSlot) }
                )
            }
        }
    }

}

@Composable
fun SlotsEmpty() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        text = "No slots found.",
        textAlign = TextAlign.Center,
        style = BrandTheme.typography.body2
    )
}

@Composable
fun TimeSlotGrid(
    modifier: Modifier = Modifier,
    dateSlot: DateSlot?,
    selectedTimeId: Int?,
    onSlotSelected: (TimeSlot) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.animateContentSize()
    ) {
        if (dateSlot != null) {
            val timeSlots = dateSlot.timeSlots
            var index = 0

            while (index < timeSlots.size) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    repeat(2) { offset ->
                        val actualIndex = index + offset
                        if (actualIndex >= timeSlots.size) {
                            Spacer(Modifier.weight(1f))
                            return@Row
                        }
                        val timeSlot = timeSlots[index + offset]
                        val (startTime, startSuffix) = timeSlot.startTimeStamp.formatTime()
                        val (endTime, endSuffix) = timeSlot.endTimeStamp.formatTime()


                        TimeSlotButton(
                            modifier = Modifier.weight(1f),
                            startTime = startTime,
                            startTimeSuffix = startSuffix,
                            endTime = endTime,
                            endTimeSuffix = endSuffix,
                            selected = timeSlot.id == selectedTimeId,
                            unavailable = !timeSlot.isAvailable,
                            caption = when {
                                !timeSlot.isAvailable -> "slot is full"
                                timeSlot.offersAvailable.isNotEmpty() -> "${timeSlot.offersAvailable.size} offer${if (timeSlot.offersAvailable.size > 1) "s" else ""}"
                                else -> null
                            },
                            onClick = { onSlotSelected(timeSlot) })
                    }
                }
                index += 2
            }

        } else SlotsEmpty()
    }

}


@Composable
fun DeliveryNotes(
    modifier: Modifier = Modifier,
    placeHolder: String = "Add notes for the delivery driver",
    text: String,
    onValueChange: (String) -> Unit,
    imageUri: Uri? = null,
    onCameraClick: (() -> Unit)? = null,
    onImageClick: (() -> Unit)? = null,
) {


    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = BrandTheme.shapes.textField)
            .border(
                border = BorderStroke(0.5.dp, BrandTheme.colors.gray.normal),
                shape = BrandTheme.shapes.textField
            )
            .padding(
                start = 16.dp, end = 12.dp, top = 18.dp, bottom = 18.dp
            )
    ) {


        var hasFocus by remember { mutableStateOf(false) }
        BasicTextField(value = text.ifBlank {
            if (hasFocus) "" else placeHolder
        },
            onValueChange = onValueChange,
            modifier = Modifier
                .onFocusChanged { hasFocus = it.hasFocus }
                .clearFocusOnKeyboardDismiss()
                .fillMaxWidth()
                .weight(1f),
            maxLines = 2, // Allow overflow content
            minLines = 2,
            textStyle = BrandTheme.typography.body2.copy(color = if (text.isBlank()) BrandTheme.colors.gray.normalDark else Color.Unspecified),
            singleLine = false)
        if (onCameraClick == null && onImageClick == null) return
        AnimatedContent(imageUri, label = "load image") {
            when (it) {
                null -> {
                    Box(
                        modifier = Modifier
                            .clip(BrandTheme.shapes.chip)
                            .clickable(
                                enabled = onCameraClick != null, onClick = onCameraClick!!
                            )
                            .height(48.dp)
                            .width(48.dp)
                    ) {

                        Icon(
                            imageVector = vectorResource(Res.drawable.camera),
                            contentDescription = "camera icon",
                            modifier = Modifier
                                .size(size = 32.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .clip(BrandTheme.shapes.chip)
                            .background(Color.Black)
                            .clickable(
                                enabled = onImageClick != null, onClick = onImageClick!!
                            )
                            .height(48.dp)
                            .width(48.dp)
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Uploaded Image",
                            clipToBounds = true,
                            alpha = 0.7f,
                            modifier = Modifier.clip(BrandTheme.shapes.chip),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.TopStart)
                                .clip(BrandTheme.shapes.chip),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Remove Image",
                            tint = BrandTheme.colors.gray.light,
                        )
                    }
                }

            }
        }
    }
}


@Composable
fun Coupon(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector = Icons.Rounded.ShoppingCart
) {
    val containerColor = if (selected) Green else Color.Transparent
    val strokeColor =
        if (selected) Green else BrandTheme.colors.gray.normal

    val contentColor = if (selected) BrandTheme.colors.gray.light else BrandTheme.colors.gray.dark

    val iconColor = if (selected) BrandTheme.colors.gray.light else Green

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = BrandTheme.shapes.card)
            .border(
                border = BorderStroke(0.5.dp, strokeColor), shape = BrandTheme.shapes.card
            )
            .background(containerColor)
            .clickable(enabled = !selected, onClick = onClick)
            .padding(
                start = 16.dp, end = 16.dp, top = 18.dp, bottom = 18.dp
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Coupon Icon",
            modifier = Modifier.size(34.dp),
            tint = iconColor
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
            modifier = Modifier.weight(weight = 1f)
        ) {
            Text(
                text = title,
                style = BrandTheme.typography.subtitle3,
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = BrandTheme.typography.caption1.copy(lineHeight = 1.em),
                color = contentColor
            )
        }
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.size(14.dp),
            colors = RadioButtonColors(
                selectedColor = iconColor,
                unselectedColor = BrandTheme.colors.gray.normal,
                disabledSelectedColor = BrandTheme.colors.gray.light,
                disabledUnselectedColor = BrandTheme.colors.gray.light
            )
        )
    }
}

//@Preview(widthDp = 370, heightDp = 72)
@Composable
private fun CouponPreview() {
    MixedWashTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                8.dp, alignment = Alignment.CenterVertically
            )
        ) {
            var selectedIndex by remember {
                mutableIntStateOf(0)
            }
            Coupon(title = "30₹ Cashback on SBI Credit Card",
                subtitle = "On all orders above 3kg",
                selected = selectedIndex == 0,
                onClick = { selectedIndex = 0 })
            Coupon(title = "30₹ Cashback on SBI Credit Card",
                subtitle = "On all orders above 3kg",
                selected = selectedIndex == 1,
                onClick = { selectedIndex = 1 })

        }
    }
}

@Composable
fun TimeSlotButton(
    modifier: Modifier = Modifier,
    caption: String? = null,
    startTime: String,
    startTimeSuffix: String,
    endTime: String,
    endTimeSuffix: String,
    onClick: () -> Unit = {},
    selected: Boolean = false,
    unavailable: Boolean = false,
) {
    val topPadding = if (caption.isNullOrBlank()) 0.dp else 4.dp
    val contentColor = if (unavailable) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        BrandTheme.colors.gray.dark
    } else {
        BrandTheme.colors.gray.light
    }

    val strokeColor = if (unavailable) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        BrandTheme.colors.gray.normal
    } else {
        Color.Transparent
    }

    val containerColor = if (selected) {
        BrandTheme.colors.gray.dark
    } else {
        Color.Transparent
    }

    val subtitleColor = if (unavailable) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        Green
    } else {
        BrandTheme.colors.gray.light
    }

    val clickable = if (unavailable) {
        false
    } else if (!selected) {
        true
    } else {
        false
    }



    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredHeight(height = 58.dp)
            .clip(shape = BrandTheme.shapes.card)
            .border(
                border = BorderStroke(0.5.dp, strokeColor), shape = BrandTheme.shapes.card
            )
            .clickable(enabled = clickable, onClick = onClick)
            .background(containerColor)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = topPadding)
        ) {
            val text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(startTime) }
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(" $startTimeSuffix  -  ") }
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(endTime) }
                withStyle(
                    style = SpanStyle(
                        color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.Medium
                    )
                ) { append(" $endTimeSuffix") }
            }
            Text(
                text = text, modifier = Modifier, overflow = TextOverflow.Clip, maxLines = 1
            )
            caption?.let {
                Text(
                    text = it,
                    color = subtitleColor,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 12.sp, lineHeight = 1.em
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimeSlotButtonPreview() {
    MixedWashTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                8.dp, alignment = Alignment.CenterVertically
            )
        ) {
            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = true,
                unavailable = false,
                caption = "10% Off"
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = false,
                caption = "1 offer"
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = false,
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = true,
                caption = "slot is full"
            )

            TimeSlotButton(
                startTime = "10:00",
                startTimeSuffix = "AM",
                endTime = "11:00",
                endTimeSuffix = "AM",
                onClick = {},
                selected = false,
                unavailable = true,
            )


        }
    }
}

@Composable
fun DateSlotButton(
    modifier: Modifier = Modifier,
    day: String,
    date: String,
    selected: Boolean = false,
    disabled: Boolean = false,
    onClick: () -> Unit
) {
    val containerColor = if (disabled || !selected) {
        Color.Transparent
    } else {
        BrandTheme.colors.gray.dark
    }
    val contentColor = if (disabled) {
        BrandTheme.colors.gray.normal
    } else if (!selected) {
        BrandTheme.colors.gray.dark
    } else {
        BrandTheme.colors.gray.lighter
    }

    val clickable = !(disabled || selected)
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .requiredWidth(width = 46.dp)
            .requiredHeight(height = 58.dp)
            .clip(shape = BrandTheme.shapes.chip)
            .clickable(enabled = clickable, onClick = onClick)
            .background(color = containerColor)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = date,
            color = contentColor,
            textAlign = TextAlign.Center,
            style = BrandTheme.typography.largeButton.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = day.capitalize(),
            color = contentColor,
            textAlign = TextAlign.Center,
            style = BrandTheme.typography.tinyButton,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Month(modifier: Modifier = Modifier, month: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .requiredWidth(width = 46.dp)
            .requiredHeight(height = 58.dp)
            .clip(shape = BrandTheme.shapes.chip)
            .background(color = BrandTheme.colors.gray.light)
    ) {
        Text(
            text = month.uppercase(),
            color = BrandTheme.colors.gray.dark,
            textAlign = TextAlign.Center,
            style = BrandTheme.typography.smallButton,
            modifier = Modifier
                .rotate(-90f)
                .fillMaxWidth()
        )
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MonthPreview() {
    MixedWashTheme {
        Box(Modifier.fillMaxSize()) {
            Month(Modifier.align(Alignment.Center), "Jan")
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDateSlot() {
    MixedWashTheme {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                8.dp, alignment = Alignment.CenterHorizontally
            )
        ) {
            DateSlotButton(
                day = "Sat",
                date = "29",
                selected = true,
                disabled = false,
                onClick = {})
            DateSlotButton(
                day = "Sat",
                date = "29",
                selected = false,
                disabled = false,
                onClick = {})
            DateSlotButton(
                day = "Sat",
                date = "29",
                selected = true,
                disabled = true,
                onClick = {})
        }
    }
}
