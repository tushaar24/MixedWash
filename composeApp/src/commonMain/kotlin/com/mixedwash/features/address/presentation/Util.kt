package com.mixedwash.features.address.presentation

import com.mixedwash.core.presentation.models.FieldID
import com.mixedwash.features.address.domain.model.Address

/**
 * Converts an [Address] to a [EnumMap] of [FieldID] to [String].
 * @return [EnumMap] of [FieldID] to [String]
 */
fun Address.toFieldIDValueMap(): Map<FieldID, String?> =
    FieldID.entries.associateWith {
        when (it) {
            FieldID.ADDRESS_TITLE -> title
            FieldID.ADDRESS_LINE_1 -> addressLine1
            FieldID.ADDRESS_LINE_2 -> addressLine2 ?: ""
            FieldID.ADDRESS_LINE_3 -> addressLine3 ?: ""
            FieldID.PIN_CODE -> pinCode
            FieldID.PHONE, FieldID.EMAIL, FieldID.NAME -> null
        }
    }

