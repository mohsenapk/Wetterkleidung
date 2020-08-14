package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.RepositoryResponse
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import kotlinx.coroutines.launch

class CityViewModel(
    private val prefs: SharedPreferenceManager,
    private val repository: WeatherRepository
) : ViewModel() {

    private val _showAllCities = MutableLiveData<List<City>>()
    val showAllCities: LiveData<List<City>> = _showAllCities

    fun start(){
        getAllCities()
    }

    fun addCityClicked(cityName: String) {
        if(cityName.isNullOrEmpty())
            return
        prefs.putCity(cityName)
        getAllCities()
    }

    private fun getAllCities() = viewModelScope.launch {
        val list = mutableListOf<City>()
        val cityNames = prefs.getCities()
        cityNames.forEach { cityName ->
            var city = City()
            val weather = getCurrentWeather(cityName, WeatherUnit.METRIC)
            val cityFromCurrentWeather = weather?.let { createCityWithCurrentWeather(it) }
            if (cityFromCurrentWeather != null) city = cityFromCurrentWeather
            city.name = cityName
            list.add(city)
        }
        _showAllCities.value = list
    }

    private fun createCityWithCurrentWeather(weather: CurrentWeather): City {
        val temp = weather.currentWeatherTemp?.temp?.toInt()
        val icon = weather.weatherTitle?.get(0)?.icon
        val city = City()
        if (temp != null) city.temp = temp.toString()
        if (icon != null) city.tempIconId = icon
        return city
    }

    private suspend fun getCurrentWeather(city: String, unit: WeatherUnit): CurrentWeather? {
        val response = repository.getCurrentWeather(city, WeatherUnit.METRIC)
        return when (response) {
            is RepositoryResponse.Success -> response.data
            is RepositoryResponse.Filure -> null
        }
    }
}