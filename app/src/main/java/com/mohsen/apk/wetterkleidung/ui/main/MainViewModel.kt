package com.mohsen.apk.wetterkleidung.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.model.SeekBarValue
import com.mohsen.apk.wetterkleidung.model.TimeSelect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val prefs: SharedPreferenceManager) : ViewModel() {
    private val _gotoCityFragment = MutableLiveData<Unit>()
    private val _gotoWeatherFragment = MutableLiveData<Unit>()
    private val _changeLoaderImageResource = MutableLiveData<Int>()
    private val _removeTopBackStackItem = MutableLiveData<Unit>()
    private val _finishApp = MutableLiveData<Unit>()
    private val _showLoadingView = MutableLiveData<Boolean>()
    private val _hasWeatherFragmentINnStackOrNot = MutableLiveData<HasFragmentListener>()
    private val _callOnBackPressed = MutableLiveData<Unit>()

    val gotoWeatherFragment: LiveData<Unit> = _gotoWeatherFragment
    val gotoCityFragment: LiveData<Unit> = _gotoCityFragment
    val changeLoaderImageResource: LiveData<Int> = _changeLoaderImageResource
    val removeTopBackStackItem: LiveData<Unit> = _removeTopBackStackItem
    val finishApp: LiveData<Unit> = _finishApp
    val hasWeatherFragmentINnStackOrNot: LiveData<HasFragmentListener> =
        _hasWeatherFragmentINnStackOrNot
    val callOnBackPressed: LiveData<Unit> = _callOnBackPressed

    interface HasFragmentListener {
        fun hasFragment(has: Boolean)
    }

    fun onResume(){
        start()
    }

    private fun start() = viewModelScope.launch {
        if (prefs.getCityDefault().isEmpty())
            _gotoCityFragment.value = Unit
        else
            _gotoWeatherFragment.value = Unit
        prefsDefaultValuesSet()
    }

    private fun prefsDefaultValuesSet() {
        setFirstTimeTimeSelectingList()
    }

    private fun setFirstTimeTimeSelectingList() {
        if (prefs.getTimeSelectedList().isNullOrEmpty()) {
            val listTimes = mutableListOf<TimeSelect>()
            SeekBarValue.values().toList().forEach {
                listTimes.add(TimeSelect(it.hours, true))
            }
            prefs.setTimeSelectedList(listTimes)
        }
    }

    fun backedFromCityFragment() {
        val defCity = prefs.getCityDefault()
        if (defCity.isEmpty()) {
            _callOnBackPressed.value = Unit
            return
        }
        ifHasWeatherFragmentGotoThat()
    }

    private fun ifHasWeatherFragmentGotoThat() {
        _hasWeatherFragmentINnStackOrNot.value = object : HasFragmentListener {
            override fun hasFragment(has: Boolean) {
                _gotoWeatherFragment.value = Unit
            }
        }
    }

    fun backFromSettingFragment() {
        ifHasWeatherFragmentGotoThat()
    }

}