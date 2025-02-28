package com.mixedwash.libs.loki.permission.internal

import com.mixedwash.libs.loki.permission.PermissionState
import platform.CoreLocation.CLAuthorizationStatus

internal val CLAuthorizationStatus.toPermissionState: PermissionState
    get() = when (this) {
        platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways,
        platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse,
        -> PermissionState.Granted
        platform.CoreLocation.kCLAuthorizationStatusNotDetermined -> PermissionState.NotDetermined
        platform.CoreLocation.kCLAuthorizationStatusDenied -> PermissionState.DeniedForever
        else -> error("Unknown location authorization status $this")
    }
