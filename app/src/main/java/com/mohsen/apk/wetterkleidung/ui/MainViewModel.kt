package com.mohsen.apk.wetterkleidung.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.apk.wetterkleidung.model.CurrentWeather
import com.mohsen.apk.wetterkleidung.model.ForecastWeather
import com.mohsen.apk.wetterkleidung.model.RepositoryResponse
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
    private val _snackBarErrorShow = MutableLiveData<String>()
    val snackBarErrorShow: LiveData<String> = _snackBarErrorShow

    fun getWeathers() {
        viewModelScope.launch {
            val cuWeather = weatherRepository
                .getCurrentWeather("bremen", WeatherUnit.METRIC)
            when (cuWeather) {
                is RepositoryResponse.Success ->
                    _currentWeather.value = cuWeather.data
                is RepositoryResponse.Filure ->
                    _snackBarErrorShow.value = cuWeather.exception.message
            }
        }
        viewModelScope.launch {
            val foWeather = weatherRepository
                .getForecastWeather("berlin", WeatherUnit.METRIC)
            when (foWeather) {
                is RepositoryResponse.Success ->
                    _forecastWeather.value = foWeather.data
                is RepositoryResponse.Filure ->
                    _snackBarErrorShow.value = foWeather.exception.message
            }

        }
    }
}