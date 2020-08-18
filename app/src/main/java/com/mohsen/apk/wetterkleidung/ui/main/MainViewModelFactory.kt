package com.mohsen.apk.wetterkleidung.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper

class MainViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val dateHelper: DateHelper,
    private val imageHelper: ImageHelper,
    private val prefs: SharedPreferenceManager
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            weatherRepository,
            dateHelper,
            imageHelper,
            prefs
        ) as T
    }
}