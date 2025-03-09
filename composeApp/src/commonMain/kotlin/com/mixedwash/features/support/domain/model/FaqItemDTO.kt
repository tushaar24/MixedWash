package com.mixedwash.features.support.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param question the question that a user may have
 * @param answer the answer to the question
 * @param categories labels identify the general category to which the question belongs(for eg. General, Pickup)
 * @param tags tags are hidden keywords associated with the question which identify what commonly searched issue this question is related to.(for eg., login issue, payment method)
 */
@Serializable
data class FaqItemDTO(
    @SerialName("question")
    val question: String,
    @SerialName("answer")
    val answer: String,
    @SerialName("categories")
    val categories: List<FaqItemCategoryDto>,
    @SerialName("tags")
    val tags: List<FaqItemTagDto> = emptyList(),
)