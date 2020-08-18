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
    private val _goToMainActivity = MutableLiveData<Unit>()
    private val _finishApp = MutableLiveData<Unit>()
    private val _showNoneCitySelectedError = MutableLiveData<Boolean>()

    val showAllCities: LiveData<List<City>> = _showAllCities
    val showSnackBarError: LiveData<String> = _showSnackBarError
    val goToMainActivity: LiveData<Unit> = _goToMainActivity
    val showNoneCitySelectedError: LiveData<Boolean> = _showNoneCitySelectedError
    val finishApp: LiveData<Unit> = _finishApp

    fun start() {
        getAllCities()
    }

    fun addCityClicked(cityName: String) = viewModelScope.launch {
        if (cityName.length < 3) return@launch
        val duplicateCity = cities.filter { it.name.toUpperCase() == cityName.toUpperCase() }
        if (duplicateCity.isNotEmpty()) {
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
        _goToMainActivity.value = Unit
    }

    private fun setDefaultCity(cityName: String) {
        prefs.setCityDefault(cityName)
    }

    private fun sendCitiesToView() {
        checkHasCitiesOrNot()
        if (cities.isNotEmpty())
            _showAllCities.value = cities
    }

    private fun checkHasCitiesOrNot() {
        _showNoneCitySelectedError.value = cities.isEmpty()
    }

    private fun getAllCities() = viewModelScope.launch {
        val cityNames = prefs.getCities()
        if (cityNames.isEmpty()) {
            checkHasCitiesOrNot()
            return@launch
        }
        setCitiesFromCityNames(cityNames)
        sendCitiesToView()
    }

    private suspend fun setCitiesFromCityNames(cityNames: List<String>) {
        val cityDefault = prefs.getCityDefault()
        cityNames.forEach { cityName ->
            val city = getCity(cityName)
            if (cityName == cityDefault)
                city?.isDefault = true
            city?.let { cities.add(city) }
        }
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

    fun fabGpsClicked() {

    }

    fun onBackPressed() {
        if (prefs.getCities().isNotEmpty())
            _goToMainActivity.value = Unit
        else
            _finishApp.value = Unit
    }
}