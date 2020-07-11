package com.mohsen.apk.wetterkleidung.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohsen.apk.wetterkleidung.model.ForecastWeather

@Dao
interface ForecastWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setForecastWeather(forecastWeather: ForecastWeather)

    @Query("SELECT * FROM forecast_weather")
    fun getForecastWeather(): ForecastWeather
}