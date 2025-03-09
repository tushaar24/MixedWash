package com.mixedwash.features.support.presentation

import androidx.lifecycle.ViewModel
import com.mixedwash.features.support.domain.FaqRepository
import com.mixedwash.features.support.domain.model.FaqItemCategory
import com.mixedwash.features.support.domain.model.FaqItemTag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// TODO : Populate 'faqItems' properly
class FaqScreenViewModel(
    private val faqRepository: FaqRepository
) : ViewModel() {

    private val initialState = FaqScreenState(
        faqItems = faqRepository.getAllFaqs().getOrNull() ?: emptyList(),
        currentCategory = FaqItemCategory.All,
        faqCategories = FaqItemCategory.entries,
        searchString = "",
        faqTags = FaqItemTag.entries
    )

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<FaqScreenState> = _state.asStateFlow()

    fun onEvent(event: FaqScreenEvent) {
        when (event) {
            is FaqScreenEvent.OnSearchStringValueChanged -> {
                updateSearchString(event.newString)
                // when we update the search string, we also dynamically update the faq items
                searchFaqs(event.newString)
            }
            is FaqScreenEvent.OnFaqCategoryChipClicked -> {
                updateSearchString("")
                updateLabel(event.newLabel)
            }
            is FaqScreenEvent.OnFaqTagClicked -> {
                updateSearchString(event.tag)
                filterByTag(event.tag)
            }
            FaqScreenEvent.OnCallButtonClicked -> raiseCallIntent()
        }
    }

    private fun updateSearchString(newString: String) {
        _state.update {
            it.copy(searchString = newString)
        }
    }

    private fun filterByTag(tag: String) {
        _state.update {
            it.copy(faqItems = faqRepository.filterByTag(tag).getOrNull() ?: emptyList())
        }
    }

    private fun searchFaqs(searchString: String) {
        _state.update {
            it.copy(faqItems = faqRepository.searchFaqs(searchString).getOrNull() ?: emptyList())
        }
    }

    private fun updateLabel(newLabel: FaqItemCategory) {
        _state.update {
            it.copy(currentCategory = newLabel, faqItems = faqRepository.getFaqsByLabel(newLabel).getOrNull() ?: emptyList())
        }
    }

    private fun raiseCallIntent() {
        // TODO: implement
    }
}