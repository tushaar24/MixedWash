package com.mixedwash.core.presentation.navigation

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
import com.mixedwash.features.common.presentation.history.OrderHistoryScreen
import com.mixedwash.features.common.presentation.history.OrderHistoryScreenViewModel
import com.mixedwash.features.common.presentation.profile.ProfileEditScreen
import com.mixedwash.features.common.presentation.profile.ProfileEditScreenViewModel
import com.mixedwash.features.common.presentation.profile.ProfileScreen
import com.mixedwash.features.common.presentation.profile.ProfileScreenState
import com.mixedwash.features.common.presentation.profile.ProfileSection
import com.mixedwash.features.common.presentation.profile.ProfileSectionItem
import com.mixedwash.core.presentation.models.SnackbarHandler
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

fun NavGraphBuilder.ProfileNav(
    userService: UserService,
    snackbarHandler: SnackbarHandler,
    navController: NavController
) {
    navigation<Route.ProfileNav>(
        startDestination = Route.ProfileRoute
    ) {
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

        composable<Route.AddressRoute> {
            val addressViewModel = koinViewModel<AddressScreenViewModel>()
            val state by addressViewModel.state.collectAsState()
            AddressScreen(
                state = state,
                uiEventsFlow = addressViewModel.uiEventsFlow,
                snackbarHandler = snackbarHandler,
            )
        }

        composable<Route.HistoryRoute> {

            val viewModel = koinViewModel<OrderHistoryScreenViewModel>()
            val state by viewModel.uiState.collectAsStateWithLifecycle()
            OrderHistoryScreen(
                state = state,
                modifier = Modifier
            )
        }

    }


}