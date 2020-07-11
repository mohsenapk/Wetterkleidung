package com.mohsen.apk.wetterkleidung.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mohsen.apk.wetterkleidung.model.CurrentWeather

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun set(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather")
    fun get(): CurrentWeather

}