package com.mixedwash.features.local_cart.data.model

import androidx.room.TypeConverter

enum class PricingTypeEntity {
    FIXED, RANGED, SERVICE
}

class PricingTypeEntityTypeConverter {
    @TypeConverter
    fun fromPricingTypeEntity(value : PricingTypeEntity) : String{
        return value.name
    }

    @TypeConverter
    fun toPricingTypeEntity(value : String) : PricingTypeEntity {
        return PricingTypeEntity.valueOf(value)
    }
}
