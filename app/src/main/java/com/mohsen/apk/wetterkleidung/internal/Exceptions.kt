package com.mohsen.apk.wetterkleidung.internal

import android.content.Context
import com.mohsen.apk.wetterkleidung.R

class NoInternetConnectionException(private val context: Context) : Exception() {
    override val message: String?
        get() = context.getString(R.string.app_name)
}