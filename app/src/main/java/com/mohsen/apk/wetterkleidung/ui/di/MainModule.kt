package com.mohsen.apk.wetterkleidung.ui.di

import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.ui.MainViewModelFactory
import com.mohsen.apk.wetterkleidung.ui.dialog.FragmentDialogManager
import com.mohsen.apk.wetterkleidung.ui.dialog.FragmentDialogManagerImpl
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

    @Provides
    @ActivityScope
    fun provideFragmentDialogManager(): FragmentDialogManager =
        FragmentDialogManagerImpl()

}