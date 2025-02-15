package com.mixedwash.features.createOrder.domain

import com.mixedwash.features.createOrder.domain.models.AddressEntity
import com.mixedwash.features.createOrder.domain.models.AddressScreenEntity

class GetAddressScreenUseCase () {
    operator fun invoke() : AddressScreenEntity {
        return AddressScreenEntity(
            title = "Pickup and Delivery Address",
            addresses = listOf(
                AddressEntity(
                    id = "alkka",
                    title = "Home",
                    addressLine1 = "2342, Electronic City Phase 2",
                    addressLine3 = "Silicon Town, Bengaluru",
                    pinCode = "560100"
                ),
                AddressEntity(
                    id = "lsasd",
                    title = "Office",
                    addressLine1 = "2342, Electronic City Phase 2",
                    addressLine3 = "Silicon Town, Bengaluru",
                    pinCode = "560100"
                ),
                AddressEntity(
                    id = "lkmk",
                    title = "Dadi",
                    addressLine1 = "2342, Electronic City Phase 2",
                    addressLine3 = "Silicon Town, Bengaluru",
                    pinCode = "560100"
                )
            ),
            selectedIndex = 0,
            buttonText = "Continue",
        )

    }
}