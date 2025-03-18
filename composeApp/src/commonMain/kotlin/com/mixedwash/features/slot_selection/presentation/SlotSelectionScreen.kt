package com.mixedwash.features.slot_selection.presentation

import BrandTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.Route
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.BottomBox
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.services.presentation.components.DefaultButtonLarge
import com.mixedwash.features.slot_selection.domain.model.response.DateSlot
import com.mixedwash.features.slot_selection.presentation.components.Coupon
import com.mixedwash.features.slot_selection.presentation.components.DateRow
import com.mixedwash.features.slot_selection.presentation.components.DeliveryNotes
import com.mixedwash.features.slot_selection.presentation.components.TimeSlotGrid
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.ui.theme.screenHorizontalPadding
import com.mixedwash.ui.theme.screenVerticalPadding
import kotlinx.coroutines.flow.Flow

@Composable
fun SlotSelectionScreen(
    modifier: Modifier = Modifier,
    state: SlotSelectionScreenState,
    uiEventsFlow: Flow<SlotSelectionScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    navController: NavController
) {

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is SlotSelectionScreenUiEvent.ShowSnackbar -> {
                Logger.d("SlotSelectionScreen", "ShowSnackbar")
                snackbarHandler(
                    snackbarPayload = SnackbarPayload(
                        message = event.value,
                        type = event.type,
                        duration = SnackbarDuration.Short
                    )
                )
            }

            is SlotSelectionScreenUiEvent.NavigateToReview -> {
                Logger.d("SlotSelectionScreen", "NavigateToReview")
                navController.navigate(
                    Route.BookingDetailsRoute(
                        bookingId = null,
                        destinationType = Route.BookingDetailsRoute.DestinationType.CONFIRM_DRAFT_BOOKING
                    )
                )

            }
        }
    }

    WindowInsetsContainer {
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
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

            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(
                        top = headerContentSpacing,
                        start = screenHorizontalPadding,
                        end = screenHorizontalPadding,
                        bottom = screenVerticalPadding
                    ),
                verticalArrangement = Arrangement.spacedBy(
                    32.dp, alignment = Alignment.Top
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Text(
                        "Pickup Slot",
                        style = BrandTheme.typography.subtitle1.copy(lineHeight = 24.sp)
                    )
                    Spacer(Modifier.height(2.dp))

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

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        "Drop Slot",
                        style = BrandTheme.typography.subtitle1.copy(lineHeight = 24.sp)
                    )
                    Spacer(Modifier.height(2.dp))

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

                val offers = state.getOffers()
                if (offers.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Offers", style = BrandTheme.typography.subtitle1)

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.animateContentSize()
                        ) {
                            offers.forEach { offer ->
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


            }
            val isEnabled by remember(state.pickupTimeSelectedId, state.dropTimeSelectedId) {
                derivedStateOf {
                    state.pickupTimeSelectedId != null && state.dropTimeSelectedId != null
                }
            }
            val elevation by animateDpAsState(if (scrollState.value < scrollState.maxValue) 4.dp else 0.dp)
            BottomBox(elevation = elevation) {
                DefaultButtonLarge(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = screenHorizontalPadding),
                    text = "BOOK ORDER",
                    enabled = isEnabled,
                    onClick = { state.screenEvent(SlotSelectionScreenEvent.OnSubmit) }
                )
            }

        }
    }
}