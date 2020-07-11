package com.mohsen.apk.wetterkleidung.base.di

import com.mohsen.apk.wetterkleidung.db.di.DBModule
import com.mohsen.apk.wetterkleidung.network.di.NetworkModule
import com.mohsen.apk.wetterkleidung.repository.WeatherRepositoryImpl
import com.mohsen.apk.wetterkleidung.repository.di.RepositoryModule
import com.mohsen.apk.wetterkleidung.utility.di.UtilityModule
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        NetworkModule::class,
        DBModule::class,
        RepositoryModule::class,
        UtilityModule::class]
)
@Singleton
interface ApplicationComponent {
    fun getRepository(): WeatherRepositoryImpl
}