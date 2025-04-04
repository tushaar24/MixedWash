package com.mixedwash.features.slot_selection.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.ClickableLoadingOverlay
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.ElevatedShape
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.services.presentation.components.DefaultButtonLarge
import com.mixedwash.features.slot_selection.presentation.components.DeliveryNotes
import com.mixedwash.features.slot_selection.presentation.components.BookingSlotContainer
import com.mixedwash.features.slot_selection.presentation.components.PickupSlotContainer
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
                    Route.OrderDetailsRoute(
                        bookingId = null,
                        destinationType = Route.OrderDetailsRoute.DestinationType.CONFIRM_DRAFT_ORDER
                    ),
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
                title = state.screenTitle,
                headingSize = HeadingSize.Subtitle1,
                headingAlign = HeadingAlign.Start,
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        onClick = { navController.navigateUp() }
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
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionHeaderText("Pickup")

                    PickupSlotContainer(
                        pickupSlotState = state.pickupSlotState,
                        onDateSelected = { dateSlot ->
                            state.screenEvent(SlotSelectionScreenEvent.OnPickupDateSelected(dateSlot))
                        },
                        onTimeSelected = { dateSlot, timeSlot ->
                            state.screenEvent(
                                SlotSelectionScreenEvent.OnPickupTimeSelected(
                                    dateSlot = dateSlot,
                                    timeSlot = timeSlot
                                )
                            )
                        },
                        onTogglePickupExpanded = { state.screenEvent(SlotSelectionScreenEvent.OnTogglePickupExpanded) }
                    )
                }

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionHeaderText("Delivery")
                        Text(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            text = "we can deliver your items as soon as they’re ready, right after the minimum processing time",
                            lineHeight = 18.sp,
                            fontSize = 12.sp
                        )
                    }
                    // Show booking slots for each service group
                    state.bookingsSlotStates.forEach { bookingSlotState ->
                        BookingSlotContainer(
                            bookingSlotState = bookingSlotState,
                            onDateSelected = { dateSlot ->
                                state.screenEvent(
                                    SlotSelectionScreenEvent.OnBookingDateSelected(
                                        bookingId = bookingSlotState.id,
                                        dateSlot = dateSlot
                                    )
                                )
                            },
                            onTimeSelected = { dateSlot, timeSlot ->
                                state.screenEvent(
                                    SlotSelectionScreenEvent.OnBookingTimeSelected(
                                        bookingId = bookingSlotState.id,
                                        dateSlot = dateSlot,
                                        timeSlot = timeSlot
                                    )
                                )
                            },
                            onToggleBookingExpanded = { bookingId ->
                                state.screenEvent(
                                    SlotSelectionScreenEvent.OnToggleBookingExpanded(
                                        bookingId
                                    )
                                )
                            }
                        )
                    }
                }

                val onDeliveryNotesChange: (String) -> Unit = remember(state.screenEvent) {
                    { value ->
                        state.screenEvent(SlotSelectionScreenEvent.OnDeliveryNotesUpdate(value))
                    }
                }

                DeliveryNotes(
                    modifier = Modifier.padding(top = 8.dp),
                    text = state.deliveryNotes,
                    onValueChange = onDeliveryNotesChange
                )
            }

            ElevatedShape(scrollState = scrollState) {
                DefaultButtonLarge(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = screenHorizontalPadding),
                    text = "BOOK ORDER",
                    enabled = state.canSubmit(),
                    onClick = { state.screenEvent(SlotSelectionScreenEvent.OnSubmit) }
                )
            }

        }
    }
    ClickableLoadingOverlay(state.isLoading)
}

@Composable
private fun SectionHeaderText(text: String) {
    Text(
        text = text,
        lineHeight = 24.sp,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
    )
}