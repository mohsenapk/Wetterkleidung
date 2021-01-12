package com.mohsen.apk.wetterkleidung.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager

class SettingViewModelFactory(private val prefs: InAppSharedPreferenceManager) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingViewModel(prefs) as T
    }
}