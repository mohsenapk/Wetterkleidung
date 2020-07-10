package com.mohsen.apk.wetterkleidung.network.apiService

import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeatherApi(
        @Query("q") city: String,
        @Query("units") weatherUnitStr: String
    ): CurrentWeather
}