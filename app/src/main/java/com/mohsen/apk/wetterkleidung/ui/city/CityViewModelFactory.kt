package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.LocationHelper

class CityViewModelFactory(
    private val application: BaseApplication,
    private val InAppSharedPreferenceManager: InAppSharedPreferenceManager,
    private val repository: WeatherRepository,
    private val locationHelper: LocationHelper
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel(
            application,
            InAppSharedPreferenceManager,
            repository,
            locationHelper
        ) as T
    }
}