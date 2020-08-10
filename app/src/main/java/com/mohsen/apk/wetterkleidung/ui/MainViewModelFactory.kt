package com.mohsen.apk.wetterkleidung.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper

class MainViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val dateHelper: DateHelper,
    private val imageHelper: ImageHelper
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            weatherRepository,
            dateHelper,
            imageHelper
        ) as T
    }
}