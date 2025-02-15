package com.mixedwash.loki.geolocation.internal

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.location.Location
import android.location.LocationManager
import android.os.Build
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.Priority.PRIORITY_LOW_POWER
import com.google.android.gms.location.Priority.PRIORITY_PASSIVE
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import com.mixedwash.loki.core.exception.NotFoundException
import kotlinx.coroutines.tasks.await


internal class LocationManager(
    private val context: Context,
) {

    private val _locationUpdates = MutableSharedFlow<LocationResult>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    internal val locationUpdates: Flow<LocationResult> = _locationUpdates

    private var locationCallback: LocationCallback? = null

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val settingsClient: SettingsClient by lazy {
        LocationServices.getSettingsClient(context)
    }

    suspend fun locationEnabled(): Boolean {
        val request = LocationSettingsRequest.Builder().addAllLocationRequests(
            listOf(
                LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, 1000).build(),
                LocationRequest.Builder(PRIORITY_BALANCED_POWER_ACCURACY, 1000).build(),
                LocationRequest.Builder(PRIORITY_PASSIVE, 1000).build(),
                LocationRequest.Builder(PRIORITY_LOW_POWER, 1000).build(),
            )
        ).build()

        val result = runCatching {
            settingsClient.checkLocationSettings(request).await()
        }.isSuccess

        return result || legacyLocationEnabled()
    }

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun currentLocation(priority: Int): Location {
        val cancellation = CancellationTokenSource()

        val location: Location? = fusedLocationClient
            .getCurrentLocation(priority, cancellation.token)
            .await(cancellation)

        if (location == null) {
            throw NotFoundException()
        }
        return location
    }

    @SuppressLint("MissingPermission")
    fun startTracking(request: LocationRequest): Flow<LocationResult> {
        if (locationCallback != null) return _locationUpdates

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                _locationUpdates.tryEmit(result)
            }
        }

        fusedLocationClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationCallback = callback

        return _locationUpdates
    }

    fun stopTracking() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
            locationCallback = null
        }
    }

    private fun legacyLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val providers = listOfNotNull(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) LocationManager.FUSED_PROVIDER else null,
        )

        return providers.any { locationManager.isProviderEnabled(it) }
    }
}
