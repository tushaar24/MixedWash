package com.mixedwash.loki.permission

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.mixedwash.loki.core.Priority
import com.mixedwash.loki.permission.internal.LocationPermissionManagerDelegate
import com.mixedwash.loki.permission.internal.toPermissionState
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal actual fun createPermissionController(): LocationPermissionController {
    return IosLocationPermissionController(LocationPermissionManagerDelegate())
}

internal class IosLocationPermissionController(
    private val locationDelegate: LocationPermissionManagerDelegate,
) : LocationPermissionController {

    private val mutex: Mutex = Mutex()

    private val _permissionsStatus = MutableStateFlow(
        value = locationDelegate.currentPermissionStatus().toPermissionState,
    )

    init {
        locationDelegate.monitorPermission { permissionStatus ->
            _permissionsStatus.update { permissionStatus.toPermissionState }
        }
    }

    override fun hasPermission(): Boolean {
        return _permissionsStatus.value == PermissionState.Granted
    }

    override suspend fun requirePermissionFor(priority: Priority): PermissionState {
        val currentState = locationDelegate.currentPermissionStatus().toPermissionState
        return when {
            currentState == PermissionState.Granted ||
                    currentState == PermissionState.DeniedForever -> currentState
            else -> mutex.withLock {
                val result = suspendCoroutine { continuation ->
                    locationDelegate.requestPermission { continuation.resume(it) }
                }

                result.toPermissionState
            }
        }
    }
}