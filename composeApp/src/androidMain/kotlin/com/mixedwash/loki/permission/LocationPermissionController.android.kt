package com.mixedwash.loki.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import com.mixedwash.loki.core.InternalLokiApi
import com.mixedwash.loki.core.Priority
import com.mixedwash.loki.permission.internal.activity.ActivityProvider
import com.mixedwash.loki.permission.internal.context.ContextProvider
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(InternalLokiApi::class)
internal actual fun createPermissionController(): LocationPermissionController {
    return AndroidLocationPermissionController(
        context = ContextProvider.getInstance().context,
        activityProvider = ActivityProvider.getInstance(),
    )
}

@InternalLokiApi
internal class AndroidLocationPermissionController(
    private val context: Context,
    private val activityProvider: ActivityProvider,
) : LocationPermissionController {

    private val mutex: Mutex = Mutex()

    override fun hasPermission(): Boolean {
        return context.hasAnyPermission()
    }

    override suspend fun requirePermissionFor(priority: Priority): PermissionState {
        val permissions = permissionsFor(priority).filter { !context.hasPermission(it) }
        if (permissions.isEmpty() || permissions.hasPermissions()) return PermissionState.Granted

        return mutex.withLock {
            suspendCoroutine { continuation ->
                activityProvider.permissionRequester.request(permissions) { result ->
                    continuation.resume(result)
                }
            }
        }
    }

    private fun permissionsFor(priority: Priority): List<String> {
        return when (priority) {
            Priority.HighAccuracy -> listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            Priority.Balanced,
            Priority.LowPower,
            Priority.Passive,
                -> listOf(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    private fun List<String>.hasPermissions(): Boolean {
        return all { context.hasPermission(it) }
    }
}

private fun Context.hasAnyPermission(): Boolean {
    return listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ).any { hasPermission(it) }
}

private fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}
