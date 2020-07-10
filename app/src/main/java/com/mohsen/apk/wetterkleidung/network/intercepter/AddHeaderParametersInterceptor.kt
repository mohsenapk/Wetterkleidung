package com.mohsen.apk.wetterkleidung.network.intercepter

import okhttp3.Interceptor
import okhttp3.Response

class AddHeaderParametersInterceptor(private val parameters: Map<String, String>) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request
            .url
            .newBuilder()
        for ((key, value) in parameters) {
            url.addQueryParameter(key, value)
        }
        return chain.proceed(
            request
                .newBuilder()
                .url(url.build())
                .build()
        )
    }
}