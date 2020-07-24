package com.mohsen.apk.wetterkleidung.ui.di

import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.repository.WeatherRepositoryImpl
import com.mohsen.apk.wetterkleidung.ui.MainViewModelFactory
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    @ActivityScope
    fun provideMainViewModelFactory(
        weatherRepository: WeatherRepositoryImpl
    ) = MainViewModelFactory(weatherRepository)

}