package com.mixedwash.features.createOrder.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mixedwash.features.createOrder.presentation.address.model.Address

@Entity
data class AddressEntity(
    @PrimaryKey val uid: String,
    val title: String,
    val addressLine1: String,
    val addressLine2: String = "",
    val addressLine3: String = "",
    val fetchedPlacedId: String? = null,
    val lat: Double? = null,        // TODO : MAKE LATITUDE AND LONGITUDE MANDATORY
    val long: Double? = null,
    val pinCode: String,
) {
    fun toAddress() = Address(
        uid = uid,
        title = title,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        addressLine3 = addressLine3,
        pinCode = pinCode,
        lat = lat,
        long = long,
        fetchedPlacedId = fetchedPlacedId
    )
}