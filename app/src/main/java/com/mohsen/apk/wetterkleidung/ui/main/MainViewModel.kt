package com.mohsen.apk.wetterkleidung.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val prefs: SharedPreferenceManager) : ViewModel() {
    private val _gotoCityFragment = MutableLiveData<Unit>()
    private val _gotoWeatherFragment = MutableLiveData<Unit>()
    private val _changeLoaderImageResource = MutableLiveData<Int>()

    val gotoWeatherFragment: LiveData<Unit> = _gotoWeatherFragment
    val gotoCityFragment: LiveData<Unit> = _gotoCityFragment
    val changeLoaderImageResource: LiveData<Int> = _changeLoaderImageResource

    fun start() = viewModelScope.launch {
        loaderImageStart()
        if (prefs.getCityDefault().isEmpty())
            _gotoCityFragment.value = Unit
        else
            _gotoWeatherFragment.value = Unit
    }

    private suspend fun loaderImageStart() {
        for(i in 1..6){
            delay(250)
            var resourceId = R.drawable.w1
            when (i) {
                1 -> resourceId = R.drawable.w1
                2 -> resourceId = R.drawable.w2
                3 -> resourceId = R.drawable.w3
                4 -> resourceId = R.drawable.w1
                5 -> resourceId = R.drawable.w2
                6 -> resourceId = R.drawable.w3
            }
            _changeLoaderImageResource.value = resourceId
        }
    }
}