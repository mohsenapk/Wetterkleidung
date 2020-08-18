package com.mohsen.apk.wetterkleidung.base.di

import com.mohsen.apk.wetterkleidung.base.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseModule(private val application: BaseApplication) {

    @Singleton
    @Provides
    fun provideBaseApplication() =
        application

}