package com.mixedwash.features.support.data

import com.mixedwash.features.support.domain.FaqRepository
import com.mixedwash.features.support.domain.model.FaqItemCategoryDto
import com.mixedwash.features.support.domain.model.FaqItemDTO
import kotlinx.serialization.json.Json
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

class FaqRepositoryImpl : FaqRepository {
    private val filePath = "files/mock/faq_items_json.json"
    @OptIn(ExperimentalResourceApi::class)
    override suspend fun getAllFaqs(): Result<List<FaqItemDTO>> {
        val bytes = Res.readBytes(filePath)
        val jsonString = bytes.decodeToString()
        val json = Json { ignoreUnknownKeys = true }
        return Result.success(json.decodeFromString(jsonString))
    }

    override suspend fun getFaqsByCategory(label: FaqItemCategoryDto): Result<List<FaqItemDTO>> {
        return Result.success(getAllFaqs().getOrNull()!!.filter { it.categories.contains(label) })
    }

    override suspend fun searchFaqs(searchString: String): Result<List<FaqItemDTO>> {
        // search each question, answer, and the tags for the substring
        val result = getAllFaqs().getOrNull()!!.filter { faqItem ->
            faqItem.question.contains(searchString, ignoreCase = true)
                    || faqItem.answer.contains(searchString, ignoreCase = true)
                    || faqItem.tags.any { tag -> tag.name.contains(searchString, ignoreCase = true) }
        }

        return Result.success(result)
    }

    override suspend fun filterByTag(tag: String) : Result<List<FaqItemDTO>> {
        return Result.success(
            getAllFaqs().getOrNull()!!.filter {
                it.tags.any { t -> t.displayTag.contains(tag, ignoreCase = true) }
            }
        )
    }
}