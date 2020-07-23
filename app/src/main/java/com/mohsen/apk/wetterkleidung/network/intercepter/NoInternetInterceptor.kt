package com.mohsen.apk.wetterkleidung.network.intercepter

import android.content.Context
import android.net.ConnectivityManager
import com.mohsen.apk.wetterkleidung.internal.NoInternetConnectionException
import okhttp3.Interceptor
import okhttp3.Response

class NoInternetInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline())
            throw NoInternetConnectionException()
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        return if (connectivityManager.activeNetworkInfo != null)
            connectivityManager.activeNetworkInfo.isConnected
        else
            false
    }
}