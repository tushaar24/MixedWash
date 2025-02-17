package com.mixedwash.services.loki.geolocation.internal

import android.location.Location
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import com.google.android.gms.location.LocationRequest
import com.mixedwash.services.loki.core.Altitude
import com.mixedwash.services.loki.core.Azimuth
import com.mixedwash.services.loki.core.Coordinates
import com.mixedwash.services.loki.core.Priority
import com.mixedwash.services.loki.core.Speed
import com.mixedwash.services.loki.core.Location as LokiLocation
import com.mixedwash.services.loki.geolocation.LocationRequest as LokiLocationRequest


/**
 * Converts a [Location] to a [dev.jordond.compass.Location].
 */
internal fun Location.toModel(): LokiLocation =
    com.mixedwash.services.loki.core.Location(
        coordinates = Coordinates(latitude = latitude, longitude = longitude),
        accuracy = accuracy.toDouble(),
        azimuth = Azimuth(
            degrees = bearing,
            accuracy = if (VERSION.SDK_INT < VERSION_CODES.O) null else bearingAccuracyDegrees,
        ),
        speed = Speed(
            mps = speed,
            accuracy = if (VERSION.SDK_INT < VERSION_CODES.O) null else speedAccuracyMetersPerSecond,
        ),
        altitude = Altitude(
            meters = altitude,
            accuracy = if (VERSION.SDK_INT < VERSION_CODES.O) null else verticalAccuracyMeters,
        ),
        timestampMillis = time,
    )

internal val com.mixedwash.services.loki.core.Priority.toAndroidPriority: Int
    get() = when (this) {
        com.mixedwash.services.loki.core.Priority.Balanced -> com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
        com.mixedwash.services.loki.core.Priority.HighAccuracy -> com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
        com.mixedwash.services.loki.core.Priority.LowPower -> com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
        com.mixedwash.services.loki.core.Priority.Passive -> com.google.android.gms.location.Priority.PRIORITY_PASSIVE
    }

internal fun LokiLocationRequest.toAndroidLocationRequest(): LocationRequest {
    return LocationRequest
        .Builder(priority.toAndroidPriority, interval)
        .setGranularity(com.google.android.gms.location.Granularity.GRANULARITY_PERMISSION_LEVEL)
        .build()
}
