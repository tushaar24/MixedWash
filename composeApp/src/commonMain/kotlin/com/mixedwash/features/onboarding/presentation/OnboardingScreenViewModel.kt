package com.mixedwash.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.onboarding.domain.OnboardingRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingScreenViewModel(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val initialState = OnboardingScreenState(
        items = emptyList(),
        currentIndex = 0
    )


    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<OnboardingScreenState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<OnboardingScreenUiEvent>(BUFFERED)
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    items = onboardingRepository.fetchOnboardingData().getOrNull() ?: emptyList()
                )
            }
        }
    }

    fun onEvent(event: OnboardingScreenEvent) {
        when (event) {
            OnboardingScreenEvent.OnExplore -> {
                sendNavigationEvent(Route.ServicesRoute(serviceId = null))
            }

            OnboardingScreenEvent.OnNavigateToHelpCenter -> {
                sendNavigationEvent(Route.FaqRoute)
            }

            OnboardingScreenEvent.OnSkip -> {
                viewModelScope.launch {
                    _uiEventsChannel.send(OnboardingScreenUiEvent.GoBack)
                }

            }
        }
    }

    private fun sendNavigationEvent(route: Route) {
        viewModelScope.launch {
            _uiEventsChannel.send(OnboardingScreenUiEvent.Navigate(route))
        }
    }
}