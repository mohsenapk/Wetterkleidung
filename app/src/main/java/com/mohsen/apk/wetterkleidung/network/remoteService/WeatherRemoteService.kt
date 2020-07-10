package com.mohsen.apk.wetterkleidung.network.remoteService

import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.network.apiService.WeatherApi
import retrofit2.Retrofit

interface WeatherRemoteService {
    suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather
}

class WeatherRemoteServiceImpl(private val retrofit: Retrofit) : WeatherRemoteService {
    override suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather =
        retrofit.create(WeatherApi::class.java).getCurrentWeatherApi(city, weatherUnit.name)
}