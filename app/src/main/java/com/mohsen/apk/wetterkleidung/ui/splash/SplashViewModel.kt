package com.mohsen.apk.wetterkleidung.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager

class SplashViewModel(private val prefs: SharedPreferenceManager) : ViewModel() {

    private val _showCityDefaultName = MutableLiveData<String>()

    val showCityDefaultName: LiveData<String> = _showCityDefaultName

    fun onResume() {
        viewModelStart()
    }

    private fun viewModelStart() {
        showDefaultCityName()
    }

    private fun showDefaultCityName() {
        val defaultCity = prefs.getCityDefault()
        if(!defaultCity.isNullOrEmpty())
            _showCityDefaultName.value = defaultCity
    }

}