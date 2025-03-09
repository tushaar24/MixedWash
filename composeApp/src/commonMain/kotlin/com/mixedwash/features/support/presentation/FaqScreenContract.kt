package com.mixedwash.features.support.presentation

import com.mixedwash.features.support.domain.model.FaqItemDTO
import com.mixedwash.features.support.domain.model.FaqItemCategory
import com.mixedwash.features.support.domain.model.FaqItemTag

data class FaqScreenState(
    val faqItems: List<FaqItemDTO>,
    val faqCategories: List<FaqItemCategory>,
    val faqTags: List<FaqItemTag>,
    val currentCategory: FaqItemCategory,
    val searchString: String,
)

sealed interface FaqScreenEvent {
    data class OnFaqCategoryChipClicked(val newLabel: FaqItemCategory) : FaqScreenEvent
    data class OnSearchStringValueChanged(val newString: String) : FaqScreenEvent
    data object OnCallButtonClicked : FaqScreenEvent
    data class OnFaqTagClicked(val tag: String) : FaqScreenEvent
}