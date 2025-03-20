package com.mixedwash.features.getting_started

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.Route
import com.mixedwash.features.getting_started.model.onboardingItems
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class OnboardingScreenViewModel : ViewModel() {

    private val initialState = OnboardingScreenState(
        items = onboardingItems,
        currentIndex = 0
    )

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<OnboardingScreenState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<OnboardingScreenUiEvent>(BUFFERED)
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    fun onEvent(event: OnboardingScreenEvent) {
        when (event) {
            OnboardingScreenEvent.OnExplore -> {
                sendNavigationEvent(Route.ServicesRoute(null))
            }

            OnboardingScreenEvent.OnNavigateToHelpCenter -> {
                sendNavigationEvent(Route.FaqRoute)
            }
        }
    }

    private fun sendNavigationEvent(route: Route) {
        viewModelScope.launch {
            _uiEventsChannel.send(OnboardingScreenUiEvent.Navigate(route))
        }
    }
}