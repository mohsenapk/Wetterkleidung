package com.mohsen.apk.wetterkleidung.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager
import com.mohsen.apk.wetterkleidung.model.TimeSelect
import com.mohsen.apk.wetterkleidung.model.WeatherUnit

class SettingViewModel(private val prefs: InAppSharedPreferenceManager) : ViewModel() {

    private val _exitApp = MutableLiveData<Unit>()
    private val _showSnackBarText = MutableLiveData<String>()
    private val _showTimeSelectingDialog = MutableLiveData<List<TimeSelect>>()
    private val _setWeatherUnitRadioButtonToMetric = MutableLiveData<Unit>()
    private val _setWeatherUnitRadioButtonToImperial = MutableLiveData<Unit>()
    private val _setAdvanceApp = MutableLiveData<Boolean>()
    private val _gotoCityFragment = MutableLiveData<Any>()

    val exitApp: LiveData<Unit> = _exitApp
    val showSnackBarText: LiveData<String> = _showSnackBarText
    val showTimeSelectingDialog: LiveData<List<TimeSelect>> = _showTimeSelectingDialog
    val setWeatherUnitRadioButtonToMetric: LiveData<Unit> = _setWeatherUnitRadioButtonToMetric
    val setWeatherUnitRadioButtonToImperial: LiveData<Unit> = _setWeatherUnitRadioButtonToImperial
    val setAdvanceApp: LiveData<Boolean> = _setAdvanceApp
    val gotoCityFragment: LiveData<Any> = _gotoCityFragment

    fun onResume() {
        startViewModel()
    }

    private fun startViewModel() {
        weatherUnitSetup()
        advanceAppSetup()
    }

    private fun advanceAppSetup() {
        val advanceApp = prefs.getAdvanceApp()
        _setAdvanceApp.value = advanceApp
    }

    private fun weatherUnitSetup() {
        val weatherUnit = prefs.getWeatherUnit()
        if (weatherUnit == WeatherUnit.IMPERIAL)
            _setWeatherUnitRadioButtonToImperial.value = Unit
        else
            _setWeatherUnitRadioButtonToMetric.value = Unit
    }

    fun timeSettingClicked() {
        _showTimeSelectingDialog.value = prefs.getTimeSelectedList()
    }

    fun weatherUnitCelsiusClicked() {
        prefs.setWeatherUnit(WeatherUnit.METRIC.name)
    }

    fun weatherUnitFahrenheitClicked() {
        prefs.setWeatherUnit(WeatherUnit.IMPERIAL.name)
    }

    fun advancedAppClicked(bool: Boolean) {
        prefs.setAdvanceApp(bool)
    }

    fun exitAppClicked() {
        _exitApp.value = Unit
    }

    fun changeTimeSelectedList(it: List<TimeSelect>) {
        prefs.setTimeSelectedList(it)
    }

    fun cityClClicked() {
        _gotoCityFragment.value = Any()
    }
}