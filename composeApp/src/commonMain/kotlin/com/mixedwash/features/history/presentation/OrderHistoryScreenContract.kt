package com.mixedwash.features.history.presentation

import com.mixedwash.core.orders.domain.model.Order
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.history.domain.model.InsightMetric

data class OrderHistoryScreenState(
    val orders: List<OrderHistoryPresentation>,
    val insights: List<InsightMetric>?,
    val stagingEnabled: Boolean
)

data class OrderHistoryPresentation(
    val order: Order,
    val serviceImageUrls: Map<String, String>,
    val delivered: Boolean,
)

sealed interface OrderHistoryScreenEvent {
    data class OnOrderDetailsScreen(val orderId: String) : OrderHistoryScreenEvent
    data class OnDeleteOrder(val orderId: String) : OrderHistoryScreenEvent
    data object OnClearAllOrders : OrderHistoryScreenEvent
}

sealed interface OrderHistoryScreenUiEvent {
    data class Navigate(val route: Route) : OrderHistoryScreenUiEvent
}

fun formatIndianCurrency(amount: Int): String {
    if (amount < 0) {
        return "-${formatIndianCurrency(-amount)}"
    }
    if (amount < 1000) {
        return amount.toString()
    }

    val amountStr = amount.toString()
    val length = amountStr.length

    return if (length <= 3) {
        amountStr
    } else if (length <= 5) {
        amountStr.substring(0, length - 3) + ", " + amountStr.substring(length - 3)
    } else if (length <= 7) {
        amountStr.substring(0, length - 5) + ", " + amountStr.substring(
            length - 5,
            length - 3
        ) + "," + amountStr.substring(length - 3)
    } else {
        val firstGroup = amountStr.substring(0, length - 7)
        val secondGroup = amountStr.substring(length - 7, length - 5)
        val thirdGroup = amountStr.substring(length - 5, length - 3)
        val lastGroup = amountStr.substring(length - 3)

        val builder = StringBuilder()
        builder.append(firstGroup)
        if (firstGroup.isNotEmpty()) {
            builder.append(",")
        }
        builder.append(secondGroup)
        builder.append(", ")
        builder.append(thirdGroup)
        builder.append(", ")
        builder.append(lastGroup)
        builder.toString()
    }
}