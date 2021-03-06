package com.mohsen.apk.wetterkleidung.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeather
import com.mohsen.apk.wetterkleidung.model.Forecast5DaysWeatherDetail
import com.mohsen.apk.wetterkleidung.model.ForecastWeatherDetail
import com.mohsen.apk.wetterkleidung.model.WeatherTitle

class DBTypeConverter {

    @TypeConverter
    fun fromCurrentWeatherTitleList(list: List<WeatherTitle>): String {
        val type = object : TypeToken<List<WeatherTitle>>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toCurrentWeatherTitleList(jsonStr: String): List<WeatherTitle> {
        val type = object : TypeToken<List<WeatherTitle>>() {}.type
        return Gson().fromJson(jsonStr, type)
    }

    @TypeConverter
    fun fromForecastWeatherDetailList(list: List<ForecastWeatherDetail>): String {
        val type = object : TypeToken<List<ForecastWeatherDetail>>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toForecastWeatherDetailList(jsonStr: String): List<ForecastWeatherDetail> {
        val type = object : TypeToken<List<ForecastWeatherDetail>>() {}.type
        return Gson().fromJson(jsonStr, type)
    }

    @TypeConverter
    fun fromForecast5DaysWeatherList(list: List<Forecast5DaysWeatherDetail>): String {
        val type = object : TypeToken<List<Forecast5DaysWeatherDetail>>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toForecast5DaysWeatherList(jsonStr: String): List<Forecast5DaysWeatherDetail> {
        val type = object : TypeToken<List<Forecast5DaysWeatherDetail>>() {}.type
        return Gson().fromJson(jsonStr, type)
    }

}