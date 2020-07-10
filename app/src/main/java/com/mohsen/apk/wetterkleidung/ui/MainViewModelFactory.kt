package com.mohsen.apk.wetterkleidung.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository

class MainViewModelFactory(private val weatherRepository: WeatherRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(weatherRepository) as T
    }
}