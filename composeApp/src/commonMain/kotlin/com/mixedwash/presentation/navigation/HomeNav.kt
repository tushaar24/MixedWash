package com.mixedwash.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mixedwash.Route
import com.mixedwash.core.data.UserService
import com.mixedwash.features.common.presentation.address.AddressScreen
import com.mixedwash.features.common.presentation.address.AddressScreenViewModel
import com.mixedwash.features.common.presentation.address.model.Address
import com.mixedwash.features.common.presentation.home.HomeScreen
import com.mixedwash.features.common.presentation.order_confirmation.OrderConfirmationScreen
import com.mixedwash.features.common.presentation.order_confirmation.OrderConfirmationScreenState
import com.mixedwash.features.common.presentation.order_review.OrderReviewScreen
import com.mixedwash.features.common.presentation.order_review.OrderReviewScreenState
import com.mixedwash.features.common.presentation.order_review.ServiceSummary
import com.mixedwash.features.common.presentation.slot_selection.Offer
import com.mixedwash.features.common.presentation.slot_selection.SlotSelectionScreen
import com.mixedwash.features.common.presentation.slot_selection.SlotSelectionScreenViewModel
import com.mixedwash.features.common.presentation.slot_selection.TimeSlot
import com.mixedwash.features.common.presentation.profile.ProfileEditScreen
import com.mixedwash.features.common.presentation.profile.ProfileEditScreenViewModel
import com.mixedwash.features.common.presentation.profile.ProfileScreen
import com.mixedwash.features.common.presentation.profile.ProfileScreenState
import com.mixedwash.features.common.presentation.profile.ProfileSection
import com.mixedwash.features.common.presentation.profile.ProfileSectionItem
import com.mixedwash.features.common.presentation.services.ServicesScreen
import com.mixedwash.features.common.presentation.services.ServicesScreenViewModel
import com.mixedwash.presentation.models.SnackbarHandler
import kotlinx.coroutines.launch
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_chat
import mixedwash.composeapp.generated.resources.ic_clothes_hanger
import mixedwash.composeapp.generated.resources.ic_info
import mixedwash.composeapp.generated.resources.ic_location
import mixedwash.composeapp.generated.resources.ic_reward
import mixedwash.composeapp.generated.resources.ic_share
import mixedwash.composeapp.generated.resources.ic_thumbs
import mixedwash.composeapp.generated.resources.ic_upi
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.HomeNav(
    snackbarHandler: SnackbarHandler,
    navController: NavController,
    userService: UserService,
){
    navigation<Route.HomeNav>(
        startDestination = Route.HomeRoute,
        enterTransition = { fadeIn() + scaleIn(initialScale = 0.8f) },
        exitTransition = { fadeOut() }
    ) {

        composable<Route.HomeRoute> {
            HomeScreen()
        }

        composable<Route.ServicesRoute>{
            val viewmodel = koinViewModel<ServicesScreenViewModel>()
            val state by viewmodel.uiState.collectAsStateWithLifecycle()
            ServicesScreen(
                state = state,
                onEvent = viewmodel::onEvent,
                modifier = Modifier
            )
        }


        composable<Route.SlotSelectionRoute> {
            val viewModel = koinViewModel<SlotSelectionScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            SlotSelectionScreen(
                modifier = Modifier,
                state = state,
                uiEventsFlow = viewModel.uiEventsFlow,
                snackbarHandler = snackbarHandler
            )

        }

        composable<Route.OrderReviewRoute> {

            val state = OrderReviewScreenState(
                items = listOf(
                    ServiceSummary(
                        title = "Shirt Wash & Iron",
                        description = "Professional cleaning and ironing service for shirts.",
                        price = "$5.00",
                        unit = "kg",
                        actionLabel = "Remove",
                        action = { println("Remove Shirt Wash & Iron clicked") }
                    ),
                    ServiceSummary(
                        title = "Pant Dry Clean",
                        description = "Delicate dry cleaning for pants.",
                        price = "$7.50",
                        unit = "kg",
                        actionLabel = "Remove",
                        action = { println("Remove Pant Dry Clean clicked") }
                    ),
                    ServiceSummary(
                        title = "Suit Steam Press",
                        description = "Gentle steam pressing for suits.",
                        price = "$12.00",
                        unit = "kg",
                        actionLabel = "Remove",
                        action = { println("Remove Suit Steam Press clicked") }
                    ),
                    ServiceSummary(
                        title = "Flat 10% OFF on total bill",
                        description = "Slot Coupon",
                        price = "",
                        unit = "",
                        actionLabel = "Edit",
                        action = { println("Remove Suit Steam Press clicked") }
                    )
                ),
                deliveryAddress = Address(
                    title = "Office",
                    addressLine1 = "2342, Electronic City Phase 2",
                    addressLine2 = "Silicon Town, Bengaluru",
                    pinCode = "560100",
                    uid = "asnak"
                ),
                pickupSlot = TimeSlot(
                    startTimeStamp = 1736933400L, // 9:30 AM
                    endTimeStamp = 1736944200L,   // 12:00 PM
                    isAvailable = true, offersAvailable = listOf(
                        Offer(
                            title = "Flat 10% OFF",
                            subtitle = "10% off on SBI Credit Card",
                            code = "10%OFFSBI"
                        )
                    )
                ),
                dropSlot = TimeSlot(
                    startTimeStamp = 1736933400L, // 9:30 AM
                    endTimeStamp = 1736944200L,   // 12:00 PM
                    isAvailable = true, offersAvailable = listOf(
                        Offer(
                            title = "Flat 10% OFF",
                            subtitle = "10% off on SBI Credit Card",
                            code = "10%OFFSBI"
                        )
                    )
                ),
                paymentBreakup = listOf(
                    "Subtotal" to "$24.50",
                    "Tax" to "$2.45",
                    "Discount" to "-$3.00",
                    "Total" to "$23.95"
                ),
                onEditSlot = {
                    println("Edit Slot clicked")
                },
                onEditAddress = {
                    println("Edit Address clicked")
                }
            )

            OrderReviewScreen(state = state)

        }

        composable<Route.OrderConfirmationRoute> {
            val state = OrderConfirmationScreenState(
                onBackHome = { },
                title = "Order Placed!",
                description = "Thank you for placing an order with us. You will receive an email confirmation shortly.",
                onCheckOrderStatus = { }
            )
            OrderConfirmationScreen(state = state)
        }


    }
}