package com.mohsen.apk.wetterkleidung.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mohsen.apk.wetterkleidung.db.converter.DBTypeConverter
import com.mohsen.apk.wetterkleidung.db.dao.CurrentWeatherDao
import com.mohsen.apk.wetterkleidung.model.CurrentWeather

@Database(
    entities = [CurrentWeather::class],
    version = 1
)
@TypeConverters(DBTypeConverter::class)
abstract class WeatherDB : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
}