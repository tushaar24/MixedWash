package com.mixedwash.features.local_cart.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItemEntity(
    @PrimaryKey
    val itemId: String,
    @ColumnInfo(index = true)
    val serviceId: String,
    val name: String,
    @Embedded val metadata: ServiceItemMetadataEntity? = null,
    @Embedded val itemPricing: ItemPricingEntity,
    val quantity: Int,
)