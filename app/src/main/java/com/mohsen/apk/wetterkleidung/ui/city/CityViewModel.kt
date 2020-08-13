package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager

class CityViewModel(private val prefs: SharedPreferenceManager) : ViewModel() {

    private val _showAllCities = MutableLiveData<List<String>>()
    val showAllCities: LiveData<List<String>> = _showAllCities

    fun addCityClicked(cityName: String) {
        prefs.putCity(cityName)
        getAllCities()
    }

    private fun getAllCities() {
        _showAllCities.value = prefs.getCities()
    }
}