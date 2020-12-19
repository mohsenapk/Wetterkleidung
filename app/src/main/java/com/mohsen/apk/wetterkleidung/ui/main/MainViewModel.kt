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

    val gotoWeatherFragment: LiveData<Unit> = _gotoWeatherFragment
    val gotoCityFragment: LiveData<Unit> = _gotoCityFragment

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

}