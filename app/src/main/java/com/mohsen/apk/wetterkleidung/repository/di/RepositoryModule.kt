package com.mohsen.apk.wetterkleidung.repository.di

import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalServiceImpl
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteServiceImpl
import com.mohsen.apk.wetterkleidung.repository.WeatherRepositoryImpl
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(
        remoteService: WeatherRemoteServiceImpl,
        localService: WeatherLocalServiceImpl,
        dateHelper: DateHelper,
        imageHelper: ImageHelper
    ) = WeatherRepositoryImpl(
        remoteService,
        localService,
        dateHelper,
        imageHelper
    )

}