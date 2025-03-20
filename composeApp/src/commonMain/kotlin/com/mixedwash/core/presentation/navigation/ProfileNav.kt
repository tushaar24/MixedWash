package com.mixedwash.core.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mixedwash.Route
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.features.address.presentation.AddressScreenViewModel
import com.mixedwash.features.common.presentation.address.AddressScreen
import com.mixedwash.features.history.presentation.OrderHistoryScreen
import com.mixedwash.features.history.presentation.OrderHistoryScreenViewModel
import com.mixedwash.features.profile.profile_edit.ProfileEditScreen
import com.mixedwash.features.profile.profile_edit.ProfileEditScreenViewModel
import com.mixedwash.features.profile.presentation.ProfileScreen
import com.mixedwash.features.profile.presentation.ProfileScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.ProfileNav(
    snackbarHandler: SnackbarHandler,
    navController: NavController
) {
    navigation<Route.ProfileNav>(
        startDestination = Route.ProfileRoute
    ) {
        composable<Route.ProfileRoute> {
            val viewModel = koinViewModel<ProfileScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            ProfileScreen(
                state = state,
                onEvent = viewModel::onEvent,
                uiEvents = viewModel.uiEventsFlow,
                navController = navController
            )
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
                navController = navController
            )
        }

        composable<Route.HistoryRoute> {
            val viewModel = koinViewModel<OrderHistoryScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            OrderHistoryScreen(
                state = state,
                onEvent = viewModel::onEvent,
                uiEventsFlow = viewModel.uiEventsFlow,
                navController = navController
            )
        }
    }


}