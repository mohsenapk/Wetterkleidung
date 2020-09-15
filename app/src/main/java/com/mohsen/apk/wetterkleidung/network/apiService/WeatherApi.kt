package com.mohsen.apk.wetterkleidung.network.apiService

import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") weatherUnitStr: String
    ): Response<CurrentWeather>

    @GET("weather")
    suspend fun getCurrentWeatherWithLatAndLon(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") weatherUnitStr: String
    ): Response<CurrentWeather>

    @GET("forecast/daily")
    suspend fun getForecastWeather(
        @Query("q") city: String,
        @Query("units") weatherUnitStr: String
    ): Response<ForecastWeather>

    @GET("forecast")
    suspend fun getForecast5DaysWeather(
        @Query("q") city: String,
        @Query("units") weatherUnitStr: String
    ): Response<Forecast5DaysWeather>

}