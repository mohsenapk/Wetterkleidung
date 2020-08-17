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
import kotlin.math.roundToInt

class CityViewModel(
    private val prefs: SharedPreferenceManager,
    private val repository: WeatherRepository
) : ViewModel() {
    private val cities = mutableListOf<City>()

    private val _showAllCities = MutableLiveData<List<City>>()
    private val _showSnackBarError = MutableLiveData<String>()

    val showAllCities: LiveData<List<City>> = _showAllCities
    val showSnackBarError: LiveData<String> = _showSnackBarError

    fun start() {
        getAllCities()
    }

    fun addCityClicked(cityName: String) = viewModelScope.launch {
        if (cityName.isEmpty()) return@launch
        val duplicateCity = cities.filter { it.name.toUpperCase() == cityName.toUpperCase() }
        if(duplicateCity.isNotEmpty()){
            _showSnackBarError.value = "duplicate City Name"
           return@launch
        }
        val city = getCity(cityName)
        if (city != null) {
            saveCity(cityName, city)
        }
    }

    private fun saveCity(cityName: String, city: City) {
        prefs.putCity(cityName)
        cities.map { it.isDefault = false }
        city.isDefault = true
        cities.add(city)
        setDefaultCity(cityName)
        sendCitiesToView()
    }

    fun rvCityClicked(selectedCity: City) {
        setDefaultCity(selectedCity.name)
    }

    private fun setDefaultCity(cityName: String) {
        prefs.setCityDefault(cityName)
    }

    private fun sendCitiesToView() {
        _showAllCities.value = cities
    }

    private fun getAllCities() = viewModelScope.launch {
        val cityNames = prefs.getCities()
        val cityDefault = prefs.getCityDefault()
        cityNames.forEach { cityName ->
            val city = getCity(cityName)
            if (cityName == cityDefault)
                city?.isDefault = true
            city?.let { cities.add(city) }
        }
        sendCitiesToView()
    }

    private suspend fun getCity(cityName: String): City? {
        val weather = getCurrentWeather(cityName, WeatherUnit.METRIC)
        val weatherTemp = weather?.currentWeatherTemp?.temp?.roundToInt().toString()
        val weatherIcon = weather?.weatherTitle?.get(0)?.icon
        weather?.let {
            return City().apply {
                name = cityName
                if (weatherTemp != null) temp = weatherTemp
                if (weatherIcon != null) tempIconId = weatherIcon
            }
        }
        return null
    }


    private suspend fun getCurrentWeather(city: String, unit: WeatherUnit): CurrentWeather? {
        val response = repository.getCurrentWeather(city, WeatherUnit.METRIC)
        return when (response) {
            is RepositoryResponse.Success -> response.data
            is RepositoryResponse.Failure -> {
                _showSnackBarError.value = response.exception.message
                null
            }
        }
    }
}