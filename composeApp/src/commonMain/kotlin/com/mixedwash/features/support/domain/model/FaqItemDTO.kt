package com.mixedwash.features.support.domain.model

import kotlinx.serialization.Serializable

/**
 * @param question the question that a user may have
 * @param answer the answer to the question
 * @param categories labels identify the general category to which the question belongs
 * @param tags tags are hidden keywords associated with the question which identify what commonly searched issue this question is related to.
 */
@Serializable
data class FaqItemDTO(
    val question: String,
    val answer: String,
    val categories: List<FaqItemCategory>,
    val tags: List<FaqItemTag> = emptyList(),
)