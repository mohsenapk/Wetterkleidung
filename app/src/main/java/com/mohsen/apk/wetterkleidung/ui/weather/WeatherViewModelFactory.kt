package com.mohsen.apk.wetterkleidung.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mohsen.apk.wetterkleidung.db.prefrences.InAppSharedPreferenceManager
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.*

class WeatherViewModelFactory(
    private val weatherRepository: WeatherRepository,
    private val dateHelper: DateHelper,
    private val imageHelper: ImageHelper,
    private val seekBarManager: SeekBarManager,
    private val dayNameManager: DayNameManager,
    private val resourceManager: ResourceManager,
    private val prefs: InAppSharedPreferenceManager
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherViewModel(
            weatherRepository,
            dateHelper,
            imageHelper,
            seekBarManager,
            dayNameManager,
            resourceManager,
            prefs
        ) as T
    }
}