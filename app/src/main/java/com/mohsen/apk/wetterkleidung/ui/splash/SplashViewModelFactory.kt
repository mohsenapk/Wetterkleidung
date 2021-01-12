package com.mohsen.apk.wetterkleidung.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager

class SplashViewModelFactory(private val prefs: InAppSharedPreferenceManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(prefs) as T
    }
}