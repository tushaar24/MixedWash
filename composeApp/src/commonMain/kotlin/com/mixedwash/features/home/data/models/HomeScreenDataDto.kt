package com.mixedwash.features.home.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeScreenDataDto(
    @SerialName("home_banners") val homeBanners: List<HomeBannerDto>,
    @SerialName("preferred_banner_id") val preferredBannerId: String,
    @SerialName("offer_cards") val offerCards: List<HomeOfferCardDto>? = null,
    @SerialName("home_services") val homeServices: List<HomeServicesDto>,
    @SerialName("active_orders") val activeOrders: List<OrderStatusDto>? = null,
    @SerialName("intro_section") val introSection: IntroSectionDto?=null,
    @SerialName("schema_version") val schemaVersion: String,
)

@Serializable
data class HomeBannerDto(
    @SerialName("id") val id: String,
    @SerialName("heading") val heading: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("description") val description: String? = null,
    @SerialName("button") val button: String? = null,
    @SerialName("link") val link: String? = null,
    @SerialName("ui_text_color") val uiTextColor: String,
    @SerialName("content_text_color") val contentTextColor: String,
    @SerialName("gradient") val gradient: GradientDto
)

@Serializable
data class HomeOfferCardDto(
    @SerialName("text") val text: String,
    @SerialName("description") val description: String,
    @SerialName("offer_id") val offerId: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("link") val link: String,
    @SerialName("gradient") val gradient: GradientDto,
    @SerialName("content_text_color") val contentTextColor: String,
)

@Serializable
data class HomeServicesDto(
    @SerialName("service_id") val serviceID: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("image_url") val imageUrl: String
)

@Serializable
data class OrderStatusDto(
    @SerialName("order_id") val orderId: String,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String,
    @SerialName("description") val description: String
)

@Serializable
data class IntroSectionDto(
    @SerialName("heading") val heading: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("description") val description: String,
    @SerialName("button_text") val buttonText: String,
    @SerialName("content_text_color") val contentTextColor: String,
    @SerialName("button_text_color") val buttonTextColor: String,
    @SerialName("gradient") val gradient: GradientDto
)

@Serializable
data class GradientDto(
    @SerialName("gradient_colors") val gradientColors: List<GradientColorDto>,
    @SerialName("angle") val angle: Float
)

@Serializable
data class GradientColorDto(
    @SerialName("color_hex") val colorHex: String,
    @SerialName("color_stop") val colorStop: Float
)


