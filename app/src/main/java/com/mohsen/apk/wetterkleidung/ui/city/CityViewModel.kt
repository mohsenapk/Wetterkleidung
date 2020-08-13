package com.mohsen.apk.wetterkleidung.ui.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CityViewModel : ViewModel() {
    private val _showAddCityDialog = MutableLiveData<Unit>()
    val showAddCityDialog: LiveData<Unit> = _showAddCityDialog

    fun addCityClicked() {
        _showAddCityDialog.value = Unit
    }
}