package com.mohsen.apk.wetterkleidung.ui.main.di

import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.ui.di.ActivityScope
import com.mohsen.apk.wetterkleidung.ui.main.MainViewModelFactory
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(
        weatherRepository: WeatherRepository,
        dateHelper: DateHelper,
        imageHelper: ImageHelper
    ) = MainViewModelFactory(
        weatherRepository,
        dateHelper,
        imageHelper
    )
}