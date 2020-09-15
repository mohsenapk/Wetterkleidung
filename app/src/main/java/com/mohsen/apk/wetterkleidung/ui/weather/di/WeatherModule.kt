package com.mohsen.apk.wetterkleidung.ui.weather.di

import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.weather.WeatherViewModelFactory
import com.mohsen.apk.wetterkleidung.utility.*
import dagger.Module
import dagger.Provides

@Module
class WeatherModule {

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(
        weatherRepository: WeatherRepository,
        dateHelper: DateHelper,
        imageHelper: ImageHelper,
        seekBarManager: SeekBarManager,
        dayNameManager: DayNameManager,
        resourceManager: ResourceManager,
        prefs: SharedPreferenceManager
    ) = WeatherViewModelFactory(
        weatherRepository,
        dateHelper,
        imageHelper,
        seekBarManager,
        dayNameManager,
        resourceManager,
        prefs
    )
}