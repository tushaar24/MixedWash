package com.mixedwash.libs.loki.permission

/**
 * Defines a controller for managing location permissions.
 */
public interface LocationPermissionController {

    /**
     * Checks if the app has the necessary permissions to access the device's location.
     *
     * @return `true` if the app has the necessary permissions, `false` otherwise.
     */
    public fun hasPermission(): Boolean

    /**
     * Requests the necessary permissions to access the device's location.
     *
     * @return The state of the permission after the request.
     */
    public suspend fun requirePermissionFor(priority: com.mixedwash.libs.loki.core.Priority): PermissionState

    /**
     * Requests the necessary permissions to access the device's location, and throws an exception
     * if the permission is denied.
     *
     * @throws PermissionDeniedException If the permission is denied.
     * @throws PermissionDeniedForeverException If the permission is missing.
     */
    public suspend fun requirePermissionForOrThrow(priority: com.mixedwash.libs.loki.core.Priority) {
        requirePermissionFor(priority).also { it.throwOnError() }
    }

    public companion object
}


/**
 * Creates a new [LocationPermissionController] for Android and iOS.
 *
 * @return A new [LocationPermissionController].
 */
public fun LocationPermissionController(): LocationPermissionController {
    return createPermissionController()
}

/**
 * Creates a new [LocationPermissionController] for Android and iOS.
 *
 * @return A new [LocationPermissionController].
 */
@Suppress("FunctionName")
public fun MobileLocationPermissionController(): LocationPermissionController {
    return LocationPermissionController()
}

/**
 * Creates a new [LocationPermissionController] for Android and iOS.
 *
 * @return A new [LocationPermissionController].
 */
public fun LocationPermissionController.Companion.mobile(): LocationPermissionController {
    return LocationPermissionController()
}

internal expect fun createPermissionController(): LocationPermissionController