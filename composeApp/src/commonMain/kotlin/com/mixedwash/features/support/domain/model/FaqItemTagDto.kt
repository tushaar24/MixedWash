package com.mixedwash.features.support.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FaqItemTagDto(val displayTag: String) {
    @SerialName("login_issue")
    LOGIN_ISSUE("login issue"),
    @SerialName("customer_support")
    CUSTOMER_SUPPORT("customer support"),
    @SerialName("change_payment_method")
    CHANGE_PAYMENT_METHOD("change payment method")
}