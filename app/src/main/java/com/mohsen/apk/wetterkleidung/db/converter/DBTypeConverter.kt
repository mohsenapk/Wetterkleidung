package com.mohsen.apk.wetterkleidung.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohsen.apk.wetterkleidung.model.CurrentWeatherTitle

class DBTypeConverter {

    @TypeConverter
    fun fromCurrentWeatherTitleList(list: List<CurrentWeatherTitle>): String {
        val type = object : TypeToken<List<CurrentWeatherTitle>>() {}.type
        return Gson().toJson(list, type)
    }

    @TypeConverter
    fun toCurrentWeatherTitleList(jsonStr: String): List<CurrentWeatherTitle> {
        val type = object : TypeToken<List<CurrentWeatherTitle>>() {}.type
        return Gson().fromJson(jsonStr, type)
    }

}