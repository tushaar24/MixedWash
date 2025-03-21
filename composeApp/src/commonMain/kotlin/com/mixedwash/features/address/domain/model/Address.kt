package com.mixedwash.features.address.domain.model

import androidx.compose.runtime.Immutable
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.core.presentation.models.FieldID
import com.mixedwash.libs.loki.core.Place
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class Address @OptIn(ExperimentalUuidApi::class) constructor(
    val uid: String = Uuid.random().toHexString(),
    val title: String,
    val addressLine1: String,
    val addressLine2: String = "",
    val addressLine3: String = "",
    val pinCode: String,
    val lat: Double? = null,
    val long: Double? = null,
    val fetchedPlacedId: String? = null
) {
    override fun toString(): String {
        return "$addressLine1, $addressLine2\n$addressLine3\n$pinCode"
    }
}

fun Place.toAddress(): Address {
    Logger.d("TAG", this.toString())
    val addressString = if (!formattedAddress.isNullOrBlank()) {
        formattedAddress
    } else {
        val route = this.route?.let { "$it, " } ?: ""
        val locality = this.locality?.let { "$it, " } ?: ""
        val subLocality = this.subLocality?.let { "$it, " } ?: ""
        val subAdministrativeArea = this.subAdministrativeArea?.let { "$it, " } ?: ""
        val administrativeArea = this.administrativeArea?.let { "$it, " } ?: ""
        val country = this.country?.let { "$it, " } ?: ""
        "$route$locality$subLocality$subAdministrativeArea$administrativeArea$country"
    }

    // split formatted string into list of string by ', '
    val chunks = addressString.split(", ")
    var addressLine1: String? = null
    var addressLine2: String? = null
    var addressLine3: String? = null
    if (chunks.size < 3) {
        if(chunks.size > 0) {
            addressLine1 = chunks[0]
        }
        if(chunks.size > 1) {
            addressLine3 = chunks[1]
        }
    } else {
        // Calculate indices for splitting into three parts.
        val groupSize = chunks.size / 3
        // For any extra elements (when not divisible by 3), add them to the third address line.
        val splitIndex1 = groupSize
        val splitIndex2 = groupSize * 2

        addressLine1 = chunks.subList(0, splitIndex1).joinToString(", ")
        addressLine2 = chunks.subList(splitIndex1, splitIndex2).joinToString(", ")
        addressLine3 = chunks.subList(splitIndex2, chunks.size).joinToString(", ")

        // Use addressLine1, addressLine2, addressLine3 as needed.
    }


    return Address(
        title = "",
        addressLine1 = addressLine1 ?: "",
        addressLine2 = addressLine2 ?: "",
        addressLine3 = addressLine3 ?: "",
        pinCode = this.postalCode ?: "",
        lat = this.coordinates.latitude,
        long = this.coordinates.longitude,
        fetchedPlacedId = this.googlePlaceId
    )
}



