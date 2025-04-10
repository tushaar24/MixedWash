package com.mixedwash.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.data.UserService
import com.mixedwash.core.presentation.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_chat
import mixedwash.composeapp.generated.resources.ic_clothes_hanger
import mixedwash.composeapp.generated.resources.ic_info
import mixedwash.composeapp.generated.resources.ic_location_outlined
import mixedwash.composeapp.generated.resources.ic_reward
import mixedwash.composeapp.generated.resources.ic_share
import mixedwash.composeapp.generated.resources.ic_thumbs
import mixedwash.composeapp.generated.resources.ic_upi

class ProfileScreenViewModel(
    private val userService: UserService
) : ViewModel() {

    val metadata =
        userService.currentUser?.userMetadata

    private val initialState = ProfileScreenState(
        imageUrl = metadata?.photoUrl,
        name = metadata?.name,
        email = metadata?.email,
        phone = metadata?.phoneNumber,
        appName = "MixedWash",
        appVersion = "V.0.1-beta",
        sections = listOf(
            ProfileSection(
                title = "Manage",
                items = listOf(
                    ProfileSectionItem(
                        resource = Res.drawable.ic_clothes_hanger,
                        text = "Order History",
                        onClick = {
                            viewModelScope.launch {
                                _uiEventsChannel.send(ProfileScreenUiEvent.Navigate(Route.HistoryRoute))
                            }
                        }),
                    ProfileSectionItem(
                        resource = Res.drawable.ic_location_outlined,
                        text = "Address Book",
                        onClick = {
                            viewModelScope.launch {
                                _uiEventsChannel.send(
                                    ProfileScreenUiEvent.Navigate(
                                        Route.AddressRoute(
                                            title = "Address Book",
                                            screenType = Route.AddressRoute.ScreenType.Edit,
                                            submitText = "hello"
                                        )
                                    )
                                )
                            }
                        }
                    )
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
                    )
                )
            ),
            ProfileSection(
                title = "Support",
                items = listOf(
                    ProfileSectionItem(
                        resource = Res.drawable.ic_chat,
                        text = "Help Center ",
                        onClick = {
                            viewModelScope.launch {
                                _uiEventsChannel.send(ProfileScreenUiEvent.Navigate(Route.FaqRoute))
                            }
                        },
                        comingSoon = false
                    ),
                    ProfileSectionItem(
                        resource = Res.drawable.ic_thumbs,
                        text = "Share Feedback ",
                        onClick = { },
                        comingSoon = true
                    )
                )
            ),
            ProfileSection(
                title = "Other",
                items = listOf(
                    ProfileSectionItem(
                        resource = Res.drawable.ic_share,
                        text = "Share App",
                        onClick = { },
                        comingSoon = true
                    ),
                    ProfileSectionItem(
                        resource = Res.drawable.ic_info,
                        text = "About Us",
                        onClick = { },
                        comingSoon = true
                    )
                )
            )
        )
    )
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<ProfileScreenState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<ProfileScreenUiEvent>()
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    fun onEvent(event: ProfileScreenEvent) {
        when (event) {
            ProfileScreenEvent.OnEditProfile -> {
                viewModelScope.launch {
                    _uiEventsChannel.send(ProfileScreenUiEvent.Navigate(Route.ProfileEditRoute))
                }
            }

            ProfileScreenEvent.OnLogout -> {
                viewModelScope.launch {
                    userService.signOut()
                }
            }
        }
    }
}