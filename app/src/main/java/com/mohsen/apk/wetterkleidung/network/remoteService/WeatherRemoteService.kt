package com.mohsen.apk.wetterkleidung.network.remoteService

import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.network.apiService.WeatherApi
import retrofit2.Retrofit

interface WeatherRemoteService {
    suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather

    suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): ForecastWeather

    suspend fun getForecast5DaysWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): Forecast5DaysWeather
}

class WeatherRemoteServiceImpl(private val retrofit: Retrofit) : WeatherRemoteService {

    override suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather = retrofit.create(WeatherApi::class.java)
        .getCurrentWeather(city, weatherUnit.name)

    override suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): ForecastWeather = retrofit.create(WeatherApi::class.java)
        .getForecastWeather(city, weatherUnit.name)

    override suspend fun getForecast5DaysWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): Forecast5DaysWeather =
        retrofit.create(WeatherApi::class.java)
            .getForecast5DaysWeather(city, weatherUnit.name)
}