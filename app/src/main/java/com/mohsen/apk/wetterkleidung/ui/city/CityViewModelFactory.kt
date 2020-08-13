package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CityViewModelFactory: ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel() as T
    }
}