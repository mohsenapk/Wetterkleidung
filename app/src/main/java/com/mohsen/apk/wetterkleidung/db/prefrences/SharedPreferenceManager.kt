package com.mohsen.apk.wetterkleidung.db.prefrences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohsen.apk.wetterkleidung.BuildConfig

interface SharedPreferenceManager {
    fun putCity(cityName: String)
    fun getCities(): List<String>
    fun setCityDefault(cityName: String)
    fun getCityDefault(): String
}

private const val cityKey = "CITY"
private const val cityDefaultKey = "CITY_DEFAULT_KEY"

class SharedPreferenceManagerImpl(context: Context) : SharedPreferenceManager {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(BuildConfig.SHARED_PREFRENCES_NAME, 0)
    private val gson = Gson()

    override fun putCity(cityName: String) {
        getCityList().forEach { if (it == cityName) return }
        val list = getCityList().toMutableList()
        list.add(cityName)
        addCityList(list)
    }

    override fun getCities(): List<String> {
        return getCityList()
    }

    private fun addCityList(list: List<String>) {
        val cities = gson.toJson(list)
        prefs.edit().putString(cityKey, cities).apply()
    }

    private fun getCityList(): List<String> {
        val cities = prefs.getString(cityKey, "")
        val typeToke = object : TypeToken<List<String>>() {}.type
        return if (cities.isNotEmpty())
            gson.fromJson(cities, typeToke)
        else
            listOf()
    }

    override fun setCityDefault(cityName: String) {
        prefs.edit().putString(cityDefaultKey, cityName).apply()
    }

    override fun getCityDefault(): String =
        prefs.getString(cityDefaultKey, "")
}