package com.mixedwash.services.loki.geolocation.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import com.mixedwash.services.loki.core.Altitude
import com.mixedwash.services.loki.core.Azimuth
import com.mixedwash.services.loki.core.Coordinates
import com.mixedwash.services.loki.core.Location
import com.mixedwash.services.loki.core.Priority
import com.mixedwash.services.loki.core.Speed
import platform.CoreLocation.CLLocation
import platform.Foundation.timeIntervalSince1970
import platform.UIKit.UIDevice

internal val com.mixedwash.services.loki.core.Priority.toIosPriority: Double
    get() = when (this) {
        com.mixedwash.services.loki.core.Priority.HighAccuracy -> platform.CoreLocation.kCLLocationAccuracyBestForNavigation
        com.mixedwash.services.loki.core.Priority.Balanced -> platform.CoreLocation.kCLLocationAccuracyNearestTenMeters
        com.mixedwash.services.loki.core.Priority.LowPower -> platform.CoreLocation.kCLLocationAccuracyKilometer
        com.mixedwash.services.loki.core.Priority.Passive -> platform.CoreLocation.kCLLocationAccuracyThreeKilometers
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