package com.mixedwash.libs.loki.geolocation

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import com.mixedwash.libs.loki.core.InternalLokiApi
import com.mixedwash.libs.loki.core.Location
import com.mixedwash.libs.loki.geolocation.internal.LocationManager
import com.mixedwash.libs.loki.geolocation.internal.toAndroidLocationRequest
import com.mixedwash.libs.loki.geolocation.internal.toAndroidPriority
import com.mixedwash.libs.loki.geolocation.internal.toModel
import com.mixedwash.libs.loki.permission.LocationPermissionController
import com.mixedwash.libs.loki.permission.PermissionState
import com.mixedwash.libs.loki.permission.internal.context.ContextProvider
import com.mixedwash.libs.loki.permission.throwOnError

/**
 * Creates a new [MobileLocator] instance for Android.
 */
@OptIn(InternalLokiApi::class)
internal actual fun createLocator(
    permissionController: LocationPermissionController,
): MobileLocator {
    return AndroidLocator(ContextProvider.getInstance().context, permissionController)
}

internal class AndroidLocator(
    private val context: Context,
    private val permissionController: LocationPermissionController,
    private val locationManager: LocationManager = LocationManager(context),
) : MobileLocator {

    override val locationUpdates: Flow<Location> = locationManager.locationUpdates
        .mapNotNull { result -> result.lastLocation?.toModel() }

    override suspend fun isAvailable(): Boolean {
        return locationManager.locationEnabled()
    }

    override fun hasPermission(): Boolean {
        return permissionController.hasPermission()
    }

    override suspend fun current(priority: com.mixedwash.libs.loki.core.Priority): Location {
        requirePermission(priority)
        return locationManager.currentLocation(priority.toAndroidPriority).toModel()
    }

    override suspend fun track(request: LocationRequest): Flow<Location> {
        requirePermission(request.priority)
        locationManager.startTracking(request.toAndroidLocationRequest())

        return locationUpdates
    }

    override fun stopTracking() {
        locationManager.stopTracking()
    }

    private suspend fun requirePermission(priority: com.mixedwash.libs.loki.core.Priority) {
        val state = permissionController.requirePermissionFor(priority)
        if (state != PermissionState.Granted) {
            state.throwOnError()
        }
    }
}