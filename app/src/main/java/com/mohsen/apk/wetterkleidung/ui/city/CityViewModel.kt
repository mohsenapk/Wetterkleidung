package com.mohsen.apk.wetterkleidung.ui.city

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.internal.LocationPermissionNotGrantedException
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.RepositoryResponse
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.LocationHelper
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt


class CityViewModel(
    private val application: BaseApplication,
    private val prefs: SharedPreferenceManager,
    private val repository: WeatherRepository,
    private val locationHelper: LocationHelper
) : AndroidViewModel(application) {
    private val cities = mutableListOf<City>()

    private val _showAllCities = MutableLiveData<List<City>>()
    private val _showSnackBarError = MutableLiveData<String>()
    private val _goToMainActivity = MutableLiveData<Unit>()
    private val _goToLastActivity = MutableLiveData<Unit>()
    private val _finishApp = MutableLiveData<Unit>()
    private val _showNoneCitySelectedError = MutableLiveData<Boolean>()
    private val _getLocationPermission = MutableLiveData<Unit>()

    val showAllCities: LiveData<List<City>> = _showAllCities
    val showSnackBarError: LiveData<String> = _showSnackBarError
    val goToMainActivity: LiveData<Unit> = _goToMainActivity
    val goToLastActivity: LiveData<Unit> = _goToMainActivity
    val showNoneCitySelectedError: LiveData<Boolean> = _showNoneCitySelectedError
    val finishApp: LiveData<Unit> = _finishApp
    val getLocationPermission: LiveData<Unit> = _getLocationPermission

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
        if (cities.any { it.name == cityName })
            return
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

    private suspend fun getCurrentWeather(cityName: String, unit: WeatherUnit): CurrentWeather? {
        val response = repository.getCurrentWeather(cityName, unit)
        return currentWeatherToRepoResponse(response)
    }

    fun fabGpsClicked() = viewModelScope.launch {
        try {
            val location = locationHelper.getLastLocationAsync()
            if (location != null) {
                prefs.setLastLocation(location.latitude, location.longitude)
                getCityFromLatAndLon(location.latitude, location.longitude)
                Timber.d("location - ${location.latitude},${location.longitude}")
            } else
                getCityFromLatAndLonPref()
        } catch (e: LocationPermissionNotGrantedException) {
            _getLocationPermission.value = Unit
            Timber.d("location - ${e.message}")
        }
    }

    private fun getCityFromLatAndLonPref() {
        val latAndLonPair = prefs.getLastLocation()
        latAndLonPair?.let {
            val lat = latAndLonPair.first
            val lon = latAndLonPair.second
            if (lat > 0 && lon > 0) {
                getCityFromLatAndLon(lat, lon)
                Timber.d("location from pref $lat,$lon")
            }
        }
    }

    private fun getCityFromLatAndLon(lat: Double, lon: Double) = viewModelScope.launch {
        val currentWeather = getCurrentWeatherWithLatAndLon(lat, lon, WeatherUnit.METRIC)
        currentWeather?.let { currentWeather ->
            val city = getCityFromWeather(currentWeather, currentWeather.cityName)
            city?.let { city ->
                saveCity(city.name, city)
            }
        }
    }

    private suspend fun getCurrentWeatherWithLatAndLon(
        lat: Double,
        lon: Double,
        unit: WeatherUnit
    ): CurrentWeather? {
        val response = repository.getCurrentWeatherWithLatAndLon(
            lat,
            lon,
            unit
        )
        return currentWeatherToRepoResponse(response)
    }

    private fun currentWeatherToRepoResponse(response: RepositoryResponse<CurrentWeather>): CurrentWeather? {
        return when (response) {
            is RepositoryResponse.Success -> response.data
            is RepositoryResponse.Failure -> {
                _showSnackBarError.value = response.exception.message
                null
            }
        }
    }

    private suspend fun getCity(
        cityName: String = "",
        lat: Double = 0.0,
        lon: Double = 0.0
    ): City? {
        var weather: CurrentWeather? = null
        if (cityName.isNotEmpty())
            weather = getCurrentWeather(cityName, WeatherUnit.METRIC)
        else if (lat > 0 && lon > 0)
            weather = getCurrentWeatherWithLatAndLon(lat, lon, WeatherUnit.METRIC)
        weather?.let {
            return getCityFromWeather(weather, cityName)
        }
        return null
    }

    private fun getCityFromWeather(
        weather: CurrentWeather,
        cityName: String
    ): City? {
        val weatherTemp = weather.currentWeatherTemp?.temp?.roundToInt().toString()
        val weatherIcon = weather.weatherTitle?.get(0)?.icon
        return City().apply {
            name = cityName
            temp = weatherTemp
            if (weatherIcon != null) tempIconId = weatherIcon
        }
    }

    fun onBackPressed() {
        if (prefs.getCities().isEmpty()) {
            _finishApp.value = Unit
            return
        } else
            _goToMainActivity.value = Unit
    }
}