package com.mixedwash.features.support.data

import com.mixedwash.features.support.domain.FaqRepository
import com.mixedwash.features.support.domain.model.FaqItemCategoryDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class FaqService(private val faqRepository: FaqRepository) {
    suspend fun getAllFaqs() = withContext(Dispatchers.IO) {
        faqRepository.getAllFaqs()
    }

    suspend fun getFaqsByLabel(category: FaqItemCategoryDto) = withContext(Dispatchers.IO) {
        faqRepository.getFaqsByCategory(category)
    }

    suspend fun searchFaqs(searchString: String) =
        withContext(Dispatchers.IO) {
            faqRepository.searchFaqs(searchString)
        }

    suspend fun filterByTag(tag: String) =
        withContext(Dispatchers.IO) {
            faqRepository.filterByTag(tag)
        }
}