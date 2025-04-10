package com.mixedwash.features.local_cart.data.model

import androidx.room.TypeConverter

enum class GenderEntity {
    MALE,
    FEMALE
}

class GenderEntityTypeConverter {
    @TypeConverter
    fun fromGenderEntity(value : GenderEntity) : String{
        return value.name
    }

    @TypeConverter
    fun toGenderEntity(value : String) : GenderEntity {
        return GenderEntity.valueOf(value)
    }
}