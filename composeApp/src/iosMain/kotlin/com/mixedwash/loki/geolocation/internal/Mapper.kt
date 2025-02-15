package com.mixedwash.loki.geolocation.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import com.mixedwash.loki.core.Altitude
import com.mixedwash.loki.core.Azimuth
import com.mixedwash.loki.core.Coordinates
import com.mixedwash.loki.core.Location
import com.mixedwash.loki.core.Priority
import com.mixedwash.loki.core.Speed
import platform.CoreLocation.CLLocation
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIDevice

internal val Priority.toIosPriority: Double
    get() = when (this) {
        Priority.HighAccuracy -> platform.CoreLocation.kCLLocationAccuracyBestForNavigation
        Priority.Balanced -> platform.CoreLocation.kCLLocationAccuracyNearestTenMeters
        Priority.LowPower -> platform.CoreLocation.kCLLocationAccuracyKilometer
        Priority.Passive -> platform.CoreLocation.kCLLocationAccuracyThreeKilometers
    }

@OptIn(ExperimentalForeignApi::class)
internal fun CLLocation.toModel(): Location {
    val coordinates = coordinate().useContents {
        Coordinates(
            latitude = latitude,
            longitude = longitude,
        )
    }

    val speed = Speed(
        mps = speed.toFloat(),
        accuracy = speedAccuracy.toFloat(),
    )

    val courseAccuracy =
        if (UIDevice.currentDevice.systemVersion < "13.4") null
        else courseAccuracy

    val azimuth = Azimuth(
        degrees = course.toFloat(),
        accuracy = courseAccuracy?.toFloat(),
    )

    val altitude = Altitude(
        meters = altitude,
        accuracy = verticalAccuracy.toFloat(),
    )

    return Location(
        coordinates = coordinates,
        accuracy = horizontalAccuracy,
        altitude = altitude,
        speed = speed,
        azimuth = azimuth,
        timestampMillis = timestamp.timeIntervalSince1970.toLong(),
    )
}