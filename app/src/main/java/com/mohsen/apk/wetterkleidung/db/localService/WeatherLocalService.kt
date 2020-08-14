package com.mohsen.apk.wetterkleidung.db.localService

import com.mohsen.apk.wetterkleidung.db.WeatherDB
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeather
import org.threeten.bp.LocalDateTime

interface WeatherLocalService {
    suspend fun setCurrentWeather(currentWeather: CurrentWeather)
    suspend fun getCurrentWeather(cityName: String): CurrentWeather
    suspend fun setForecastWeather(forecastWeather: ForecastWeather)
    suspend fun getForecastWeather(): ForecastWeather
    suspend fun setForecast5DaysWeather(forecast5DaysWeather: Forecast5DaysWeather)
    suspend fun getForecast5DaysWeather(): Forecast5DaysWeather
}

class WeatherLocalServiceImpl(private val db: WeatherDB) : WeatherLocalService {
    override suspend fun setCurrentWeather(currentWeather: CurrentWeather) {
        currentWeather.createdDate = LocalDateTime.now().toString()
        db.currentWeatherDao().set(currentWeather)
    }

    override suspend fun getCurrentWeather(cityName: String): CurrentWeather =
        db.currentWeatherDao().get(cityName)

    override suspend fun setForecastWeather(forecastWeather: ForecastWeather) {
        forecastWeather.createdDate = LocalDateTime.now().toString()
        db.forecastWeatherDao().setForecastWeather(forecastWeather)
    }

    override suspend fun getForecastWeather(): ForecastWeather =
        db.forecastWeatherDao().getForecastWeather()

    override suspend fun getForecast5DaysWeather(): Forecast5DaysWeather =
        db.forecast5DaysWeatherDao().getForecast5DaysWeather()

    override suspend fun setForecast5DaysWeather(
        forecast5DaysWeather: Forecast5DaysWeather
    ) {
        forecast5DaysWeather.createdDate = LocalDateTime.now().toString()
        db.forecast5DaysWeatherDao().setForecast5DaysWeather(forecast5DaysWeather)
    }

}