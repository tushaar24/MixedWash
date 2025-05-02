package com.mixedwash.core.presentation.navigation

import androidx.navigation.NavController
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NavArgs(
    val navType: NavArgType
)

@Serializable
sealed class NavArgType {
    @Serializable
    @SerialName("navigate")
    data class Navigate(
        val route: Route,
        val popUpOption: PopUpOption? = null,
        val launchSingleTop: Boolean = false
    ) : NavArgType()

    @Serializable
    @SerialName("navigate_up")
    data object NavigateUp : NavArgType()
}

@Serializable
sealed class PopUpOption {
    @Serializable
    @SerialName("pop_current_route")
    data object PopCurrentRoute : PopUpOption()
    @Serializable
    @SerialName("pop_to_route")
    data class PopToRoute(val route: Route, val isInclusive: Boolean = false) : PopUpOption()
}

fun NavController.navigateWithArgs(args: NavArgs) {
    when (val nav = args.navType) {
        is NavArgType.NavigateUp -> navigateUp()
        is NavArgType.Navigate -> {
            navigate(nav.route) {
                nav.popUpOption?.let { popUpOption ->
                    when (popUpOption) {
                        is PopUpOption.PopCurrentRoute -> {
                            popUpTo(currentBackStackEntry?.destination?.route ?: return@navigate) {
                                inclusive = true
                            }
                        }

                        is PopUpOption.PopToRoute -> {
                            popUpTo(popUpOption.route) {
                                inclusive = popUpOption.isInclusive
                            }
                        }
                    }
                }
                launchSingleTop = nav.launchSingleTop
            }
        }
    }
}