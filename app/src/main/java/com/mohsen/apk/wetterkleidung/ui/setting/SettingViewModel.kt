package com.mohsen.apk.wetterkleidung.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.model.WeatherUnit

class SettingViewModel(private val prefs: SharedPreferenceManager) : ViewModel() {

    private val _exitApp = MutableLiveData<Unit>()
    private val _showSnackBarText = MutableLiveData<String>()

    val exitApp: LiveData<Unit> = _exitApp
    val showSnackBarText: LiveData<String> = _showSnackBarText


    fun citySettingClicked() {

    }

    fun timeSettingClicked() {

    }

    fun weatherUnitCelsiusClicked() {
        prefs.setWeatherUnit(WeatherUnit.METRIC.name)
        _showSnackBarText.value = R.string.set_celsius.toString()
    }

    fun weatherUnitFahrenheitClicked() {
        prefs.setWeatherUnit(WeatherUnit.IMPERIAL.name)
        _showSnackBarText.value = R.string.set_fahrenheit.toString()
    }

    fun advancedAppClicked(bool: Boolean) {}
    fun exitAppClicked() {
        _exitApp.value = Unit
    }
}