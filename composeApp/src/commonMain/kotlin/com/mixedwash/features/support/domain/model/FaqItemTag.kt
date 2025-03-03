package com.mixedwash.features.support.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class FaqItemTag(val displayTag: String) {
    login_issue("login issue"),
    customer_support("customer support"),
    change_payment_method("change payment method")
}