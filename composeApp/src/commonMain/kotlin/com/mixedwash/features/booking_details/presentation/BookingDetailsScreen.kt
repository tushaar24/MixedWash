package com.mixedwash.features.booking_details.presentation

import BrandTheme
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.Route
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.booking.domain.model.BookingItemPricing
import com.mixedwash.core.booking.domain.model.BookingTimeSlot
import com.mixedwash.core.presentation.components.BottomBox
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.features.booking_details.presentation.components.BookingDetailsSummary
import com.mixedwash.features.booking_details.presentation.components.BookingItem
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.services.presentation.components.DefaultButtonLarge
import com.mixedwash.ui.theme.MixedWashTheme
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.dividerBlack
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.ui.theme.screenHorizontalPadding
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random


@Composable
fun BookingDetailsScreen(
    modifier: Modifier = Modifier,
    state: BookingDetailsScreenState,
    uiEventsFlow: Flow<BookingDetailsScreenUiEvent>,
    onEvent: (BookingDetailsScreenEvent) -> Unit,
    snackbarHandler: SnackbarHandler,
    navController: NavController
) {

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            is BookingDetailsScreenUiEvent.NavigateToBookingConfirmation -> {
                navController.navigate(Route.BookingConfirmationRoute(event.bookingId)) {
                    popUpTo(Route.HomeRoute)
                }
            }

            is BookingDetailsScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.snackbarPayload)
            }
        }
    }

    WindowInsetsContainer {

        val scrollState = rememberScrollState()
        val headerElevation by animateDpAsState(
            if (scrollState.value > 0) 4.dp else 0.dp,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )
        val footerElevation by animateDpAsState(
            if (scrollState.value < scrollState.maxValue) 4.dp else 0.dp,
            animationSpec = spring(stiffness = Spring.StiffnessLow)
        )

        Column(modifier = modifier) {
            DefaultHeader(
                title = state.title,
                headingSize = HeadingSize.Subtitle1,
                headingAlign = HeadingAlign.Start,
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        onClick = {}
                    )
                },
                headerElevation = headerElevation
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.weight(1f)
                    .padding(horizontal = screenHorizontalPadding)
                    .verticalScroll(scrollState)
                    .padding(top = headerContentSpacing)
            ) {

                if (state.pickupSlot != null && state.dropSlot != null && state.deliveryAddress != null) {
                    Text(
                        text = "Booking Details",
                        style = BrandTheme.typography.subtitle2
                    )
                    BookingDetailsSummary(
                        modifier = Modifier.fillMaxWidth(),
                        pickupSlot = state.pickupSlot,
                        dropSlot = state.dropSlot,
                        deliveryAddress = state.deliveryAddress,
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Service Details",
                    style = BrandTheme.typography.subtitle2
                )
                Column(
                    modifier = Modifier
                        .clip(BrandTheme.shapes.card)
                        .background(BrandTheme.colors.gray.light)
                        .padding(horizontal = 16.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.items.forEachIndexed { index, item ->

                        val description = when (val pricing = item.itemPricing) {
                            is BookingItemPricing.ServiceItemPricing -> {
                                "Min ₹${pricing.minimumPrice.div(100)} (${pricing.minimumUnits}${pricing.unit})"
                            }

                            is BookingItemPricing.SubItemFixedPricing -> {
                                "₹${pricing.fixedPrice.div(100)} x ${item.quantity}pc"
                            }

                            is BookingItemPricing.SubItemRangedPricing -> {
                                "₹${pricing.minPrice.div(100)} - ₹${pricing.maxPrice.div(100)} x ${item.quantity}pc"
                            }
                        }

                        val annotatedPriceText = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = BrandTheme.colors.gray.darker)) {
                                when (val pricing = item.itemPricing) {
                                    is BookingItemPricing.ServiceItemPricing -> {
                                        withStyle(SpanStyle(fontSize = 14.sp)) {
                                            append(
                                                "₹${
                                                    pricing.pricePerUnit.div(
                                                        100
                                                    )
                                                }"
                                            )
                                        }
                                        withStyle(SpanStyle(fontSize = 12.sp)) { append("/${pricing.unit}") }
                                    }

                                    is BookingItemPricing.SubItemFixedPricing -> {
                                        withStyle(SpanStyle(fontSize = 14.sp)) {
                                            append(
                                                "₹${
                                                    pricing.fixedPrice.div(
                                                        100
                                                    )
                                                }"
                                            )
                                        }
                                    }

                                    is BookingItemPricing.SubItemRangedPricing -> {
                                        withStyle(SpanStyle(fontSize = 14.sp)) {
                                            append(
                                                "₹${
                                                    pricing.minPrice.div(
                                                        100
                                                    )
                                                } - ₹${pricing.maxPrice.div(100)}"
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        val title = when (item.itemPricing) {
                            is BookingItemPricing.ServiceItemPricing -> item.name
                            else -> "${item.name} · ${item.serviceName}"
                        }

                        BookingItem(
                            title = title,
                            description = description,
                            annotatedPriceText = annotatedPriceText,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (index < state.items.size - 1) {
                            HorizontalDivider(
                                color = dividerBlack,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                state.note?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(vertical = 8.dp),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

            }
            if (state.screenType == BookingDetailsScreenType.CONFIRMATION) {
                BottomBox(elevation = footerElevation) {
                    DefaultButtonLarge(
                        modifier = Modifier
                            .fillMaxWidth().padding(horizontal = screenHorizontalPadding),
                        text = "CONFIRM ORDER",
                        onClick = { onEvent(BookingDetailsScreenEvent.OnConfirmBooking) }
                    )

                }
            }

        }
    }
}



//@Preview(showSystemUi = true)
@Composable
private fun PreviewOrderDetailsSummary() {
    MixedWashTheme {
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = screenHorizontalPadding)
        ) {
            BookingDetailsSummary(
                Modifier.align(Alignment.Center),
                deliveryAddress = Address(
                    title = "Office",
                    addressLine1 = "2342, Electronic City Phase 2",
                    addressLine2 = "Silicon Town, Bengaluru",
                    pinCode = "560100",
                    uid = "asnak"
                ),
                pickupSlot = BookingTimeSlot(
                    startTimeStamp = 1736933400L, // 9:30 AM
                    endTimeStamp = 1736944200L,
                    id = Random.nextInt(),   // 12:00 PM
                ),
                dropSlot = BookingTimeSlot(
                    startTimeStamp = 1736933400L, // 9:30 AM
                    endTimeStamp = 1736944200L,   // 12:00 PM
                    id = Random.nextInt()
                )
            )
        }
    }
}
