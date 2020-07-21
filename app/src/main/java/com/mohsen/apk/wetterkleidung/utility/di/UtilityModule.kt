package com.mohsen.apk.wetterkleidung.utility.di

import com.mohsen.apk.wetterkleidung.utility.date.DateHelper
import com.mohsen.apk.wetterkleidung.utility.date.DateHelperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilityModule {

    @Provides
    @Singleton
    fun provideDateHelper(): DateHelper =
        DateHelperImpl()
}