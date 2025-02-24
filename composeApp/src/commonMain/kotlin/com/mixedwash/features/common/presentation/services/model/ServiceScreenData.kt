package com.mixedwash.features.common.presentation.services.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Some services(such as dry clean), have their own subcategories(such as kurta, shirts, etc.) with their own pricing.
 */
@Serializable
data class CartItem(
    val name: String,
    val minPrice: Float,
    val maxPrice: Float,
    val imageUrl: String,
    val gender: Gender
)

/**
 * Some services are offered in diff. variants such as mixed and segregated. These variants have their own price and minimum cart.
 */
@Serializable
data class ServiceVariant(
    val variantName: ServiceVariantName,
    val description: String,
    val minCart: Int,
)

/**
 *
 * @param id unique id of the service
 * @param title The name of the service(eg: Wash and Fold, Dry Clean, etc)
 * @param description the description of the service
 * @param imageUrl the complete url of the service image
 * @param variants some services are offered in diff. variants such as mixed and segregated. When there are no variants, this field is left null
 * @param cartItems Some services(such as dry clean), have their own subcategories(such as kurta, shirts, etc.) with their own pricing. These are represented as a list of such cart items.
 * @param pricing the pricing for the service(this field is not required for services that have subcategories, such as dry clean).
 * @param unit The unit considered for this service(kgs, pairs, etc)
 * @param inclusions The type of clothing which can be included in this service.
 * @param exclusions The type of clothing which must be excluded in this service.
 * @param deliveryTimeMinInHrs minimum delivery time for this service
 * @param deliveryTimeMaxInHrs maximum delivery time for this service
 */
@Serializable
data class ServiceItem(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("image_url")
    val imageUrl: String,   // should get the whole url
    val variants: List<ServiceVariant>? = null,
    val cartItems: List<CartItem>? = null,
    val pricing: Float? = null,
    val unit: String,
    val inclusions: String,
    val exclusions: String,
    @SerialName("delivery_time_min_hrs")
    val deliveryTimeMinInHrs: Int,
    @SerialName("delivery_time_max_hrs")
    val deliveryTimeMaxInHrs: Int
)

data class Offer(
    val type: Int,  // percent, cash
    val amount: Int,
    val qualifyingCartPrice: Int,
    val maxOfferAmount: Int,
    val discountCode: String,
)