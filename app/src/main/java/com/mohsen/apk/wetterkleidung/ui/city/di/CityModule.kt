package com.mohsen.apk.wetterkleidung.ui.city.di

import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.ui.city.CityViewModelFactory
import com.mohsen.apk.wetterkleidung.ui.base.di.ActivityScope
import com.mohsen.apk.wetterkleidung.utility.LocationHelper
import dagger.Module
import dagger.Provides

@Module
class CityModule {

    @Provides
    @ActivityScope
    fun provideCityViewModelFactory(
        application: BaseApplication,
        sharedPreferenceManager: SharedPreferenceManager,
        repository: WeatherRepository,
        locationHelper: LocationHelper
    ) = CityViewModelFactory(
        application,
        sharedPreferenceManager,
        repository,
        locationHelper
    )
}