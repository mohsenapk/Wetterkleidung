package com.mohsen.apk.wetterkleidung.repository

import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalService
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteService
import com.mohsen.apk.wetterkleidung.utility.date.DateHelper
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
    private val local: WeatherLocalService,
    private val dateHelper: DateHelper
) : WeatherRepository {
    override suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather = withContext(Dispatchers.IO) {
        val data = getCurrentWeatherLocal()
        data ?: getCurrentWeatherRemote(city, weatherUnit)
    }

    override suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): ForecastWeather = withContext(Dispatchers.IO) {
        val data = getForecastWeatherLocal()
        data ?: getForecastWeatherRemote(city, weatherUnit)
    }

    private suspend fun getCurrentWeatherLocal(): CurrentWeather? = withContext(Dispatchers.IO) {
        val localData: CurrentWeather? = local.getCurrentWeather()
        val createdDate = localData?.createdDate
        if (createdDate != null && !dateHelper.is30MinExpired(LocalDateTime.parse(createdDate)))
            localData
        else
            null
    }

    private suspend fun getCurrentWeatherRemote(
        city: String,
        weatherUnit: WeatherUnit
    ): CurrentWeather = withContext(Dispatchers.IO) {
        val remoteData = remote.getCurrentWeather(city, weatherUnit)
        local.setCurrentWeather(remoteData)
        remoteData
    }

    private suspend fun getForecastWeatherLocal(): ForecastWeather? = withContext(Dispatchers.IO) {
        val localData: ForecastWeather? = local.getForecastWeather()
        val createdDate = localData?.createdDate
        if (createdDate != null && !dateHelper.is30MinExpired(LocalDateTime.parse(createdDate)))
            localData
        else
            null
    }

    private suspend fun getForecastWeatherRemote(
        city: String,
        weatherUnit: WeatherUnit
    ): ForecastWeather = withContext(Dispatchers.IO) {
        val remoteData = remote.getForecastWeather(city, weatherUnit)
        local.setForecastWeather(remoteData)
        remoteData
    }
}