package com.mixedwash.features.getting_started

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.Route
import com.mixedwash.features.getting_started.model.gettingStartedItems
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GettingStartedScreenViewModel : ViewModel() {

    private val initialState = GettingStartedScreenState(
        items = gettingStartedItems,
        currentIndex = 0
    )

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<GettingStartedScreenState> = _state.asStateFlow()

    private val _uiEventsChannel = Channel<GettingStartedScreenUiEvent>(BUFFERED)
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow()

    fun onEvent(event: GettingStartedScreenEvent) {
        when (event) {
            GettingStartedScreenEvent.OnExplore -> {
                sendNavigationEvent(Route.ServicesRoute(null))
            }

            GettingStartedScreenEvent.OnNavigateToHelpCenter -> {
                sendNavigationEvent(Route.FaqRoute)
            }

            GettingStartedScreenEvent.OnNext -> {
                _state.update {
                    it.copy(currentIndex = it.currentIndex.inc())
                }
            }

            GettingStartedScreenEvent.OnSkip -> {
                _state.update {
                    it.copy(currentIndex = it.items.size - 1)
                }
            }
        }
    }

    private fun sendNavigationEvent(route: Route) {
        viewModelScope.launch {
            _uiEventsChannel.send(GettingStartedScreenUiEvent.Navigate(route))
        }
    }
}