package com.mixedwash

import BrandTheme
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.mixedwash.core.data.AuthState
import com.mixedwash.core.data.UserService
import com.mixedwash.features.createOrder.presentation.address.AddressScreen
import com.mixedwash.features.createOrder.presentation.address.AddressScreenViewModel
import com.mixedwash.features.createOrder.presentation.address.model.Address
import com.mixedwash.features.createOrder.presentation.order_confirmation.OrderConfirmationScreen
import com.mixedwash.features.createOrder.presentation.order_confirmation.OrderConfirmationScreenState
import com.mixedwash.features.createOrder.presentation.order_review.OrderReviewScreen
import com.mixedwash.features.createOrder.presentation.order_review.OrderReviewScreenState
import com.mixedwash.features.createOrder.presentation.order_review.ServiceItem
import com.mixedwash.features.createOrder.presentation.slot_selection.Offer
import com.mixedwash.features.createOrder.presentation.slot_selection.SlotSelectionScreen
import com.mixedwash.features.createOrder.presentation.slot_selection.SlotSelectionScreenViewModel
import com.mixedwash.features.createOrder.presentation.slot_selection.TimeSlot
import com.mixedwash.features.profile.ProfileEditScreen
import com.mixedwash.features.profile.ProfileEditScreenViewModel
import com.mixedwash.features.profile.ProfileScreen
import com.mixedwash.features.profile.ProfileScreenState
import com.mixedwash.features.profile.ProfileSection
import com.mixedwash.features.profile.ProfileSectionItem
import com.mixedwash.presentation.components.BrandSnackbar
import com.mixedwash.presentation.components.ShimmerText
import com.mixedwash.presentation.components.noRippleClickable
import com.mixedwash.presentation.components.rememberSnackbarHandler
import com.mixedwash.presentation.models.decodeSnackBar
import com.mixedwash.presentation.navigation.AuthNav
import com.mixedwash.presentation.util.Logger
import com.mixedwash.ui.theme.MixedWashTheme
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
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MixedWashTheme {
        KoinContext {
            val scaffoldState = rememberBottomSheetScaffoldState(
                snackbarHostState = SnackbarHostState()
            )
            val snackbarHandler = rememberSnackbarHandler(scaffoldState)
            Surface {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(scaffoldState.snackbarHostState) { snackbarData ->
                            val (type, message) = decodeSnackBar(snackbarData.visuals.message)
                            val imeOffset = WindowInsets.ime.getBottom(LocalDensity.current)
                             /**
                              * TODO : FIX ME : Popup doesn't show on top of modal
                              * https://medium.com/@stefanoq21/accompanist-system-ui-controller-deprecated-a3678ba3f244
                              */
                            Popup(
                                alignment = Alignment.BottomCenter,
                                offset = IntOffset(x = 0, y = -imeOffset),
                                onDismissRequest = { snackbarData.dismiss() },
                                properties = PopupProperties()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .noRippleClickable {
                                            snackbarData.dismiss()
                                        },
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    BrandSnackbar(message = message, snackbarType = type, actionText = snackbarData.visuals.actionLabel, action = snackbarData::performAction)
                                }
                            }
                        }
                    },
                    contentColor = LocalContentColor.current,
                    containerColor = BrandTheme.colors.background,
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .imePadding()
                    ) {

                        val navController = rememberNavController()
                        val userService = koinInject<UserService>()
                        val authState by userService.authState.collectAsStateWithLifecycle(
                            AuthState.Loading
                        )
                        val userState by userService.userStateFlow.collectAsStateWithLifecycle()
                        Logger.d("TAG", "App.kt > userState : $userState \nauthState : $authState \nuserMetadata : ${userState?.userMetadata}")

                        var startDestination: Route by remember { mutableStateOf(Route.LoadingRoute) }
                        when (authState) {
                            is AuthState.Authenticated -> startDestination = Route.HomeNav
                            is AuthState.Loading -> {
                                startDestination = Route.LoadingRoute
                            }

                            is AuthState.Unauthenticated, AuthState.Authenticating -> {
                                startDestination = Route.AuthNav
                                if (authState is AuthState.Unauthenticated) {
                                    navController.navigate(Route.SignInRoute) { popUpTo(Route.AuthNav) }
                                } else {
                                    navController.navigate(Route.PhoneRoute) { popUpTo(Route.AuthNav) }
                                }
                            }
                        }

                        NavHost(
                            navController = navController,
                            startDestination = startDestination
                        ) {

                            AuthNav(
                                snackbarHandler = snackbarHandler,
                            )

                            composable<Route.LoadingRoute>(
                                enterTransition = { scaleIn(initialScale = 0.8f) },
                                exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessVeryLow)) }
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ShimmerText(
                                        "Mixed Wash",
                                        isShimmering = true,
                                        style = BrandTheme.typography.h4,
                                        durationMillis = 1500,
                                        delayMillis = 0,
                                        color = BrandTheme.colors.background,
                                        shimmerColor = BrandTheme.colors.gray.normalDark
                                    )
                                }
                            }


                            navigation<Route.HomeNav>(
                                startDestination = Route.ProfileRoute,
                                enterTransition = { fadeIn() + scaleIn(initialScale = 0.8f) },
                                exitTransition = { fadeOut() }
                            ) {
                                composable<Route.AddressRoute> {
                                    val addressViewModel = koinViewModel<AddressScreenViewModel>()
                                    val state by addressViewModel.state.collectAsState()
                                    AddressScreen(
                                        state = state,
                                        uiEventsFlow = addressViewModel.uiEventsFlow,
                                        snackbarHandler = snackbarHandler,
                                    )
                                }

                                composable<Route.ProfileRoute> {
                                    val scope = rememberCoroutineScope()
                                    val metadata =
                                        userService.currentUser?.userMetadata ?: return@composable

                                    val state = ProfileScreenState(
                                        imageUrl = metadata.photoUrl, // Placeholder
                                        name = metadata.name,
                                        email = metadata.email,
                                        phone = metadata.phoneNumber,
                                        onEditProfile = { navController.navigate(Route.ProfileEditRoute) },
                                        onLogout = { scope.launch { userService.signOut() } },
                                        appName = "MixedWash",
                                        appVersion = "V.0.1-beta",
                                        sections = listOf(
                                            ProfileSection(
                                                title = "Manage",
                                                items = listOf(
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_clothes_hanger,
                                                        text = "Order History",
                                                        onClick = { }),
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_location,
                                                        text = "Address Book",
                                                        onClick = { })
                                                )
                                            ),
                                            ProfileSection(
                                                title = "Payment",
                                                items = listOf(
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_upi,
                                                        text = "Payment Methods ",
                                                        onClick = { },
                                                        comingSoon = true
                                                    ),
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_reward,
                                                        text = "Referrals and Rewards ",
                                                        onClick = { },
                                                        comingSoon = true
                                                    ),

                                                    )
                                            ),
                                            ProfileSection(
                                                title = "Support",
                                                items = listOf(
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_chat,
                                                        text = "Help Center ",
                                                        onClick = { },
                                                        comingSoon = true
                                                    ),
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_thumbs,
                                                        text = "Share Feedback ",
                                                        onClick = { })
                                                )
                                            ),
                                            ProfileSection(
                                                title = "Other",
                                                items = listOf(
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_share,
                                                        text = "Share App",
                                                        onClick = { }),
                                                    ProfileSectionItem(
                                                        resource = Res.drawable.ic_info,
                                                        text = "About Us",
                                                        onClick = { })
                                                )
                                            )
                                        )
                                    )

                                    ProfileScreen(state = state)
                                }

                                composable<Route.ProfileEditRoute> {
                                    val viewmodel = koinViewModel<ProfileEditScreenViewModel>()
                                    val state by viewmodel.state.collectAsStateWithLifecycle()
                                    ProfileEditScreen(
                                        modifier = Modifier,
                                        state = state,
                                        uiEventsFlow = viewmodel.uiEventsFlow,
                                        snackbarHandler = snackbarHandler,
                                        navigateBack = { navController.popBackStack() },
                                        onEvent = viewmodel::onEvent
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
                                            ServiceItem(
                                                title = "Shirt Wash & Iron",
                                                description = "Professional cleaning and ironing service for shirts.",
                                                price = "$5.00",
                                                unit = "kg",
                                                actionLabel = "Remove",
                                                action = { println("Remove Shirt Wash & Iron clicked") }
                                            ),
                                            ServiceItem(
                                                title = "Pant Dry Clean",
                                                description = "Delicate dry cleaning for pants.",
                                                price = "$7.50",
                                                unit = "kg",
                                                actionLabel = "Remove",
                                                action = { println("Remove Pant Dry Clean clicked") }
                                            ),
                                            ServiceItem(
                                                title = "Suit Steam Press",
                                                description = "Gentle steam pressing for suits.",
                                                price = "$12.00",
                                                unit = "kg",
                                                actionLabel = "Remove",
                                                action = { println("Remove Suit Steam Press clicked") }
                                            ),
                                            ServiceItem(
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
                    }
                }
            }
        }
    }
}