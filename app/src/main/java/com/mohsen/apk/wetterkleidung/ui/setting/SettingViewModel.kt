package com.mohsen.apk.wetterkleidung.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {

    private val _gotoCityActivity = MutableLiveData<Unit>()
    private val _gotoTimeSetting = MutableLiveData<Unit>()
    private val _exitApp = MutableLiveData<Unit>()

    val gotoCityActivity: LiveData<Unit> = _gotoCityActivity
    val gotoTimeSetting: LiveData<Unit> = _gotoTimeSetting
    val exitApp: LiveData<Unit> = _exitApp


    fun citySettingClicked() {
        _gotoCityActivity.value = Unit
    }

    fun timeSettingClicked() {
        _gotoTimeSetting.value = Unit
    }

    fun weatherUnitCelsiusClicked() {}
    fun weatherUnitFahrenheitClicked() {}
    fun advancedAppClicked(bool: Boolean) {}
    fun exitAppClicked() {
        _exitApp.value = Unit
    }
}