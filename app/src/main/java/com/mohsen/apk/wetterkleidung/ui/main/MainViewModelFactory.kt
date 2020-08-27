package com.mohsen.apk.wetterkleidung.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager

class MainViewModelFactory(private val prefs: SharedPreferenceManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(prefs) as T
    }
}