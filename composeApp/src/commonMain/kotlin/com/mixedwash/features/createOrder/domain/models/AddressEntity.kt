package com.mixedwash.features.createOrder.domain.models

import com.mixedwash.features.createOrder.presentation.models.Address
import com.mixedwash.features.createOrder.presentation.models.AddressId

data class AddressEntity(
    val id: String,
    val title: String,
    val addressLine1: String,
    val addressLine2: String = "",
    val addressLine3: String = "",
    val pinCode: String,
) {
    fun toAddress() = Address(
        id = AddressId(id),
        title = title,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        pinCode = pinCode
    )
}