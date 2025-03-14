package com.mixedwash.features.support.domain

import com.mixedwash.features.support.domain.model.FaqItemCategoryDto
import com.mixedwash.features.support.domain.model.FaqItemDTO

interface FaqRepository {
    suspend fun getAllFaqs() : Result<List<FaqItemDTO>>

    suspend fun getFaqsByCategory(label: FaqItemCategoryDto) : Result<List<FaqItemDTO>>

    suspend fun searchFaqs(searchString: String) : Result<List<FaqItemDTO>>

    suspend fun filterByTag(tag: String) : Result<List<FaqItemDTO>>
}