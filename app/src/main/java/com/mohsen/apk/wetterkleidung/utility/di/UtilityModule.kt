package com.mohsen.apk.wetterkleidung.utility.di

import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.DateHelperImpl
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilityModule {

    @Provides
    @Singleton
    fun provideDateHelper(): DateHelper =
        DateHelperImpl()

    @Provides
    @Singleton
    fun provideImageHelper(): ImageHelper =
        ImageHelperImpl()

}