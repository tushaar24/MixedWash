package com.mixedwash.features.history.domain.model

import com.mixedwash.features.common.presentation.services.model.ServiceItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

@Serializable
data class OrderHistoryDTO(
    @SerialName("order_id") val orderId: Long,
    @SerialName("service_items") val serviceItems: List<ServiceItem>,
    @SerialName("ordered_timestamp") val orderedTimestamp: Long,
    @SerialName("delivery_timestamp") val deliveryTimestamp: Long?, // null if the order was cancelled
    @SerialName("order_delivery_status") val orderDeliveryStatusDto: OrderDeliveryStatusDto,
    @SerialName("price") val price: Int,
)

data class InsightMetricDTO(
    val metric: String,
    val unit: String,
    val icon: DrawableResource
)
