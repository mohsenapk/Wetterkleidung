package com.mohsen.apk.wetterkleidung.utility

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.mohsen.apk.wetterkleidung.internal.LocationPermissionNotGrantedException
import kotlinx.coroutines.CompletableDeferred

interface LocationHelper {
    suspend fun getLastLocationAsync(): Location?
}

class LocationHelperImpl(private val context: Context) : LocationHelper {

    private var lastLocation: Location? = null

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getLastLocationAsync(): Location? {
        if (hasLocationPermission()) {
            getLastLocation()
            if (lastLocation == null)
                getUpdatedLocation()
            return lastLocation
        } else
            throw LocationPermissionNotGrantedException()
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastLocation() {
        lastLocation = fusedLocationProviderClient.lastLocation.asDeferred().await()
    }

    @SuppressLint("MissingPermission")
    private fun getUpdatedLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val locationCallBack = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                result?.let {
                    lastLocation = result.lastLocation
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null)
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun <T> Task<T>.asDeferred(): CompletableDeferred<T> {
        val deferred = CompletableDeferred<T>()
        this.addOnSuccessListener {
            deferred.complete(it)
        }
        this.addOnFailureListener {
            deferred.completeExceptionally(it)
        }
        return deferred
    }

}