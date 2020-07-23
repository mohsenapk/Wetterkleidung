package com.mohsen.apk.wetterkleidung.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohsen.apk.wetterkleidung.db.converter.DBTypeConverter
import com.mohsen.apk.wetterkleidung.db.dao.CurrentWeatherDao
import com.mohsen.apk.wetterkleidung.db.dao.Forecast5DaysWeatherDao
import com.mohsen.apk.wetterkleidung.db.dao.ForecastWeatherDao
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeather

@Database(
    entities = [
        CurrentWeather::class,
        ForecastWeather::class,
        Forecast5DaysWeather::class],
    version = 1
)
@TypeConverters(DBTypeConverter::class)
abstract class WeatherDB : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao
    abstract fun forecast5DaysWeatherDao(): Forecast5DaysWeatherDao
}