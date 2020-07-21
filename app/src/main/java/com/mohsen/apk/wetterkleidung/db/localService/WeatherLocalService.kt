package com.mohsen.apk.wetterkleidung.db.localService

import com.mohsen.apk.wetterkleidung.db.WeatherDB
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import org.threeten.bp.LocalDateTime

interface WeatherLocalService {
    suspend fun setCurrentWeather(currentWeather: CurrentWeather)
    suspend fun getCurrentWeather(): CurrentWeather
    suspend fun setForecastWeather(forecastWeather: ForecastWeather)
    suspend fun getForecastWeather(): ForecastWeather
}

class WeatherLocalServiceImpl(private val db: WeatherDB) : WeatherLocalService {
    override suspend fun setCurrentWeather(currentWeather: CurrentWeather) {
        currentWeather.createdDate = LocalDateTime.now().toString()
        db.currentWeatherDao().set(currentWeather)
    }

    override suspend fun getCurrentWeather(): CurrentWeather =
        db.currentWeatherDao().get()

    override suspend fun setForecastWeather(forecastWeather: ForecastWeather) {
        forecastWeather.createdDate = LocalDateTime.now().toString()
        db.forecastWeatherDao().setForecastWeather(forecastWeather)
    }

    override suspend fun getForecastWeather(): ForecastWeather =
        db.forecastWeatherDao().getForecastWeather()

}