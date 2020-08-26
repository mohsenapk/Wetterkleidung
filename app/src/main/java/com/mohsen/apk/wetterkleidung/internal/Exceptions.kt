package com.mohsen.apk.wetterkleidung.internal

import android.content.Context
import com.mohsen.apk.wetterkleidung.R
import java.io.IOException
import java.lang.Exception

class GeneralApiException : IOException() {
    override val message: String?
        get() = "Server Not Respond"
}

class NoInternetConnectionException : IOException() {
    override val message: String?
        get() = "no internet connection problem !!"
}

class CityNotFoundException : IOException() {
    override val message: String?
        get() = "city not found"
}

class LocationPermissionNotGrantedException : Exception() {
    override val message: String?
        get() = "no location permission granted"
}