package com.mohsen.apk.wetterkleidung.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.WeatherUnit
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather
    private val _forecastWeather = MutableLiveData<ForecastWeather>()
    val forecastWeather: LiveData<ForecastWeather> = _forecastWeather

    fun getWeathers() {
        viewModelScope.launch {
            val data = weatherRepository
                .getCurrentWeather("bremen", WeatherUnit.METRIC)
            _currentWeather.postValue(data)
        }
        viewModelScope.launch {
            val data = weatherRepository
                .getForecastWeather("berlin", WeatherUnit.METRIC)
            _forecastWeather.postValue(data)
        }
    }
}