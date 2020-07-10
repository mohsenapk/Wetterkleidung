package com.mohsen.apk.wetterkleidung.repository

import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalService
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface WeatherRepository {
    suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather

    suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): ForecastWeather
}

class WeatherRepositoryImpl(
    private val remote: WeatherRemoteService,
    private val local: WeatherLocalService
) : WeatherRepository {
    override suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather = withContext(Dispatchers.IO) {
        val data = remote.getCurrentWeather(city, weatherUnit)
        val insertedId = local.setCurrentWeather(data)
        data
    }

    override suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): ForecastWeather = withContext(Dispatchers.IO) {
        val data = remote.getForecastWeather(city, weatherUnit)
        val insertedId = local.setForecastWeather(data)
        data
    }

}