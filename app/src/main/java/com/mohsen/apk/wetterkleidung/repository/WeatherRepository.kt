package com.mohsen.apk.wetterkleidung.repository

import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalService
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime

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
        val localData = local.getCurrentWeather()
        if (localData != null && !dateIsExpired(localData.createdDate)) {
            localData
        } else {
            val remoteData = remote.getCurrentWeather(city, weatherUnit)
            local.setCurrentWeather(remoteData)
            remoteData
        }
    }

    override suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): ForecastWeather = withContext(Dispatchers.IO) {
        val localData: ForecastWeather = local.getForecastWeather()
        if (localData != null && !dateIsExpired(localData.createdDate))
            localData
        else {
            val data = remote.getForecastWeather(city, weatherUnit)
            val insertedId = local.setForecastWeather(data)
            data
        }
    }

    private fun dateIsExpired(date: String?): Boolean {
        val oldDate = LocalDateTime.parse(date)
        val newDate = LocalDateTime.now()
        return (newDate >= oldDate.plusMinutes(30))
    }

}