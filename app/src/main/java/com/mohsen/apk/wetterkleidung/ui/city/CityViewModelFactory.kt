package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager

class CityViewModelFactory(private val sharedPreferenceManager: SharedPreferenceManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel(sharedPreferenceManager) as T
    }
}