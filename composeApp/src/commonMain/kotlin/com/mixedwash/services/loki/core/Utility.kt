package com.mixedwash.services.loki.core

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val earthRadius = 6371.0  // Radius of the Earth in kilometers

    // Convert degrees to radians
    val dLat = toRadians(lat2 - lat1)
    val dLon = toRadians(lon2 - lon1)

    // Apply the haversine formula
    val a = sin(dLat / 2).pow(2.0) +
            cos(toRadians(lat1)) * cos(toRadians(lat2)) *
            sin(dLon / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}

fun toRadians(deg: Double): Double = deg / 180.0 * PI


fun Coordinates.calculateDistance(other: Coordinates): Double {
    return calculateDistance(
        lat1 = latitude,
        lon1 = longitude,
        lat2 = other.latitude,
        lon2 = other.longitude
    )
}