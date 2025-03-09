package com.mixedwash.features.support.data

import com.mixedwash.features.support.domain.FaqRepository
import com.mixedwash.features.support.domain.model.FaqItemCategoryDto
import com.mixedwash.features.support.domain.model.FaqItemDTO

class FaqRepositoryImpl(private val faqService: FaqService) : FaqRepository {
    override fun getAllFaqs(): Result<List<FaqItemDTO>> {
        return faqService.getAllFaqs()
    }

    override fun getFaqsByLabel(label: FaqItemCategoryDto): Result<List<FaqItemDTO>> {
        return faqService.getFaqsByLabel(label)
    }

    override fun searchFaqs(searchString: String): Result<List<FaqItemDTO>> {
        return faqService.searchFaqs(searchString)
    }

    override fun filterByTag(tag: String): Result<List<FaqItemDTO>> {
        return faqService.filterByTag(tag)
    }
}