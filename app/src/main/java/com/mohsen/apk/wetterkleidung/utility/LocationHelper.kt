package com.mohsen.apk.wetterkleidung.utility

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.mohsen.apk.wetterkleidung.internal.LocationPermissionNotGrantedException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

interface LocationHelper {
    fun getLastLocationAsync(): Deferred<Location?>
}

class LocationHelperImpl(private val context: Context) : LocationHelper {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override fun getLastLocationAsync(): Deferred<Location?> {
        return if (hasLocationPermission()) {
            fusedLocationProviderClient.lastLocation.asDeferred()
        } else
            throw LocationPermissionNotGrantedException()
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