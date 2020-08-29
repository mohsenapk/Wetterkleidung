package com.mohsen.apk.wetterkleidung.db.prefrences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohsen.apk.wetterkleidung.BuildConfig
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import org.threeten.bp.LocalDateTime
import java.lang.Exception

interface SharedPreferenceManager {
    fun putCity(cityName: String)
    fun putCity(cityList: List<City>)
    fun getCities(): List<String>
    fun setCityDefault(cityName: String)
    fun getCityDefault(): String
    fun setLastLocation(lat: Double, lon: Double)
    fun getLastLocation(): Pair<Double, Double>?
    fun removeCities()
}

private const val cityKey = "CITY"
private const val cityDefaultKey = "CITY_DEFAULT_KEY"
private const val location = "LOCATION"

class SharedPreferenceManagerImpl(
    context: Context,
    private val dateHelper: DateHelper
) : SharedPreferenceManager {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(BuildConfig.SHARED_PREFRENCES_NAME, 0)
    private val gson = Gson()

    override fun putCity(cityName: String) {
        getCityList().forEach { if (it == cityName) return }
        val list = getCityList().toMutableList()
        list.add(cityName)
        addCityList(list)
    }

    override fun putCity(cityList: List<City>) {
        cityList.forEach { putCity(it.name) }
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

    override fun setLastLocation(lat: Double, lon: Double) {
        val strLocation = "$lat-$lon@${LocalDateTime.now()}"
        prefs.edit().putString(location, strLocation).apply()
    }

    override fun getLastLocation(): Pair<Double, Double>? {
        val strLocation = prefs.getString(location, "")
        if (strLocation.isNotEmpty()) {
            val prefCreateDate = LocalDateTime.parse(strLocation.substringAfter("@"))
            if (!dateHelper.isToday(prefCreateDate) || dateHelper.isDateExpiredWithOneHour(
                    prefCreateDate
                )
            ) {
                prefs.edit().remove(location)
                return null
            }
            return Pair(
                getDoubleFromString(strLocation.substringBefore("-")),
                getDoubleFromString(strLocation.substringAfter("-"))
            )
        }
        return null
    }

    private fun getDoubleFromString(str: String): Double = try {
        str.toDouble()
    } catch (e: Exception) {
        0.0
    }

    override fun removeCities() {
        prefs.edit().remove(cityKey).apply()
        prefs.edit().remove(cityDefaultKey).apply()
    }
}