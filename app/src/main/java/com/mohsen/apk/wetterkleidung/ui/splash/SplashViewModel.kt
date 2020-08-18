package com.mohsen.apk.wetterkleidung.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val prefs: SharedPreferenceManager) : ViewModel() {
    private val _gotoCityActivity = MutableLiveData<Unit>()
    private val _gotoMainActivity = MutableLiveData<Unit>()

    val gotoMainActivity: LiveData<Unit> = _gotoMainActivity
    val gotoCityActivity: LiveData<Unit> = _gotoCityActivity

    fun start() = viewModelScope.launch{
        delay(1000)
        if (prefs.getCityDefault().isEmpty())
            _gotoCityActivity.value = Unit
        else
            _gotoMainActivity.value = Unit
    }
}