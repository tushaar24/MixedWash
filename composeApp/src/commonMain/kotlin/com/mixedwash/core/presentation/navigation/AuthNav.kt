package com.mixedwash.core.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.mixedwash.Route
import com.mixedwash.features.common.presentation.phone.PhoneScreen
import com.mixedwash.features.common.presentation.phone.PhoneScreenViewModel
import com.mixedwash.features.common.presentation.signin.CarouselItem
import com.mixedwash.features.common.presentation.signin.SignInScreen
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.models.SnackbarPayload
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel


fun NavGraphBuilder.AuthNav(
    snackbarHandler: SnackbarHandler,
) {
    navigation<Route.AuthNav>(
        startDestination = Route.SignInRoute,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        composable<Route.SignInRoute> {

            val scope = rememberCoroutineScope()
            SignInScreen(
                carouselItems = listOf(
                    CarouselItem(
                        imageUrl = "https://assets-aac.pages.dev/assets/wooden_laundry_basket_next_to_pile_clothing.png",
                        title = "New Startup\nSame Laundry",
                        description = "no AI. no machine learning.\njust washing machine go brrr"
                    ),
                    CarouselItem(
                        imageUrl = "https://assets-aac.pages.dev/assets/washing_machine_sunglases.png",
                        title = "Do laundry the \nRIGHT WAY",
                        description = "seriously. stop sun-drying your dark clothes. baka."
                    ),
                    CarouselItem(
                        imageUrl = "https://assets-aac.pages.dev/assets/man_meditating.png",
                        title = "Save more time & \nDo more laundry",
                        description = "meditate. practice breathing. \ncontrol your chakra. learn rizz jutsu"
                    ),

                    ),
                onSignInSuccess = {
                    // Handled in App.kt
                },
                onSignInFailure = {
                    scope.launch {
                        snackbarHandler(snackbarPayload = SnackbarPayload("Sign In Failed"))
                    }
                }
            )
        }

        composable<Route.PhoneRoute> {
            val viewModel = koinViewModel<PhoneScreenViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            PhoneScreen(
                modifier = Modifier.padding(),
                state = state,
                snackbarHandler = snackbarHandler,
                uiEventsFlow = viewModel.uiEventsFlow,
                onEvent = { event -> viewModel.onEvent(event) },
                onSubmitSuccess = {
                    // Navigation handled by App.kt
                }
            )
        }

    }


}