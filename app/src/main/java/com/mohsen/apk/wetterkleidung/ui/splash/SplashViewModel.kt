package com.mohsen.apk.wetterkleidung.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class SplashViewModel(private val prefs: InAppSharedPreferenceManager) : ViewModel() {

    private val _setLoadingImageResourceIs = MutableLiveData<Int>()
    private val _gotoMainActivity = MutableLiveData<Unit>()
    private val _showDefaultCityLayout = MutableLiveData<String>()

    val setLoadingImageResourceIs: LiveData<Int> = _setLoadingImageResourceIs
    val gotoMainActivity: LiveData<Unit> = _gotoMainActivity
    val showDefaultCityLayout: LiveData<String> = _showDefaultCityLayout

    fun onResume() {
        start()
    }

    private fun start() {
        setDefaultCityName()
        startLoadingImageAsync()
    }

    private fun setDefaultCityName() {
        prefs.getCityDefault()?.let { _showDefaultCityLayout.value = it }
    }

    private fun startLoadingImageAsync() = viewModelScope.async {
        var imageResourceId = 0
        for (i in 1..5) {
            delay(200)
            when (i) {
                1 -> imageResourceId = R.drawable.w1
                2 -> imageResourceId = R.drawable.w2
                3 -> imageResourceId = R.drawable.w3
                4 -> imageResourceId = R.drawable.w1
                5 -> imageResourceId = R.drawable.w2
            }
            _setLoadingImageResourceIs.value = imageResourceId
        }
        _gotoMainActivity.value = Unit
    }
}