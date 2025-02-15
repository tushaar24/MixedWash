package com.mixedwash.features.createOrder.presentation.models

import androidx.compose.runtime.Immutable
import com.mixedwash.features.createOrder.domain.models.AddressEntity
import com.mixedwash.presentation.models.FieldID
import kotlin.jvm.JvmInline

@Immutable
data class Address(
    val id: AddressId,
    val title: String,
    val addressLine1: String,
    val addressLine2: String = "",
    val addressLine3: String = "",
    val pinCode: String,
)

fun Address.toAddressEntity() = AddressEntity(
    id = id.value ?: "",
    title = title,
    addressLine1 = addressLine1,
    addressLine2 = addressLine2,
    addressLine3 = addressLine3,
    pinCode = pinCode
)

@JvmInline
value class AddressId(val value: String? = null)

/**
 * Converts an [Address] to a [EnumMap] of [FieldID] to [String].
 * @return [EnumMap] of [FieldID] to [String]
 */
fun Address.toFieldIDValueMap(): Map<FieldID, String?> =
    FieldID.entries.associateWith<FieldID, String?> {
        when (it) {
            FieldID.ADDRESS_TITLE -> title
            FieldID.ADDRESS_LINE_1 -> addressLine1
            FieldID.ADDRESS_LINE_2 -> addressLine2 ?: ""
            FieldID.ADDRESS_LINE_3 -> addressLine3 ?: ""
            FieldID.PIN_CODE -> pinCode
            FieldID.PHONE, FieldID.EMAIL -> null
        }
    }

