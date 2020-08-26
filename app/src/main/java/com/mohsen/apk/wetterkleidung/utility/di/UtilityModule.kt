package com.mohsen.apk.wetterkleidung.utility.di

import android.content.Context
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.utility.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilityModule {

    @Provides
    @Singleton
    fun provideDateHelper(): DateHelper = DateHelperImpl()

    @Provides
    @Singleton
    fun provideImageHelper(): ImageHelper = ImageHelperImpl()

    @Provides
    fun provideLocationHelper(application: BaseApplication): LocationHelper =
        LocationHelperImpl(application.applicationContext)

}