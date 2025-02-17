package com.mixedwash.services.loki.geolocation

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import com.mixedwash.services.loki.core.InternalLokiApi
import com.mixedwash.services.loki.core.Location
import com.mixedwash.services.loki.core.Priority
import com.mixedwash.services.loki.geolocation.internal.LocationManager
import com.mixedwash.services.loki.geolocation.internal.toAndroidLocationRequest
import com.mixedwash.services.loki.geolocation.internal.toAndroidPriority
import com.mixedwash.services.loki.geolocation.internal.toModel
import com.mixedwash.services.loki.permission.LocationPermissionController
import com.mixedwash.services.loki.permission.PermissionState
import com.mixedwash.services.loki.permission.internal.context.ContextProvider
import com.mixedwash.services.loki.permission.throwOnError

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

    override suspend fun current(priority: com.mixedwash.services.loki.core.Priority): Location {
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

    private suspend fun requirePermission(priority: com.mixedwash.services.loki.core.Priority) {
        val state = permissionController.requirePermissionFor(priority)
        if (state != PermissionState.Granted) {
            state.throwOnError()
        }
    }
}