package com.mohsen.apk.wetterkleidung.ui.city

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
    private lateinit var weatherUnit : WeatherUnit

    private val _showAllCities = MutableLiveData<List<City>>()
    private val _showSnackBarError = MutableLiveData<String>()
    private val _showNoneCitySelectedError = MutableLiveData<Boolean>()
    private val _getLocationPermission = MutableLiveData<Unit>()
    private val _closeVirtualKeyboard = MutableLiveData<Unit>()

    val showAllCities: LiveData<List<City>> = _showAllCities
    val showSnackBarError: LiveData<String> = _showSnackBarError
    val showNoneCitySelectedError: LiveData<Boolean> = _showNoneCitySelectedError
    val getLocationPermission: LiveData<Unit> = _getLocationPermission
    val closeVirtualKeyboard: LiveData<Unit> = _closeVirtualKeyboard

    fun start() {
        weatherUnit = prefs.getWeatherUnit()
        getAllCities()
    }

    fun onResume() {
        weatherUnit = prefs.getWeatherUnit()
    }

    fun addCityClicked(cityName: String) = viewModelScope.launch {
        if (cityName.length < 3 || isDuplicatedCity(cityName))
            return@launch
        val city = getCity(cityName)
        city?.let { saveCity(city) }
    }

    private fun isDuplicatedCity(cityName: String): Boolean {
        val duplicateCity = cities.filter { it.name.toUpperCase() == cityName.toUpperCase() }
        return duplicateCity.isNotEmpty()
    }

    private fun saveCity(city: City) {
        if (isNewCity(city.name)) {
            _closeVirtualKeyboard.value = Unit
            saveCityIntoPrefsAndList(city)
            sendCitiesToView()
        }
    }

    private fun saveCityIntoPrefsAndList(city: City, isDefault: Boolean = true) {
        prefs.setCity(city.name)
        if (!isDefault) return
        setDefaultCity(city.name)
        cities.map { it.isDefault = false }
        city.isDefault = true
        cities.add(city)
    }

    private fun isNewCity(cityName: String): Boolean =
        !cities.any { it.name == cityName }

    fun rvCityClicked(selectedCity: City) {
        setDefaultCity(selectedCity.name)
    }

    private fun setDefaultCity(cityName: String) {
        prefs.setCityDefault(cityName)
    }

    private fun sendCitiesToView() {
        showHasNotCityError()
        if (cities.isNotEmpty())
            _showAllCities.value = cities
    }

    private fun showHasNotCityError() {
        _showNoneCitySelectedError.value = cities.isEmpty()
    }

    private fun getAllCities() = viewModelScope.launch {
        val cityNames = prefs.getCities()
        if (cityNames.isEmpty()) {
            showHasNotCityError()
            return@launch
        }
        setCitiesFromCityNames(cityNames)
        sendCitiesToView()
    }

    private suspend fun setCitiesFromCityNames(cityNames: List<String>) {
        if (cityNames.isEmpty()) return
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
                getCityFromLatAndLonRemote(location.latitude, location.longitude)
            } else
                getCityFromLatAndLonPref()
        } catch (e: LocationPermissionNotGrantedException) {
            _getLocationPermission.value = Unit
        }
    }

    private fun getCityFromLatAndLonPref() {
        val latAndLonPair = prefs.getLastLocation()
        latAndLonPair?.let {
            val lat = latAndLonPair.first
            val lon = latAndLonPair.second
            if (lat > 0 && lon > 0) {
                getCityFromLatAndLonRemote(lat, lon)
                Timber.d("location from pref $lat,$lon")
            }
        }
    }

    private fun getCityFromLatAndLonRemote(lat: Double, lon: Double) = viewModelScope.launch {
        val city = getCity(lat, lon)
        city?.let { city ->
            saveCity(city)
        }
    }

    private suspend fun getCurrentWeatherWithLatAndLon(
        lat: Double,
        lon: Double,
        unit: WeatherUnit
    ): CurrentWeather? =
        currentWeatherToRepoResponse(repository.getCurrentWeatherWithLatAndLon(lat, lon, unit))


    private fun currentWeatherToRepoResponse(response: RepositoryResponse<CurrentWeather>): CurrentWeather? {
        return when (response) {
            is RepositoryResponse.Success -> response.data
            is RepositoryResponse.Failure -> {
                _showSnackBarError.value = response.exception.message
                null
            }
        }
    }

    private suspend fun getCity(cityName: String): City? {
        val weather = getCurrentWeather(cityName, weatherUnit)
        weather?.let {
            return getCityFromWeather(weather)
        }
        return null
    }

    private suspend fun getCity(lat: Double, lon: Double): City? {
        val weather = getCurrentWeatherWithLatAndLon(lat, lon, weatherUnit)
        weather?.let {
            return getCityFromWeather(weather)
        }
        return null
    }


    private fun getCityFromWeather(
        weather: CurrentWeather
    ): City? {
        val cityName = weather.cityName
        val weatherTemp = weather.currentWeatherTemp?.temp?.roundToInt().toString()
        val weatherIcon = weather.weatherTitle?.get(0)?.icon
        return City().apply {
            name = cityName
            temp = weatherTemp
            if (weatherIcon != null) tempIconId = weatherIcon
        }
    }

    fun rvDeleteItem(city: City) {
        removeCity(city)
    }

    private fun removeCity(city: City) {
        if (city.isDefault && cities.size > 1)
            changeDefaultCityAfterRemove(city)
        cities.remove(city)
        updateCityPref(cities)
    }

    private fun changeDefaultCityAfterRemove(removedCity: City) {
        val removedItemPosition = cities.indexOf(removedCity)
        if (removedItemPosition == 0)
            cities[1].isDefault = true
        else
            cities[removedItemPosition - 1].isDefault = true
    }

    private fun updateCityPref(list: List<City>) {
        prefs.removeCities()
        prefs.setCity(list)
    }


}