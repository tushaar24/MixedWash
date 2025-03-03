package com.mixedwash.features.support.domain

import com.mixedwash.features.support.domain.model.FaqItemCategory
import com.mixedwash.features.support.domain.model.FaqItemDTO

interface FaqRepository {
    fun getAllFaqs() : Result<List<FaqItemDTO>>

    fun getFaqsByLabel(label: FaqItemCategory) : Result<List<FaqItemDTO>>

    fun searchFaqs(searchString: String) : Result<List<FaqItemDTO>>

    fun filterByTag(tag: String) : Result<List<FaqItemDTO>>
}