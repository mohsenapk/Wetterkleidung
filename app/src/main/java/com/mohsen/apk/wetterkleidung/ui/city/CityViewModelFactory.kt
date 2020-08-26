package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.LocationHelper

class CityViewModelFactory(
    private val application: BaseApplication,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val repository: WeatherRepository,
    private val locationHelper: LocationHelper
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel(
            application,
            sharedPreferenceManager,
            repository,
            locationHelper
        ) as T
    }
}