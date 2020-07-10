package com.mohsen.apk.wetterkleidung.db.localService

import com.mohsen.apk.wetterkleidung.db.WeatherDB
import com.mohsen.apk.wetterkleidung.model.CurrentWeather

interface WeatherLocalService {
    suspend fun setCurrentWeather(currentWeather: CurrentWeather): Long
    suspend fun getCurrentWeather(): CurrentWeather
}

class WeatherLocalServiceImpl(private val db: WeatherDB) : WeatherLocalService {
    override suspend fun setCurrentWeather(currentWeather: CurrentWeather) =
        db.currentWeatherDao().set(currentWeather)

    override suspend fun getCurrentWeather(): CurrentWeather =
        db.currentWeatherDao().get()

}