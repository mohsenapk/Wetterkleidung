package com.mohsen.apk.wetterkleidung.repository

import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalService
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

interface WeatherRepository {
    suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather
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
        Timber.d("from remote: \n ${data.toString()}")
        val insertId = local.setCurrentWeather(data)
        Timber.d("insert to db $insertId")
        data

    }
}