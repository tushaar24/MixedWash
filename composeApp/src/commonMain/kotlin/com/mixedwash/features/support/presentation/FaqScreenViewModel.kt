package com.mixedwash.features.support.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixedwash.features.support.data.FaqService
import com.mixedwash.features.support.domain.model.FaqItemCategoryDto
import com.mixedwash.features.support.domain.model.FaqItemDTO
import com.mixedwash.features.support.domain.model.FaqItemTagDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// TODO : Populate 'faqItems' properly
class FaqScreenViewModel(
    private val faqService: FaqService
) : ViewModel() {

    private val initialState = FaqScreenState(
        faqItems = emptyList(),
        currentCategory = FaqItemCategoryDto.All,
        faqCategories = FaqItemCategoryDto.entries,
        searchString = "",
        faqTags = FaqItemTagDto.entries
    )

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<FaqScreenState> = _state.asStateFlow()

    init {
        getAllItems()
    }

    fun onEvent(event: FaqScreenEvent) {
        when (event) {
            is FaqScreenEvent.OnSearchStringValueChanged -> {
                updateSearchString(event.newString)
                // when we update the search string, we also dynamically update the faq items
                searchFaqs(event.newString)
            }

            is FaqScreenEvent.OnFaqCategoryChipClicked -> {
                updateSearchString("")
                updateCategory(event.newLabel)
            }

            is FaqScreenEvent.OnFaqTagClicked -> {
                updateSearchString(event.tag)
                filterByTag(event.tag)
            }

            FaqScreenEvent.OnCallButtonClicked -> raiseCallIntent()
        }
    }

    private fun updateSearchString(newString: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(searchString = newString)
            }
        }
    }

    private fun filterByTag(tag: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(faqItems = faqService.filterByTag(tag).getOrNull() ?: emptyList())
            }
        }
    }

    private fun searchFaqs(searchString: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(faqItems = faqService.searchFaqs(searchString).getOrNull() ?: emptyList())
            }
        }
    }

    private fun updateCategory(newCategory: FaqItemCategoryDto) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentCategory = newCategory,
                    faqItems = faqService.getFaqsByLabel(newCategory).getOrNull() ?: emptyList()
                )
            }
        }
    }

    private fun getAllItems() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    faqItems = faqService.getAllFaqs().getOrNull() ?: emptyList()
                )
            }
        }
    }

    private fun raiseCallIntent() {
        // TODO: implement
    }
}