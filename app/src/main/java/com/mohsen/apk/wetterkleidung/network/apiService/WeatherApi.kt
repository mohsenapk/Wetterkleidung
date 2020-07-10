package com.mohsen.apk.wetterkleidung.network.apiService

import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") weatherUnitStr: String
    ): CurrentWeather

    @GET("forecast/daily")
    suspend fun getForecastWeather(
        @Query("q") city: String,
        @Query("units") weatherUnitStr: String
    ): ForecastWeather
}