package com.mixedwash.features.createOrder.domain.models


data class AddressScreenEntity(
    val title: String,
    val addresses: List<AddressEntity>,
    val selectedIndex: Int,
    val buttonText: String,
)
