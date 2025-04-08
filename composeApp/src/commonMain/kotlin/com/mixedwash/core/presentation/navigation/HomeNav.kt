package com.mixedwash.core.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mixedwash.core.data.UserService
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.features.home.presentation.HomeScreen
import com.mixedwash.features.home.presentation.HomeScreenViewModel
import com.mixedwash.features.onboarding.presentation.OnboardingScreen
import com.mixedwash.features.onboarding.presentation.OnboardingScreenViewModel
import com.mixedwash.features.order_confirmation.presentation.OrderConfirmationScreen
import com.mixedwash.features.order_confirmation.presentation.OrderConfirmationScreenViewModel
import com.mixedwash.features.order_details.presentation.OrderDetailsScreen
import com.mixedwash.features.order_details.presentation.OrderDetailsScreenViewModel
import com.mixedwash.features.services.presentation.ServicesScreen
import com.mixedwash.features.services.presentation.ServicesScreenViewModel
import com.mixedwash.features.slot_selection.presentation.SlotSelectionScreen
import com.mixedwash.features.slot_selection.presentation.SlotSelectionScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.HomeNav(
    snackbarHandler: SnackbarHandler,
    navController: NavController,
    userService: UserService
){
    navigation<Route.HomeNav>(
        startDestination = Route.HomeRoute,
        enterTransition = { fadeIn() + scaleIn(initialScale = 0.8f) },
        exitTransition = { fadeOut() }
    ) {

        composable<Route.HomeRoute>(
            enterTransition = {
                when (initialState.destination.route?.substringBefore('?')) {
                    "services_route" -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(durationMillis = 500, easing = EaseOutExpo)
                        )
                    }
                    else -> fadeIn() + scaleIn(initialScale = 0.8f)
                }
            },
            exitTransition = {
                when (targetState.destination.route?.substringBefore('?')) {
                    "services_route" -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(durationMillis = 500, easing = EaseOutExpo)
                        )
                    }
                    else -> fadeOut()
                }
            }
        ) {
            val viewModel = koinViewModel<HomeScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            HomeScreen(
                state = state,
                onEvent = viewModel::onEvent,
                uiEvents = viewModel.uiEventsFlow,
                snackbarHandler = snackbarHandler,
                navController = navController,
            )
        }

        composable<Route.ServicesRoute>(
            enterTransition = {
                when (initialState.destination.route?.substringBefore('?')) {
                    "home_route" -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Start,
                            animationSpec = tween(durationMillis = 500, easing = EaseOutExpo)
                        )
                    }
                    else -> fadeIn() + scaleIn(initialScale = 0.8f)
                }
            },
            exitTransition = {
                when (targetState.destination.route?.substringBefore('?')) {
                    "home_route" -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.End,
                            animationSpec = tween(durationMillis = 500, easing = EaseOutExpo)
                        )
                    }
                    else -> fadeOut()
                }
            }
        ){
            val viewmodel = koinViewModel<ServicesScreenViewModel>()
            val state by viewmodel.state.collectAsStateWithLifecycle()
            ServicesScreen(
                state = state,
                onEvent = viewmodel::onEvent,
                uiEventsFlow = viewmodel.uiEventsFlow,
                snackbarHandler = snackbarHandler,
                navController = navController
            )
        }


        composable<Route.SlotSelectionRoute> {
            val viewModel = koinViewModel<SlotSelectionScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            SlotSelectionScreen(
                modifier = Modifier,
                state = state,
                uiEventsFlow = viewModel.uiEventsFlow,
                snackbarHandler = snackbarHandler,
                navController = navController,
            )

        }

        composable<Route.OrderDetailsRoute> {

            val viewModel = koinViewModel<OrderDetailsScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            OrderDetailsScreen(
                state = state,
                onEvent = viewModel::onEvent,
                snackbarHandler = snackbarHandler,
                navController = navController,
                uiEventsFlow = viewModel.uiEventsFlow
            )

        }

        composable<Route.OrderConfirmationRoute> {
            val viewModel = koinViewModel<OrderConfirmationScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            OrderConfirmationScreen(
                state = state,
                onEvent = viewModel::onEvent,
                uiEvents = viewModel.uiEventsFlow,
                navController = navController
            )
        }

        composable<Route.OnboardingRoute> {
            val viewModel = koinViewModel<OnboardingScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            OnboardingScreen(
                state = state,
                onEvent = viewModel::onEvent,
                uiEvents = viewModel.uiEventsFlow,
                navController = navController,
            )
        }
    }
}