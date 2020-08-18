package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository

class CityViewModelFactory(
    private val application: BaseApplication,
    private val sharedPreferenceManager: SharedPreferenceManager,
    private val repository: WeatherRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel(
            application,
            sharedPreferenceManager,
            repository
        ) as T
    }
}