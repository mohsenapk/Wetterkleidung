package com.mohsen.apk.wetterkleidung.db.prefrences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohsen.apk.wetterkleidung.BuildConfig
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.model.TimeSelect
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import org.threeten.bp.LocalDateTime

interface SharedPreferenceManager {
    fun setCity(cityName: String)
    fun setCity(cityList: List<City>)
    fun setCityDefault(cityName: String)
    fun setLastLocation(lat: Double, lon: Double)
    fun setWeatherUnit(unit: String)
    fun setTimeSelectedList(timeSelectedList: List<TimeSelect>)
    fun setAdvanceApp(boolean: Boolean)

    fun getCityDefault(): String
    fun getCities(): List<String>
    fun getLastLocation(): Pair<Double, Double>?
    fun getWeatherUnit(): WeatherUnit
    fun getTimeSelectedList(): List<TimeSelect>?
    fun getAdvanceApp(): Boolean

    fun removeCities()
}

private const val cityKey = "CITY"
private const val cityDefaultKey = "CITY_DEFAULT_KEY"
private const val location = "LOCATION"
private const val weatherUnit = "WEATHER_UNIT"
private const val timeSelected = "TIME_SELECTED"
private const val advanceApp = "ADVANCE_APP"

class SharedPreferenceManagerImpl(
    context: Context,
    private val dateHelper: DateHelper
) : SharedPreferenceManager {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(BuildConfig.SHARED_PREFRENCES_NAME, 0)
    private val gson = Gson()

    override fun setCity(cityName: String) {
        if (getCityList().any { it == cityName }) return
        val list = getCityList().toMutableList()
        list.add(cityName)
        addCityList(list)
    }

    override fun setCity(cityList: List<City>) {
        val defaultCity = cityList.first() { it.isDefault }
        defaultCity?.let { setCityDefault(it.name) }
        cityList.forEach { setCity(it.name) }
    }

    override fun getCities(): List<String> {
        return getCityList()
    }

    private fun addCityList(list: List<String>) {
        prefs.edit().putString(cityKey, createJsonFromList(list)).apply()
    }

    private fun getCityList(): List<String> {
        val cities = prefs.getString(cityKey, "")
        return createListFromJson(cities)
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

    override fun setWeatherUnit(unit: String) {
        prefs.edit().putString(weatherUnit, unit).apply()
    }

    override fun getWeatherUnit(): WeatherUnit {
        val weatherUnitStr = prefs.getString(weatherUnit, "")
        weatherUnitStr?.let {
            if (weatherUnitStr.toUpperCase() == WeatherUnit.METRIC.name.toUpperCase())
                return WeatherUnit.METRIC
            if (weatherUnitStr.toUpperCase() == WeatherUnit.IMPERIAL.name.toUpperCase())
                return WeatherUnit.IMPERIAL
        }
        return WeatherUnit.METRIC
    }

    override fun setTimeSelectedList(timeSelectedList: List<TimeSelect>) {
        val strJson = createJsonFromTimeSelectedList(timeSelectedList)
        prefs.edit().putString(timeSelected, strJson).apply()
    }

    override fun getTimeSelectedList(): List<TimeSelect>? {
        val strJson = prefs.getString(timeSelected, "")
        return if (!strJson.isNullOrEmpty())
            createTimeSelectedListFromJson(strJson)
        else
            null
    }

    private fun createJsonFromList(list: List<String>): String =
        gson.toJson(list)

    private fun createListFromJson(str: String): List<String> {
        val typeToke = object : TypeToken<List<String>>() {}.type
        return if (str.isNotEmpty())
            gson.fromJson(str, typeToke)
        else
            return listOf()
    }

    private fun createJsonFromTimeSelectedList(timeSelected: List<TimeSelect>): String =
        gson.toJson(timeSelected)

    private fun createTimeSelectedListFromJson(jsonStr: String): List<TimeSelect> {
        val typeToken = object : TypeToken<List<TimeSelect>>() {}.type
        return gson.fromJson(jsonStr, typeToken)
    }

    override fun setAdvanceApp(boolean: Boolean) {
        prefs.edit().putBoolean(advanceApp, boolean).apply()
    }

    override fun getAdvanceApp(): Boolean =
        prefs.getBoolean(advanceApp, false)
}