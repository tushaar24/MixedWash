package com.mixedwash

import BrandTheme
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.compose.rememberNavController
import com.mixedwash.core.data.AuthState
import com.mixedwash.core.data.UserService
import com.mixedwash.core.presentation.components.BrandSnackbar
import com.mixedwash.core.presentation.components.ShimmerText
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.core.presentation.components.rememberSnackbarHandler
import com.mixedwash.core.presentation.models.decodeSnackBar
import com.mixedwash.core.presentation.navigation.AuthNav
import com.mixedwash.core.presentation.navigation.HomeNav
import com.mixedwash.core.presentation.navigation.ProfileNav
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.features.history.presentation.OrderHistoryScreen
import com.mixedwash.features.history.presentation.OrderHistoryScreenViewModel
import com.mixedwash.features.support.presentation.FaqScreen
import com.mixedwash.features.support.presentation.FaqScreenViewModel
import com.mixedwash.ui.theme.MixedWashTheme
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
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
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
                                snackbarHandler = snackbarHandler
                            )

                            HomeNav(
                                snackbarHandler = snackbarHandler,
                                navController = navController,
                                userService = userService
                            )

                            ProfileNav(
                                userService = userService,
                                snackbarHandler = snackbarHandler,
                                navController = navController
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

                            composable<Route.HistoryRoute> {
                                val viewModel = koinViewModel<OrderHistoryScreenViewModel>()
                                val state by viewModel.state.collectAsStateWithLifecycle()
                                OrderHistoryScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent
                                )
                            }

                            composable<Route.FaqRoute> {
                                val viewModel = koinViewModel<FaqScreenViewModel>()
                                val state by viewModel.state.collectAsStateWithLifecycle()
                                FaqScreen(
                                    state = state,
                                    onEvent = viewModel::onEvent
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}