package com.mixedwash.features.slot_selection.presentation

import BrandTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.core.presentation.util.getDayAndDate
import com.mixedwash.core.presentation.util.getMonth
import com.mixedwash.features.slot_selection.presentation.components.Coupon
import com.mixedwash.features.slot_selection.presentation.components.DateSlotButton
import com.mixedwash.features.slot_selection.presentation.components.DeliveryNotes
import com.mixedwash.features.slot_selection.presentation.components.Month
import com.mixedwash.features.slot_selection.presentation.components.TimeSlotGrid
import com.mixedwash.features.slot_selection.presentation.model.DateSlotPresentation
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.components.PrimaryButton
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.screenVerticalPadding
import kotlinx.coroutines.flow.Flow

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
                snackbarPayload = SnackbarPayload(
                    message = event.value,
                    type = event.type,
                    duration = SnackbarDuration.Short
                )
            )
        }
    }


    WindowInsetsContainer {
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

                    val onPickupDateClick: (DateSlotPresentation) -> Unit = remember(state.screenEvent) {
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

                    val onDropDateClick: (DateSlotPresentation) -> Unit = remember(state.screenEvent) {
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
}

@Composable
fun DateRow(slots: List<DateSlotPresentation>, onClick: (DateSlotPresentation) -> Unit, selectedDateId: Int? = null) {
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
