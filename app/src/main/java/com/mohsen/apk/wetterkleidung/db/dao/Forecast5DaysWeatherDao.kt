package com.mohsen.apk.wetterkleidung.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeather

@Dao
interface Forecast5DaysWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setForecast5DaysWeather(forecast5DaysWeather: Forecast5DaysWeather)

    @Query("SELECT * FROM forecast_5days_weather")
    fun getForecast5DaysWeather(): Forecast5DaysWeather
}