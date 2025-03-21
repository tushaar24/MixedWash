package com.mixedwash.features.support.presentation

import com.mixedwash.features.support.domain.model.FaqItemDTO
import com.mixedwash.features.support.domain.model.FaqItemCategoryDto
import com.mixedwash.features.support.domain.model.FaqItemTagDto

data class FaqScreenState(
    val faqItems: List<FaqItemDTO>,
    val faqCategories: List<FaqItemCategoryDto>,
    val faqTags: List<FaqItemTagDto>,
    val currentCategory: FaqItemCategoryDto,
    val searchString: String,
)

sealed interface FaqScreenEvent {
    data class OnFaqCategoryChipClicked(val newLabel: FaqItemCategoryDto) : FaqScreenEvent
    data class OnSearchStringValueChanged(val newString: String) : FaqScreenEvent
    data object OnCallButtonClicked : FaqScreenEvent
    data class OnFaqTagClicked(val tag: String) : FaqScreenEvent
}